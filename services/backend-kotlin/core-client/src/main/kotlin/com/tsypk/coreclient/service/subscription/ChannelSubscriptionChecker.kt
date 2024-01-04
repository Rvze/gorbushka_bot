package com.tsypk.coreclient.service.subscription

import com.tsypk.coreclient.exception.NotChannelSubscriberException
import com.tsypk.coreclient.model.ClientType
import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.api.chat.members.getChatMember
import dev.inmo.tgbotapi.extensions.utils.ifBannedChatMember
import dev.inmo.tgbotapi.extensions.utils.ifKickedChatMember
import dev.inmo.tgbotapi.extensions.utils.ifLeftChatMember
import dev.inmo.tgbotapi.types.ChatId
import dev.inmo.tgbotapi.types.UserId
import dev.inmo.tgbotapi.types.chat.member.ChatMember
import org.springframework.stereotype.Service

@Service
//TODO remove this shit
class ChannelSubscriptionChecker(
    private val supplierBot: TelegramBot,
    private val resellerBot: TelegramBot,
) {
    private companion object {
        const val GORBUSHKA_CHAT_ID = -1001720385192L
    }

    suspend fun assertSubscribedAtChannel(id: Long, clientType: ClientType) {
        try {
            when (clientType) {
                ClientType.RESELLER -> {
                    val found = resellerBot.getChatMember(
                        chatId = ChatId(GORBUSHKA_CHAT_ID),
                        userId = UserId(id),
                    )
                    assertSubscribed(found)
                }

                ClientType.SUPPLIER -> {
                    val found = supplierBot.getChatMember(
                        chatId = ChatId(GORBUSHKA_CHAT_ID),
                        userId = UserId(id),
                    )
                    assertSubscribed(found)
                }

                else -> {}
            }
        } catch (e: Exception) {
            throw NotChannelSubscriberException()
        }
    }

    private fun assertSubscribed(found: ChatMember) {
        found.ifLeftChatMember {
            throw NotChannelSubscriberException()
        }
        found.ifBannedChatMember {
            throw NotChannelSubscriberException()
        }
        found.ifKickedChatMember {
            throw NotChannelSubscriberException()
        }
    }
}
