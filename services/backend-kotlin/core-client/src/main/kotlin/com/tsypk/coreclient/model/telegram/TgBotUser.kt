package com.tsypk.coreclient.model.telegram

import com.tsypk.coreclient.util.strOrBlank
import java.time.Instant

data class TgBotUser(
    val id: Long,
    val accountType: TelegramAccountType,
    var username: String? = null,
    val firstname: String? = null,
    val lastname: String? = null,
    val title: String? = firstname.strOrBlank() + lastname.strOrBlank(),
) {
    var createdAt: Instant = Instant.now()

    fun usernameOrAnyTitle(): String {
        return username ?: title ?: id.toString()
    }
}
