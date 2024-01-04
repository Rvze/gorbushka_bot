package com.tsypk.coreclient.util

import com.tsypk.coreclient.exception.CancelException
import com.tsypk.coreclient.exception.InputValidationException


private object UTIL {
    object VALIDATION {
        const val USERNAME = "username пользователя должен начинаться с @ и иметь длину 4 <= username <= 32"
        const val CHAT_ID = "Id чата должно быть числом"
        const val YES_OR_NO = "Нужно ответить: y/n или да/нет или t/f"
    }

    val yesAnswers = setOf(
        "y", "yes", "t", "true", "д", "да"
    )

    val noAnswers = setOf(
        "n", "no", "f", "false", "н", "нет"
    )
}

fun assertUsername(username: String) {
    if (!username.startsWith("@") || username.length < 4 || username.length > 32) {
        throw InputValidationException(UTIL.VALIDATION.USERNAME)
    }
}

fun assertInSet(check: String, set: Set<String> = emptySet()) {
    if (check !in set) {
        throw InputValidationException(
            "Ввод должен находиться внутри набора: $set"
        )
    }
}

fun assertChatId(check: String) {
    check.toLongOrNull() ?: throw InputValidationException(UTIL.VALIDATION.CHAT_ID)
}

fun isYes(check: String): Boolean {
    return check.lowercase() in UTIL.yesAnswers
}

fun isNo(check: String): Boolean {
    return check.lowercase() in UTIL.noAnswers
}

fun assertYesOrNo(check: String) {
    if (!isYes(check) && !isNo(check)) {
        throw InputValidationException(UTIL.VALIDATION.YES_OR_NO)
    }
}

fun checkCancel(text: String) {
    if (text.equals(other = "cancel", ignoreCase = true)) {
        throw CancelException()
    }
}
