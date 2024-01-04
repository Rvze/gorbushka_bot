package com.tsypk.coreclient.application.supplier

import com.tsypk.coreclient.application.BotApplication
import com.tsypk.coreclient.exception.SubscriptionNotFoundException
import com.tsypk.coreclient.exception.SupplierNotFoundException
import com.tsypk.coreclient.exception.withErrorHandling
import com.tsypk.coreclient.model.ClientType
import com.tsypk.coreclient.model.Supplier
import com.tsypk.coreclient.model.subscription.Subscription
import com.tsypk.coreclient.model.telegram.MessageUpdate
import com.tsypk.coreclient.service.SubscriptionMgmtService
import com.tsypk.coreclient.service.SupplierMgmtService
import com.tsypk.coreclient.service.UserMgmtService
import com.tsypk.coreclient.service.stat.StatisticsService
import com.tsypk.coreclient.service.subscription.ChannelSubscriptionChecker
import com.tsypk.coreclient.util.telegram.Messages
import com.tsypk.coreclient.util.telegram.messageUpdateFromTelegramMessage
import dev.inmo.micro_utils.coroutines.subscribeSafelyWithoutExceptions
import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.api.send.sendMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onText
import dev.inmo.tgbotapi.types.ChatId
import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import dev.inmo.tgbotapi.types.message.content.MessageContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service

@Service
class SupplierBotApplication(
    private val supplierBot: TelegramBot,
    private val userMgmtService: UserMgmtService,
    private val subscriptionMgmtService: SubscriptionMgmtService,
    private val supplierMgmtService: SupplierMgmtService,
    private val channelSubscriptionChecker: ChannelSubscriptionChecker,
    private val supplierBotFlowService: SupplierBotFlowService,

    private val statisticsService: StatisticsService,
) : BotApplication {

    companion object {
        val commands = setOf("/start", "/help", "start", "help")
    }

    override suspend fun listenMessages() {
        supplierBot.buildBehaviourWithLongPolling {
            onCommand("start") { msg ->
                withErrorHandling(this, msg) {
                    withSubscriptionChecks(msg) { _, _, _ ->
                        supplierBot.sendMessage(
                            chat = msg.chat,
                            text = Messages.COMMON.startCommand,
                        )
                    }
                }
            }
            onCommand("help") { msg ->
                withErrorHandling(this, msg) {
                    withSubscriptionChecks(msg) { _, _, _ ->
                        supplierBot.sendMessage(
                            chat = msg.chat,
                            text = Messages.SUPPLIER.helpCommand,
                        )
                    }
                }
            }
            onText(initialFilter = { it.content.text.trim() !in commands }) { msg ->
                withErrorHandling(this, msg) {
                    withSubscriptionChecks(msg) { msgUpdate, supplier, subscription ->
                        supplierBotFlowService.onRenewPriceCommand(
                            ctx = this,
                            msgUpdate = msgUpdate,
                            supplier = supplier,
                            subscription = subscription,
                        )
                    }
                }
            }
            allUpdatesFlow.subscribeSafelyWithoutExceptions(this) { }
        }.join()
    }

    private suspend fun withSubscriptionChecks(
        msg: CommonMessage<MessageContent>,
        action: suspend (
            msgUpdate: MessageUpdate,
            supplier: Supplier,
            subscription: Subscription,
        ) -> Unit,
    ) {
        // STAT
        statisticsService.newMessage(msg.chat.id.chatId, ClientType.SUPPLIER)

        val msgUpdate = messageUpdateFromTelegramMessage(msg)

        // check if subscribed at channel
//        channelSubscriptionChecker.assertSubscribedAtChannel(
//            id = msgUpdate.senderId,
//            clientType = ClientType.SUPPLIER,
//        )

        // create or get tgBotUser from message
        val tgBotUser = withContext(Dispatchers.IO) {
            userMgmtService.createIfNotExistAndGet(msgUpdate.sender!!)
        }
        msgUpdate.sender = tgBotUser

        // check if supplier exist
        val supplier = getSupplier(tgBotUser.id)
        // check subscription
        val subscription = getSubscriptionIfActive(tgBotUser.id)

        // execute command if permitted
        if (subscription != null && supplier != null) {
            action(msgUpdate, supplier, subscription)
        }
    }

    private suspend fun getSubscriptionIfActive(id: Long): Subscription? {
        return try {
            val subscription = subscriptionMgmtService.findOne(
                tgBotUserId = id,
                clientType = ClientType.SUPPLIER,
            )
            subscriptionMgmtService.assertNotExpired(subscription)
            subscription
        } catch (e: SubscriptionNotFoundException) {
            println(e)
            supplierBot.sendMessage(
                chatId = ChatId(id),
                text = Messages.SUPPLIER.botIsNotPermitted,
            )
            null
        }
    }

    private suspend fun getSupplier(id: Long): Supplier? {
        return try {
            supplierMgmtService.findById(id)
        } catch (e: SupplierNotFoundException) {
            supplierBot.sendMessage(
                chatId = ChatId(id),
                text = Messages.SUPPLIER.supplierIsNotInDb,
            )
            null
        }
    }
}
