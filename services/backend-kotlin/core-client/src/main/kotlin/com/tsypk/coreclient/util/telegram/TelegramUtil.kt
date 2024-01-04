package com.tsypk.coreclient.util.telegram

import com.tsypk.coreclient.model.telegram.MessageUpdate
import com.tsypk.coreclient.model.telegram.TelegramAccountType
import com.tsypk.coreclient.model.telegram.TgBotUser
import dev.inmo.tgbotapi.extensions.utils.*
import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import dev.inmo.tgbotapi.types.message.content.MessageContent

fun messageUpdateFromTelegramMessage(message: CommonMessage<MessageContent>): MessageUpdate {
    val msgText = message.content.textContentOrNull()!!.text
    val messageUpdate = MessageUpdate(
        senderId = message.chat.id.chatId,
        messageText = msgText,
    )
    messageUpdate.enrichSenderInfo(message)
    messageUpdate.enrichForwardInfo(message)
    return messageUpdate
}

private fun MessageUpdate.enrichSenderInfo(message: CommonMessage<MessageContent>) {
    message.ifFromUser {
        this.sender = TgBotUser(
            id = it.from.id.chatId,
            accountType = TelegramAccountType.USER,
            username = it.from.username?.username,
            firstname = it.from.firstName,
            lastname = it.from.lastName
        )
        return
    }

    message.ifFromChannelGroupContentMessage {
        this.sender = TgBotUser(
            id = it.channel.id.chatId,
            accountType = TelegramAccountType.CHANNEL,
            username = it.channel.username?.username,
            title = it.channel.title,
        )
        return
    }

    if (this.sender == null) {
        this.sender = TgBotUser(
            id = message.chat.id.chatId,
            accountType = TelegramAccountType.UNDEFINED,
        )
    }
}

private fun MessageUpdate.enrichForwardInfo(message: CommonMessage<MessageContent>) {
    message.forwardInfo?.ifFromUser {
        this.forwardFrom = TgBotUser(
            id = it.from.id.chatId,
            accountType = TelegramAccountType.USER,
            username = it.from.username?.username,
            firstname = it.from.firstName,
            lastname = it.from.lastName,
        )
        return
    }

    message.forwardInfo?.ifFromSupergroup {
        this.forwardFrom = TgBotUser(
            id = it.group.id.chatId,
            accountType = TelegramAccountType.GROUP,
            username = it.group.username?.username,
            title = it.group.title,
        )
        return
    }

    message.forwardInfo?.ifFromChannel {
        this.forwardFrom = TgBotUser(
            id = it.channelChat.id.chatId,
            accountType = TelegramAccountType.CHANNEL,
            username = it.channelChat.username?.username,
            title = it.channelChat.title,
        )
        return
    }
}
