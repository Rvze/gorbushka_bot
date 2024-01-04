package com.tsypk.coreclient.api

import java.io.File

interface MessagingClient {
    suspend fun sendText(chatId: Long, text: String)

    suspend fun sendFile(chatId: Long, file: File)

    suspend fun sendFiles(chatId: Long, files: List<File>)
}
