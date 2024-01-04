package com.tsypk.coreclient.application.admin

import com.tsypk.coreclient.application.BotApplication
import com.tsypk.coreclient.exception.InputValidationException
import com.tsypk.coreclient.exception.withErrorHandling
import com.tsypk.coreclient.model.stat.Stat
import com.tsypk.coreclient.service.UserMgmtService
import com.tsypk.coreclient.service.stat.StatisticsService
import com.tsypk.coreclient.util.localDateNowMoscow
import com.tsypk.coreclient.util.telegram.Messages
import com.tsypk.coreclient.util.telegram.TelegramInfo
import dev.inmo.micro_utils.coroutines.subscribeSafelyWithoutExceptions
import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.api.send.sendMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommandWithArgs
import dev.inmo.tgbotapi.types.ChatId
import dev.inmo.tgbotapi.types.message.HTMLParseMode
import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import dev.inmo.tgbotapi.types.message.content.MessageContent
import kotlinx.coroutines.runBlocking
import org.springframework.core.env.Environment
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class AdminBotApplication(
    private val adminBot: TelegramBot,
    private val adminBotFlowService: AdminBotFlowService,
    private val statisticsService: StatisticsService,
    private val userMgmtService: UserMgmtService,
    private val env: Environment,
) : BotApplication {

    override suspend fun listenMessages() {
        if (env.getProperty("feature-flags.notify-admins.enabled")!!.toBoolean()) {
            notifyAdmins(Messages.ADMIN.onReload)
        }

        adminBot.buildBehaviourWithLongPolling {
            onCommand("start") { msg ->
                withAdminCheck(msg) {
                    withErrorHandling(this, msg) {
                        adminBot.sendMessage(
                            chat = msg.chat,
                            text = Messages.COMMON.startCommand,
                        )
                    }
                }
            }
            onCommand("help") { msg ->
                withAdminCheck(msg) {
                    withErrorHandling(this, msg) {
                        adminBot.sendMessage(
                            chat = msg.chat,
                            text = Messages.ADMIN.helpCommand,
                        )
                    }
                }
            }
            onCommand("stat") { msg ->
                withAdminCheck(msg) {
                    withErrorHandling(this, msg) {
                        adminBotFlowService.onStatCommand(this, msg)
                    }
                }
            }
            onCommand("supplier_stat") { msg ->
                withAdminCheck(msg) {
                    withErrorHandling(this, msg) {
                        adminBotFlowService.onSupplierStatCommand(this, msg)
                    }
                }
            }
            onCommand("suppliers") { msg ->
                withAdminCheck(msg) {
                    withErrorHandling(this, msg) {
                        adminBotFlowService.onSuppliersCommand(this, msg)
                    }
                }
            }
            onCommand("add_subscription") { msg ->
                withAdminCheck(msg) {
                    withErrorHandling(this, msg) {
                        adminBotFlowService.onAddSubscriptionCommand(this, msg)
                    }
                }
            }
            onCommand("add_supplier") { msg ->
                withAdminCheck(msg) {
                    withErrorHandling(this, msg) {
                        adminBotFlowService.onAddSupplierCommand(this, msg)
                    }
                }
            }
            onCommand("delete_supplier") { msg ->
                withAdminCheck(msg) {
                    withErrorHandling(this, msg) {
                        adminBotFlowService.onDeleteSupplierCommand(this, msg)
                    }
                }
            }
            onCommandWithArgs("add") { msg, args ->
                withAdminCheck(msg) {
                    withErrorHandling(this, msg) {
                        val supplier = extractSupplierUsernameArg(args)
                        adminBotFlowService.onAddStuffCommand(this, msg, supplier)
                    }
                }
            }
            onCommandWithArgs("send_resellers") { msg, args ->
                withAdminCheck(msg) {
                    withErrorHandling(this, msg) {
                        val ids = extractIds(args)
                        adminBotFlowService.onSendResellersCommand(this, msg, ids)
                    }
                }
            }
            onCommandWithArgs("send_suppliers") { msg, args ->
                withAdminCheck(msg) {
                    withErrorHandling(this, msg) {
                        val ids = extractIds(args)
                        adminBotFlowService.onSendSuppliersCommand(this, msg, ids)
                    }
                }
            }
            allUpdatesFlow.subscribeSafelyWithoutExceptions(this) { }
        }.join()
    }

    @Scheduled(cron = "0 55 23 * * * ", zone = "Europe/Moscow")
    fun notifyAdmins() {
        val stat = Stat(statisticsService.r, localDateNowMoscow())
        runBlocking {
            notifyAdmins(Messages.STAT.composeStatMessage(stat))

            val suppliers = userMgmtService.enrichUsernames(stat.statRegistry.supplierStat.suppliersAddingIphones)
            notifyAdmins(Messages.STAT.composeSuppliersStat(suppliers.values))

            val messageWriters = userMgmtService.enrichUsernames(stat.statRegistry.commonStat.idToMessages.keys)
            notifyAdmins(
                Messages.STAT.composeTotalMessagesStat(
                    idsToUsername = messageWriters,
                    idsToMessages = stat.statRegistry.commonStat.idToMessages
                )
            )
        }
    }

    suspend fun notifyAdmins(text: String) {
        TelegramInfo.ADMIN_IDS.forEach {
            adminBot.sendMessage(
                chatId = ChatId(it),
                text = text,
                parseMode = HTMLParseMode,
            )
        }
    }

    private suspend fun withAdminCheck(msg: CommonMessage<MessageContent>, action: suspend () -> Unit) {
        if (msg.chat.id.chatId !in TelegramInfo.ADMIN_IDS) {
            return
        }
        action()
    }

    private suspend fun extractSupplierUsernameArg(args: Array<String>): String {
        if (args.size != 1 || !args.first().startsWith("@")) {
            throw InputValidationException(
                "Должен был один аргумент после команды, должен начинаться с @"
            )
        }
        return args.first().trim()
    }

    private suspend fun extractIds(args: Array<String>): List<Long> {
        if (args.isEmpty()) {
            throw InputValidationException("Введите ID пользователей через пробел")
        }

        return args.map { it.toLong() }.toSet().toList()
    }
}
