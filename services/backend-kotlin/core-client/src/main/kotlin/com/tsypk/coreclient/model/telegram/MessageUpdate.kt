package com.tsypk.coreclient.model.telegram

data class MessageUpdate(
    var senderId: Long,
    var messageText: String,
    var sender: TgBotUser? = null,
    var forwardFrom: TgBotUser? = null,
)
