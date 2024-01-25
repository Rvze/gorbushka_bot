package com.nmakarov.coreclient.application.reseller

import com.nmakarov.coreclient.api.feign.dto.notification.NotificationActionType
import com.nmakarov.coreclient.api.feign.dto.notification.NotificationRequest
import com.nmakarov.coreclient.exception.MaxRequestsExceededException
import com.nmakarov.coreclient.exception.RecognitionException
import com.nmakarov.coreclient.exception.SubscriptionNotFoundException
import com.nmakarov.coreclient.model.ClientType
import com.nmakarov.coreclient.model.stuff.SearchResult
import com.nmakarov.coreclient.service.RecognitionResultEnrichmentService
import com.nmakarov.coreclient.service.StuffRecognitionService
import com.nmakarov.coreclient.service.SubscriptionMgmtService
import com.nmakarov.coreclient.service.client.NotificationClientService
import com.nmakarov.coreclient.service.client.SupplierAirPodsClientService
import com.nmakarov.coreclient.service.enrichment.UsernamesEnricher
import com.nmakarov.coreclient.service.presenting.apple.airPodsGroupByConcreteModelAndCountryAndSortedByPrice
import com.nmakarov.coreclient.service.presenting.apple.iphonesGroupByConcreteModelAndCountryAndSortedByPrice
import com.nmakarov.coreclient.service.presenting.apple.macbooksGroupByConcreteModelAndCountryAndSortedByPrice
import com.nmakarov.coreclient.service.stat.StatisticsService
import com.nmakarov.coreclient.util.telegram.Messages
import com.nmakarov.coreclient.util.telegram.messageUpdateFromTelegramMessage
import com.nmakarov.coreclient.util.toSingleString
import dev.inmo.tgbotapi.extensions.api.send.sendMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.utils.textContentOrNull
import dev.inmo.tgbotapi.types.message.HTMLParseMode
import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import dev.inmo.tgbotapi.types.message.content.MessageContent
import org.springframework.stereotype.Service
import recognitioncommons.models.apple.airpods.AirPods
import recognitioncommons.models.apple.iphone.Iphone
import recognitioncommons.models.apple.macbook.Macbook
import recognitioncommons.service.recognition.apple.airpods.AirPodsRecognitionService
import recognitioncommons.util.idStringWithCountry
import recognitioncommons.util.idString

@Service
class ResellerBotFlowService(
        private val usernamesEnricher: UsernamesEnricher,
        private val subscriptionMgmtService: SubscriptionMgmtService,
        private val stuffRecognitionService: StuffRecognitionService,
        private val recognitionResultEnrichmentService: RecognitionResultEnrichmentService,
        private val airPodsClientService: SupplierAirPodsClientService,
        private val notificationClientService: NotificationClientService,
        private val statisticsService: StatisticsService,
) {
    private companion object {
        const val MAX_REQUESTS = 10
    }

    suspend fun onSubscriptionInfoCommand(
            ctx: BehaviourContext,
            msg: CommonMessage<MessageContent>,
    ) {
        try {
            val subscription = subscriptionMgmtService.findOne(
                    tgBotUserId = msg.chat.id.chatId,
                    clientType = ClientType.RESELLER,
            )
            ctx.bot.sendMessage(
                    chat = msg.chat,
                    text = Messages.SUBSCRIPTION.subscriptionInfo(subscription),
            )
        } catch (e: SubscriptionNotFoundException) {
            ctx.bot.sendMessage(
                    chat = msg.chat,
                    text = Messages.SUBSCRIPTION.noSubscription,
            )
        }
    }

    suspend fun onPotentialSearchQuery(
            ctx: BehaviourContext,
            msg: CommonMessage<MessageContent>,
    ) {
        val text = msg.content.textContentOrNull()!!.text
        if (AirPodsRecognitionService.isAirPodsWord(text)) {
            val airPodsResult = SearchResult(
                    airPods = airPodsClientService.airPodsGetAll().toMutableList(),
            )
            enrichAndAnswer(ctx, msg, airPodsResult)
            return
        }

        val recognized = stuffRecognitionService.recognizeSearchModels(text)
        if (recognized.allEmpty()) {
            throw RecognitionException(errorMsg = recognized.errors.toSingleString())
        }
        if (recognized.totalRecognized() > MAX_REQUESTS) {
            throw MaxRequestsExceededException(MAX_REQUESTS)
        }

        val found = recognitionResultEnrichmentService.enrichForRecognitionResult(recognized)
        enrichAndAnswer(ctx, msg, found)
    }

    suspend fun onSubscribeOnItemCommand(
            ctx: BehaviourContext,
            msg: CommonMessage<MessageContent>,
    ) {
        //TODO
        val text = msg.content.textContentOrNull()!!.text
        val item = text.replace("/subscribe ", "")
        val recognized = stuffRecognitionService.recognizeSearchModels(item)

        if (recognized.allEmpty()) {
            throw RecognitionException(errorMsg = recognized.errors.toSingleString())
        }
        if (recognized.totalRecognized() > MAX_REQUESTS) {
            throw MaxRequestsExceededException(MAX_REQUESTS)
        }
        val found = recognitionResultEnrichmentService.enrichForRecognitionResult(recognized)
        val msgUpdate = messageUpdateFromTelegramMessage(msg)
        enrichAndSend(msgUpdate.sender!!.id, found)
    }

    suspend fun onNotificationsCommand(ctx: BehaviourContext,
                                       msg: CommonMessage<MessageContent>) {
        val msgUpdate = messageUpdateFromTelegramMessage(msg)
        val found = notificationClientService.getNotifications(msgUpdate.sender!!.id)
        val response = StringBuilder()
        found.body!!.forEach {
            response.append(it.text)
            response.append("\n")
        }
        ctx.bot.sendMessage(
                chat = msg.chat,
                text = response.toString(),
                parseMode = HTMLParseMode
        )

    }

    private suspend fun enrichAndSend(userId: Long, searchResult: SearchResult) {
        if (searchResult.iphones.isNotEmpty()) {
            searchResult.iphones.forEach {
                notificationClientService.subscribeOnItem(
                        userId,
                        NotificationRequest(
                                actionType = NotificationActionType.SUBSCRIBE,
                                modelId = it.idStringWithCountry()
                        )
                )
            }
        }
        if (searchResult.airPods.isNotEmpty()) {
            searchResult.airPods.forEach {
                notificationClientService.subscribeOnItem(
                        userId,
                        NotificationRequest(
                                actionType = NotificationActionType.SUBSCRIBE,
                                modelId = it.idString()
                        )
                )
            }
        }
        if (searchResult.macbooks.isNotEmpty()) {
            searchResult.macbooks.forEach {
                notificationClientService.subscribeOnItem(
                        userId,
                        NotificationRequest(
                                actionType = NotificationActionType.SUBSCRIBE,
                                modelId = it.idString()
                        )
                )
            }
        }

    }

    private suspend fun enrichAndAnswer(
            ctx: BehaviourContext,
            msg: CommonMessage<MessageContent>,
            found: SearchResult
    ) {
        usernamesEnricher.enrichSupplierUsernames(found)

        if (found.allEmpty()) {
            ctx.bot.sendMessage(
                    chat = msg.chat,
                    text = Messages.SEARCH.onEmptySearchResult(),
                    parseMode = HTMLParseMode,
            )
            return
        }

        respondIphoneSearchResult(ctx, msg, found.iphones)
        respondAirPodsSearchResult(ctx, msg, found.airPods)
        responsMacbooksSearchResult(ctx, msg, found.macbooks)
    }

    private suspend fun respondIphoneSearchResult(
            ctx: BehaviourContext,
            msg: CommonMessage<MessageContent>,
            iphones: List<Iphone>,
    ) {
        statisticsService.newIphonesResponse(iphones.size)
        val sortedIphones = iphonesGroupByConcreteModelAndCountryAndSortedByPrice(iphones)
        sortedIphones.forEach {
            ctx.bot.sendMessage(
                    chat = msg.chat,
                    text = Messages.IPHONE.iphoneBestPricesForSearchModel(it),
                    parseMode = HTMLParseMode,
            )
        }
    }

    private suspend fun respondAirPodsSearchResult(
            ctx: BehaviourContext,
            msg: CommonMessage<MessageContent>,
            airPods: List<AirPods>,
    ) {
        val sortedAirPods = airPodsGroupByConcreteModelAndCountryAndSortedByPrice(airPods)
        sortedAirPods.forEach {
            ctx.bot.sendMessage(
                    chat = msg.chat,
                    text = Messages.AIRPODS.airPodsBestPricesForSearchModel(it),
                    parseMode = HTMLParseMode,
            )
        }
    }

    private suspend fun responsMacbooksSearchResult(
            ctx: BehaviourContext,
            msg: CommonMessage<MessageContent>,
            macbooks: List<Macbook>
    ) {
        val sortedMacbooks = macbooksGroupByConcreteModelAndCountryAndSortedByPrice(macbooks)
        sortedMacbooks.forEach {
            ctx.bot.sendMessage(
                    chat = msg.chat,
                    text = Messages.MACBOOK.macbooksBestPricesForSearchModel(it),
                    parseMode = HTMLParseMode
            )
        }
    }
}
