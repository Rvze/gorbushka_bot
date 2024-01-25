package com.nmakarov.coreclient.application.reseller

import com.nmakarov.coreclient.exception.SubscriptionNotFoundException
import com.nmakarov.coreclient.exception.withErrorHandling
import com.nmakarov.coreclient.model.ClientType
import com.nmakarov.coreclient.model.subscription.Subscription
import com.nmakarov.coreclient.model.subscription.SubscriptionType
import com.nmakarov.coreclient.model.telegram.MessageUpdate
import com.nmakarov.coreclient.model.telegram.TgBotUser
import com.nmakarov.coreclient.service.SubscriptionMgmtService
import com.nmakarov.coreclient.service.UserMgmtService
import com.nmakarov.coreclient.service.stat.StatisticsService
import com.nmakarov.coreclient.service.subscription.SubscriptionFactory
import com.nmakarov.coreclient.util.telegram.Messages
import com.nmakarov.coreclient.util.telegram.messageUpdateFromTelegramMessage
import dev.inmo.micro_utils.coroutines.subscribeSafelyWithoutExceptions
import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.api.send.sendMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommandWithArgs
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onText
import dev.inmo.tgbotapi.types.ChatId
import dev.inmo.tgbotapi.types.message.HTMLParseMode
import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import dev.inmo.tgbotapi.types.message.content.MessageContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service

@Service
class ResellerBotApplication(
        private val resellerBot: TelegramBot,
        private val resellerBotFlowService: ResellerBotFlowService,
        private val subscriptionMgmtService: SubscriptionMgmtService,
        private val subscriptionFactory: SubscriptionFactory,
        private val userMgmtService: UserMgmtService,
        private val statisticsService: StatisticsService,
) : com.nmakarov.coreclient.application.BotApplication {

    companion object {
        val commands = setOf(
                "/start", "/help", "/support", "/buy", "/subscription_info", "/subscribe",
                "start", "help", "support", "buy", "subscription_info", "subscribe",
        )
    }

    override suspend fun listenMessages() {
        resellerBot.buildBehaviourWithLongPolling {
            onCommand("start") { msg ->
                withErrorHandling(this, msg) {
                    resellerBot.sendMessage(
                            chat = msg.chat,
                            text = Messages.RESELLER.startCommand,
                            parseMode = HTMLParseMode,
                    )
                }
            }
            onCommand("help") { msg ->
                withErrorHandling(this, msg) {
                    withSubscriptionChecks(msg) { _, _, _ ->
                        resellerBot.sendMessage(
                                chat = msg.chat,
                                text = Messages.RESELLER.helpCommand,
                        )

                    }
                }
            }
            onCommand("support") { msg ->
                withErrorHandling(this, msg) {
                    withSubscriptionChecks(msg) { _, _, _ ->
                        resellerBot.sendMessage(
                                chat = msg.chat,
                                text = Messages.COMMAND.support,
                        )
                    }
                }
            }
            onCommand("buy") { msg ->
                withErrorHandling(this, msg) {
                    resellerBot.sendMessage(
                            chat = msg.chat,
                            text = Messages.SUBSCRIPTION.subscriptionNotReadyYet,
                    )
                }
            }
            onCommand("subscription_info") { msg ->
                withErrorHandling(this, msg) {
                    resellerBotFlowService.onSubscriptionInfoCommand(this, msg)
                }
            }
            onCommandWithArgs(
                    command = "subscribe",
                    initialFilter = { it.content.text.trim() !in commands }) { msg, _ ->
                withErrorHandling(this, msg) {
                    resellerBotFlowService.onSubscribeOnItemCommand(this, msg)
                }
            }
            onCommand("notifications") { msg ->
                withErrorHandling(this, msg) {
                    resellerBotFlowService.onNotificationsCommand(ctx = this, msg)
                }
            }
            onText(initialFilter = { it.content.textSources[0].source !in commands }) { msg ->
                withErrorHandling(this, msg) {
                    withSubscriptionChecks(msg) { _, _, _ ->
                        resellerBotFlowService.onPotentialSearchQuery(
                                ctx = this,
                                msg = msg,
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
                    tgBotUser: TgBotUser,
                    subscription: Subscription,
            ) -> Unit,
    ) {
        // STAT
        statisticsService.newMessage(msg.chat.id.chatId, ClientType.RESELLER)

        val msgUpdate = messageUpdateFromTelegramMessage(msg)

        // check if subscribed at channel
//        channelSubscriptionChecker.assertSubscribedAtChannel(
//            id = msgUpdate.senderId,
//            clientType = ClientType.RESELLER,
//        )

        // create or get tgBotUser from message
        val tgBotUser = withContext(Dispatchers.IO) {
            userMgmtService.createIfNotExistAndGet(msgUpdate.sender!!)
        }
        msgUpdate.sender = tgBotUser

        // check subscription
        val subscription = processSubscription(tgBotUser)

        // execute command if permitted
        action(msgUpdate, tgBotUser, subscription)
    }

    private suspend fun processSubscription(tgBotUser: TgBotUser): Subscription {
        val subscription = try {
            subscriptionMgmtService.findOne(
                    tgBotUserId = tgBotUser.id,
                    clientType = ClientType.RESELLER,
            )
        } catch (e: SubscriptionNotFoundException) {
            val newSubscription = subscriptionFactory.betaTestResellerSubscription(
                    tgBotUserId = tgBotUser.id,
            ) ?: subscriptionFactory.subscriptionForDays(
                    tgBotUserId = tgBotUser.id,
                    clientType = ClientType.RESELLER,
                    type = SubscriptionType.RESELLER_DEMO,
            )

            subscriptionMgmtService.createOrUpdate(newSubscription)

            resellerBot.sendMessage(
                    chatId = ChatId(tgBotUser.id),
                    text = Messages.SUBSCRIPTION.subscriptionInfo(newSubscription),
            )

            newSubscription
        }
        subscriptionMgmtService.assertNotExpired(subscription)
        return subscription
    }
}
