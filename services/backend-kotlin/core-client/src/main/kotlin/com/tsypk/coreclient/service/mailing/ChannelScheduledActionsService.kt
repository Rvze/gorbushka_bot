package com.tsypk.coreclient.service.mailing

import com.tsypk.coreclient.model.stuff.SearchResult
import com.tsypk.coreclient.service.client.SupplierAirPodsClientService
import com.tsypk.coreclient.service.client.SupplierIphonesClientService
import com.tsypk.coreclient.service.enrichment.UsernamesEnricher
import com.tsypk.coreclient.service.presenting.apple.airPodsGroupByConcreteModelAndCountryAndSortedByPrice
import com.tsypk.coreclient.service.presenting.apple.iphoneModelMemoryCompareValue
import com.tsypk.coreclient.service.presenting.apple.sortAirPodsGroupByByModel
import com.tsypk.coreclient.util.telegram.Messages
import com.tsypk.coreclient.util.telegram.TelegramInfo
import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.types.ChatId
import dev.inmo.tgbotapi.types.message.HTMLParseMode
import kotlinx.coroutines.runBlocking
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import recognitioncommons.util.idString

@Service
class ChannelScheduledActionsService(
    private val iphonesClientService: SupplierIphonesClientService,
    private val airPodsClientService: SupplierAirPodsClientService,
    private val usernamesEnricher: UsernamesEnricher,
    private val resellerBot: TelegramBot,
    private val adminBot: TelegramBot,
) {

    @Scheduled(cron = "0 0 11 * * * ", zone = "Europe/Moscow")
    fun mailing1() {
        runBlocking {
            postBestPriceListToChannel()
        }
    }

    @Scheduled(cron = "0 0 14 * * * ", zone = "Europe/Moscow")
    fun mailing2() {
        runBlocking {
            postBestPriceListToChannel()
        }
    }

    @Scheduled(cron = "0 0 17 * * * ", zone = "Europe/Moscow")
    fun mailing3() {
        runBlocking {
            postBestPriceListToChannel()
        }
    }

    @Scheduled(cron = "0 0 20 * * * ", zone = "Europe/Moscow")
    fun mailing4() {
        runBlocking {
            postBestPriceListToChannel()
        }
    }

    private suspend fun postBestPriceListToChannel() {
        return
        try {
            val allToSend = iphonesToSend()
                .plus(airPodsToSend())

            allToSend.forEach {
                resellerBot.sendTextMessage(
                    text = it,
                    chatId = ChatId(TelegramInfo.PriceListChannel.chatId),
                    parseMode = HTMLParseMode,
                )
            }
        } catch (e: Exception) {
            TelegramInfo.ADMIN_IDS.forEach {
                adminBot.sendTextMessage(
                    chatId = ChatId(it),
                    text = Messages.ADMIN.plannedMailingException(e),
                    parseMode = HTMLParseMode,
                )
            }
            throw e
        }
    }

    private suspend fun iphonesToSend(): List<String> {
        val iphones = iphonesClientService.iphonesFindBest(null, null, null, null)
        usernamesEnricher.enrichSupplierUsernames(SearchResult(iphones = iphones.toMutableList()))
        val sortedIphones = iphones
            .groupBy { "${it.model.name}/${it.memory.name}" }
            .map { kv ->
                kv.value
                    .groupBy { it.country }
                    .map { countrykv ->
                        val byPrice = countrykv.value.groupBy { it.price }
                        val keys = byPrice.keys.sortedBy { it }.toList()
                        buildList {
                            add(byPrice[keys.first()]!!)
                            if (this.size < 3 && keys.getOrNull(1) != null) {
                                add(byPrice[keys[1]]!!)
                            }
                        }.flatten().take(3)
                    }.flatten()
            }
            .filter { it.isNotEmpty() }
            .sortedBy {
                iphoneModelMemoryCompareValue(it.first())
            }
        return sortedIphones.map {
            Messages.IPHONE.iphonesBestPricesMailing(it)
        }
    }

    private suspend fun airPodsToSend(): List<String> {
        val airPods = airPodsClientService.airPodsFindBest(null, null, null)
        usernamesEnricher.enrichSupplierUsernames(SearchResult(airPods = airPods.toMutableList()))
        val sortedAirPods = sortAirPodsGroupByByModel(
            airPodsGroupByConcreteModelAndCountryAndSortedByPrice(
                airPods
                    .groupBy { "${it.idString()}/${it.country.name}" }
                    .map { kv ->
                        val byPrice = kv.value.groupBy { it.price }
                        val keys = byPrice.keys.sortedBy { it }.toList()
                        buildList {
                            add(byPrice[keys.first()]!!)
                            if (this.size < 3 && keys.getOrNull(1) != null) {
                                add(byPrice[keys[1]]!!)
                            }
                        }.flatten().take(3)
                    }
                    .filter { it.isNotEmpty() }
                    .flatten()
            )
        )
        return sortedAirPods.map {
            Messages.AIRPODS.airPodsBestPricesMailing(it)
        }
    }
}
