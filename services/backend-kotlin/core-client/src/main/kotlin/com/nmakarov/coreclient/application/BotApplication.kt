package com.nmakarov.coreclient.application

interface BotApplication {
    suspend fun listenMessages()
}
