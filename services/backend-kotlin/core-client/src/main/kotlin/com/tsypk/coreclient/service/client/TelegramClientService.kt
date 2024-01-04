package com.tsypk.coreclient.service.client

import com.tsypk.coreclient.api.MessagingClient
import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.api.send.media.sendDocument
import dev.inmo.tgbotapi.extensions.api.send.media.sendDocumentsGroup
import dev.inmo.tgbotapi.extensions.api.send.send
import dev.inmo.tgbotapi.requests.abstracts.asMultipartFile
import dev.inmo.tgbotapi.types.ChatId
import dev.inmo.tgbotapi.types.media.TelegramMediaDocument
import dev.inmo.tgbotapi.types.message.HTMLParseMode
import org.springframework.stereotype.Service
import java.io.File

@Service
class TelegramClientService(
    private val resellerBot: TelegramBot,
) : MessagingClient {

    override suspend fun sendText(chatId: Long, text: String) {
        resellerBot.send(
            chatId = ChatId(chatId),
            text = text,
            parseMode = HTMLParseMode,
        )
    }

    override suspend fun sendFile(chatId: Long, file: File) {
        resellerBot.sendDocument(
            chatId = ChatId(chatId),
            document = file.asMultipartFile(),
            protectContent = true,
        )
    }

    override suspend fun sendFiles(chatId: Long, files: List<File>) {
        when (files.size) {
            1 -> this.sendFile(chatId = chatId, file = files.first())

            else -> resellerBot.sendDocumentsGroup(
                chatId = ChatId(chatId),
                media = files.map { TelegramMediaDocument(it.asMultipartFile()) },
                protectContent = true
            )
        }
    }
}
