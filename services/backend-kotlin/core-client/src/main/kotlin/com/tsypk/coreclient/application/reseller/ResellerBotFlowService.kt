package com.tsypk.coreclient.application.reseller

import com.tsypk.coreclient.exception.MaxRequestsExceededException
import com.tsypk.coreclient.exception.RecognitionException
import com.tsypk.coreclient.exception.SubscriptionNotFoundException
import com.tsypk.coreclient.model.ClientType
import com.tsypk.coreclient.model.stuff.SearchResult
import com.tsypk.coreclient.service.RecognitionResultEnrichmentService
import com.tsypk.coreclient.service.StuffRecognitionService
import com.tsypk.coreclient.service.SubscriptionMgmtService
import com.tsypk.coreclient.service.client.SupplierAirPodsClientService
import com.tsypk.coreclient.service.enrichment.UsernamesEnricher
import com.tsypk.coreclient.service.presenting.apple.airPodsGroupByConcreteModelAndCountryAndSortedByPrice
import com.tsypk.coreclient.service.presenting.apple.iphonesGroupByConcreteModelAndCountryAndSortedByPrice
import com.tsypk.coreclient.service.presenting.apple.macbooksGroupByConcreteModelAndCountryAndSortedByPrice
import com.tsypk.coreclient.service.stat.StatisticsService
import com.tsypk.coreclient.util.telegram.Messages
import com.tsypk.coreclient.util.toSingleString
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

@Service
class ResellerBotFlowService(
    private val usernamesEnricher: UsernamesEnricher,
    private val subscriptionMgmtService: SubscriptionMgmtService,
    private val stuffRecognitionService: StuffRecognitionService,
    private val recognitionResultEnrichmentService: RecognitionResultEnrichmentService,
    private val airPodsClientService: SupplierAirPodsClientService,
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
