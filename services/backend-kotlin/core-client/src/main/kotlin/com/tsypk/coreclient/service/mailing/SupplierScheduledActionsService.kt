package com.tsypk.coreclient.service.mailing

import com.tsypk.coreclient.model.ClientType
import com.tsypk.coreclient.repository.SubscriptionRepository
import com.tsypk.coreclient.service.client.SupplierAirPodsClientService
import com.tsypk.coreclient.service.client.SupplierClientService
import com.tsypk.coreclient.service.client.SupplierIphonesClientService
import com.tsypk.coreclient.util.telegram.Messages
import com.tsypk.coreclient.util.telegram.TelegramInfo
import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.api.send.sendMessage
import dev.inmo.tgbotapi.types.ChatId
import kotlinx.coroutines.runBlocking
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class SupplierScheduledActionsService(
    private val supplierIphonesClientService: SupplierIphonesClientService,
    private val supplierAirPodsClientService: SupplierAirPodsClientService,
    private val supplierClientService: SupplierClientService,

    private val subscriptionRepository: SubscriptionRepository,
    private val supplierBot: TelegramBot,
    private val adminBot: TelegramBot,
) {
    @Retryable(value = [Exception::class], maxAttempts = 3, backoff = Backoff(delay = 2000L))
    @Scheduled(cron = "0 0 8 * * * ", zone = "Europe/Moscow")
    fun refreshStaff() {
        TelegramInfo.ADMIN_IDS.forEach {
            runBlocking {
                adminBot.sendMessage(
                    chatId = ChatId(it),
                    text = Messages.ADMIN.plannedTruncation,
                )
            }
        }

        val suppliers = try {
            val suppliers = subscriptionRepository.getAllForClientType(ClientType.SUPPLIER)
            supplierIphonesClientService.truncateSuppliersIphones()
            supplierAirPodsClientService.truncateSuppliersAirPods()
            suppliers
        } catch (e: Exception) {
            TelegramInfo.ADMIN_IDS.forEach {
                runBlocking {
                    adminBot.sendMessage(
                        chatId = ChatId(it),
                        text = Messages.COMMON.composeFullExceptionInfo(e),
                    )
                }
            }
            throw e
        }

        TelegramInfo.ADMIN_IDS.forEach {
            runBlocking {
                adminBot.sendMessage(
                    chatId = ChatId(it),
                    text = Messages.ADMIN.successTruncation,
                )
            }
        }


        suppliers.forEach {
            try {
                runBlocking {
                    supplierBot.sendMessage(
                        chatId = ChatId(it.tgBotUserId),
                        text = Messages.SUPPLIER.supplierStashDeleted,
                    )
                }
            } catch (_: Exception) {
            }
        }
    }

    @Retryable(value = [Exception::class], maxAttempts = 3, backoff = Backoff(delay = 2000L))
    @Scheduled(cron = "0 0 10 * * * ", zone = "Europe/Moscow")
    fun firstSupplierReminder() {
        remindSuppliers(1)
    }

    @Retryable(value = [Exception::class], maxAttempts = 3, backoff = Backoff(delay = 2000L))
    @Scheduled(cron = "0 0 12 * * * ", zone = "Europe/Moscow")
    fun secondSupplierReminder() {
        remindSuppliers(2)
    }

    private fun remindSuppliers(order: Int) {
        val adminToSend = when (order) {
            1 -> Messages.ADMIN.plannedFirstSupplierReminder
            2 -> Messages.ADMIN.plannedSecondSupplierReminder
            else -> Messages.ADMIN.plannedFirstSupplierReminder
        }
        TelegramInfo.ADMIN_IDS.forEach {
            runBlocking {
                adminBot.sendMessage(
                    chatId = ChatId(it),
                    text = adminToSend,
                )
            }
        }

        val suppliers = subscriptionRepository.getAllForClientType(ClientType.SUPPLIER)
        val suppliersStaffInfo = supplierClientService.getSupplierStaffInfo()

        val suppliersWithoutStaffUpdate = suppliers.map { it.tgBotUserId }.toSet() -
            suppliersStaffInfo.map { it.supplierId }.toSet()

        val supplierToSend = when (order) {
            1 -> Messages.SUPPLIER.supplierFirstReminder
            2 -> Messages.SUPPLIER.supplierSecondReminder
            else -> Messages.SUPPLIER.supplierFirstReminder
        }
        suppliersWithoutStaffUpdate.forEach {
            try {
                runBlocking {
                    supplierBot.sendMessage(
                        chatId = ChatId(it),
                        text = supplierToSend,
                    )
                }
            } catch (_: Exception) {
            }
        }
    }
}
