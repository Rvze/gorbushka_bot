package com.nmakarov.coreclient.model.telegram

enum class TelegramAccountType(val ru: String) {
    USER("Пользователь"),
    CHANNEL("Канал"),
    GROUP("Группа"),
    UNDEFINED("Не определён"),
}
