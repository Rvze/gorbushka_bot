package com.tsypk.coreclient.application.supplier

import com.tsypk.coreclient.model.Supplier
import com.tsypk.coreclient.model.subscription.Subscription
import com.tsypk.coreclient.model.telegram.MessageUpdate
import com.tsypk.coreclient.service.StuffRecognitionService
import com.tsypk.coreclient.service.client.SupplierAirPodsClientService
import com.tsypk.coreclient.service.client.SupplierIphonesClientService
import com.tsypk.coreclient.service.client.SupplierMacbooksClientService
import com.tsypk.coreclient.service.presenting.apple.toHumanReadableString
import com.tsypk.coreclient.service.stat.StatisticsService
import com.tsypk.coreclient.util.telegram.Messages
import dev.inmo.tgbotapi.extensions.api.send.sendMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.types.ChatId
import dev.inmo.tgbotapi.types.message.HTMLParseMode
import org.springframework.stereotype.Service

@Service
class SupplierBotFlowService(
    private val supplierIphonesClientService: SupplierIphonesClientService,
    private val supplierAirPodsClientService: SupplierAirPodsClientService,
    private val stuffRecognitionService: StuffRecognitionService,
    private val statisticsService: StatisticsService,
    private val supplierMacbooksClientService: SupplierMacbooksClientService
) {
    suspend fun onRenewPriceCommand(
        ctx: BehaviourContext,
        msgUpdate: MessageUpdate,
        supplier: Supplier,
        subscription: Subscription,
    ) {
        if (msgUpdate.messageText in SupplierBotApplication.commands) {
            return
        }
        val result = stuffRecognitionService.recognizeFull(msgUpdate.messageText, supplier.id)
        ctx.bot.sendMessage(
            chatId = ChatId(supplier.id),
            text = Messages.SUPPLIER.composePriceListRecognized(result)
        )

        // STAT
        statisticsService.newIphoneRecognitionSupplier(supplier.id, result.iphones.size, result.errors.size)

        supplierIphonesClientService.updateSupplierIphones(
            supplierId = supplier.id,
            iphones = result.iphones,
        )
        supplierAirPodsClientService.updateAirPods(
            supplierId = supplier.id,
            airpods = result.airPods,
        )
        supplierMacbooksClientService.updateSupplierMacbooks(
            supplierId = supplier.id,
            macbooks = result.macbooks
        )


        val toSend = Messages.COMMON.composePagedByLimitMessages(
            prefix = "Ваш прайс-лист обновился на следующий:\n",
            toSend = result.iphones.map { it.toHumanReadableString() } +
                    result.airPods.map { it.toHumanReadableString() } +
                    result.macbooks.map { it.toHumanReadableString() },
        )
        toSend.forEach {
            ctx.bot.sendMessage(
                chatId = ChatId(supplier.id),
                text = it,
                parseMode = HTMLParseMode,
            )
        }
    }
}
