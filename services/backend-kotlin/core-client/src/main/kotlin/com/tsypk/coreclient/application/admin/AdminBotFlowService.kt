package com.tsypk.coreclient.application.admin

import com.tsypk.coreclient.model.ClientType
import com.tsypk.coreclient.model.Supplier
import com.tsypk.coreclient.model.stat.Stat
import com.tsypk.coreclient.service.SubscriptionMgmtService
import com.tsypk.coreclient.service.SupplierMgmtService
import com.tsypk.coreclient.service.UserMgmtService
import com.tsypk.coreclient.service.client.SupplierAirPodsClientService
import com.tsypk.coreclient.service.client.SupplierClientService
import com.tsypk.coreclient.service.client.SupplierIphonesClientService
import com.tsypk.coreclient.service.enrichment.BotDialogEnricher
import com.tsypk.coreclient.service.stat.StatisticsService
import com.tsypk.coreclient.service.subscription.SubscriptionFactory
import com.tsypk.coreclient.util.telegram.Messages
import com.tsypk.coreclient.util.telegram.Messages.DIALOG.composeAddedOrNo
import com.tsypk.coreclient.util.telegram.Messages.DIALOG.composeDeletedOrNo
import com.tsypk.coreclient.util.telegram.Messages.STAT.composeStatMessage
import com.tsypk.coreclient.util.telegram.Messages.STAT.composeSuppliersStat
import com.tsypk.coreclient.util.telegram.Messages.STAT.composeTotalMessagesStat
import com.tsypk.coreclient.util.telegram.Messages.SUPPLIER.composeSuppliersListCommand
import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.api.send.sendMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.types.ChatId
import dev.inmo.tgbotapi.types.message.HTMLParseMode
import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import dev.inmo.tgbotapi.types.message.content.MessageContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service

@Service
class AdminBotFlowService(
    private val userMgmtService: UserMgmtService,
    private val subscriptionMgmtService: SubscriptionMgmtService,
    private val supplierMgmtService: SupplierMgmtService,

    private val supplierIphonesClientService: SupplierIphonesClientService,
    private val supplierAirPodsClientService: SupplierAirPodsClientService,
    private val supplierClientService: SupplierClientService,

    private val botDialogEnricher: BotDialogEnricher,
    private val subscriptionFactory: SubscriptionFactory,

    private val statisticsService: StatisticsService,

    private val resellerBot: TelegramBot,
    private val supplierBot: TelegramBot,
) {
    suspend fun onAddStuffCommand(
        ctx: BehaviourContext,
        msg: CommonMessage<MessageContent>,
        supplierUsername: String
    ) {
        val supplier = supplierMgmtService.findByUsername(supplierUsername)
        if (supplier == null) {
            ctx.bot.sendMessage(
                chat = msg.chat,
                text = Messages.SUPPLIER.onSupplierNotFound,
            )
            return
        }

        val result = botDialogEnricher.askPriceList(ctx, msg, supplierUsername)

        ctx.bot.sendMessage(
            chat = msg.chat,
            text = Messages.SUPPLIER.composePriceListRecognized(result),
        )

        var excToThrow: Exception? = null
        try {
            supplierIphonesClientService.updateSupplierIphones(
                supplierId = supplier.id,
                iphones = result.iphones,
            )
        } catch (e: Exception) {
            excToThrow = e
        }

        try {
            supplierAirPodsClientService.updateAirPods(
                supplierId = supplier.id,
                airpods = result.airPods,
            )
        } catch (e: Exception) {
            excToThrow = e
        }

        if (excToThrow != null) {
            throw excToThrow
        }
    }

    suspend fun onAddSubscriptionCommand(
        ctx: BehaviourContext,
        msg: CommonMessage<MessageContent>,
    ) {
        val username = botDialogEnricher.askUsername(ctx = ctx, msg = msg)
        val supplier = supplierMgmtService.findByUsername(username)
        if (supplier == null) {
            ctx.bot.sendMessage(
                chat = msg.chat,
                text = Messages.SUPPLIER.onSupplierNotFound,
            )
            return
        }

        val subscription = subscriptionFactory.manualSubscription(supplier.id, ClientType.SUPPLIER)
        subscriptionMgmtService.createOrUpdate(subscription)

        ctx.bot.sendMessage(
            chat = msg.chat,
            text = Messages.DIALOG.composeSubscriptionForSupplierCreated(supplier.id, supplier.username),
        )
    }

    suspend fun onAddSupplierCommand(ctx: BehaviourContext, msg: CommonMessage<MessageContent>) {
        val username = botDialogEnricher.askUsername(ctx, msg)
        val id = botDialogEnricher.askId(ctx, msg)
        val title = botDialogEnricher.askTitle(ctx, msg)

        val toSend = Messages.DIALOG.composeSupplierCreateAction(
            username = username,
            id = id,
            title = title,
        )

        val needToCreate = botDialogEnricher.askYesOrNo(ctx, msg, toSend)
        if (needToCreate) {
            val supplier = Supplier(
                id = id,
                username = username,
                title = title,
            )
            withContext(Dispatchers.IO) {
                supplierMgmtService.create(supplier)
            }
        }

        ctx.bot.sendMessage(msg.chat, composeAddedOrNo(needToCreate))
    }

    suspend fun onDeleteSupplierCommand(ctx: BehaviourContext, msg: CommonMessage<MessageContent>) {
        val username = botDialogEnricher.askUsername(ctx, msg)
        val supplier = supplierMgmtService.findByUsername(username)
        if (supplier == null) {
            ctx.bot.sendMessage(
                chat = msg.chat,
                text = Messages.SUPPLIER.composeSupplierNotFound(username),
            )
            return
        }

        val toSend = Messages.DIALOG.composeSupplierDeleteAction(username, supplier.id, supplier.title ?: "")
        val needToDelete = botDialogEnricher.askYesOrNo(ctx, msg, toSend)
        if (needToDelete) {
            runBlocking {
                supplierIphonesClientService.updateSupplierIphones(
                    supplierId = supplier.id,
                    iphones = emptyList(),
                )
            }
            withContext(Dispatchers.IO) {
                supplierMgmtService.delete(supplier)
                subscriptionMgmtService.delete(supplier.id, ClientType.SUPPLIER)
            }
        }

        ctx.bot.sendMessage(msg.chat, composeDeletedOrNo(needToDelete))
    }

    suspend fun onSuppliersCommand(ctx: BehaviourContext, cmd: CommonMessage<MessageContent>) {
        ctx.bot.sendMessage(
            chat = cmd.chat,
            text = composeSuppliersListCommand(suppliers = supplierMgmtService.allSuppliersMap.values),
        )
    }

    suspend fun onStatCommand(ctx: BehaviourContext, cmd: CommonMessage<MessageContent>) {
        val stat = Stat(statisticsService.r)
        ctx.bot.sendMessage(
            chat = cmd.chat,
            text = composeStatMessage(stat),
            parseMode = HTMLParseMode,
        )

        val suppliers = userMgmtService.enrichUsernames(stat.statRegistry.supplierStat.suppliersAddingIphones)
        ctx.bot.sendMessage(
            chat = cmd.chat,
            text = composeSuppliersStat(suppliers.values),
            parseMode = HTMLParseMode,
        )
        val messageWriters = userMgmtService.enrichUsernames(stat.statRegistry.commonStat.idToMessages.keys)
        ctx.bot.sendMessage(
            chat = cmd.chat,
            text = composeTotalMessagesStat(messageWriters, stat.statRegistry.commonStat.idToMessages),
            parseMode = HTMLParseMode,
        )
    }

    suspend fun onSupplierStatCommand(ctx: BehaviourContext, cmd: CommonMessage<MessageContent>) {
        val suppliersStaffInfo = supplierClientService.getSupplierStaffInfo()
        val toSend = suppliersStaffInfo.joinToString(separator = "\n\n") {
            val supplier = supplierMgmtService.findByIdOrNull(it.supplierId)
            """
                |<u><b>Поставщик: ${supplier?.username ?: it.supplierId}</b></u>
                |        Айфонов: ${it.staffInfo.iphones}
                |        Эирподсов: ${it.staffInfo.airPods}
                |        Макбуков: ${it.staffInfo.macBooks}
                |        Плэйстэйшенов: ${it.staffInfo.playstations}
            """.trimMargin()
        }
        ctx.bot.sendMessage(
            chat = cmd.chat,
            text = toSend,
            parseMode = HTMLParseMode,
        )
    }

    suspend fun onSendResellersCommand(
        ctx: BehaviourContext,
        msg: CommonMessage<MessageContent>,
        ids: List<Long>,
    ) {
        val toAsk = Messages.DIALOG.requireTextToSendToResellers
        val text = botDialogEnricher.askText(ctx, msg, toAsk)

        sendListedUsers(ctx, msg, ids, resellerBot, text)
    }

    suspend fun onSendSuppliersCommand(
        ctx: BehaviourContext,
        msg: CommonMessage<MessageContent>,
        ids: List<Long>,
    ) {
        val toAsk = Messages.DIALOG.requireTextToSendToSuppliers
        val text = botDialogEnricher.askText(ctx, msg, toAsk)

        sendListedUsers(ctx, msg, ids, supplierBot, text)
    }

    private suspend fun sendListedUsers(
        ctx: BehaviourContext,
        msg: CommonMessage<MessageContent>,
        ids: List<Long>,
        bot: TelegramBot,
        toSend: String,
    ) {
        val toAsk = Messages.DIALOG.composeSendToListedUsers(ids, toSend)
        val needToSend = botDialogEnricher.askYesOrNo(ctx, msg, toAsk)

        val unsuccessfullySentUsers = mutableListOf<Long>()
        if (needToSend) {
            ids.forEach {
                try {
                    bot.sendMessage(
                        chatId = ChatId(it),
                        text = toSend,
                    )
                } catch (e: Exception) {
                    unsuccessfullySentUsers.add(it)
                }
            }
        }

        val successfullySentUsers = (ids.toSet() - unsuccessfullySentUsers.toSet()).toList()
        ctx.sendMessage(
            chat = msg.chat,
            text = Messages.DIALOG.composeSuccessfullySentToListedUsers(successfullySentUsers)
        )

        if (unsuccessfullySentUsers.isNotEmpty()) {
            ctx.sendMessage(
                chat = msg.chat,
                text = Messages.DIALOG.composeUnsuccessfullySentToListedUsers(unsuccessfullySentUsers)
            )
        }
    }
}
