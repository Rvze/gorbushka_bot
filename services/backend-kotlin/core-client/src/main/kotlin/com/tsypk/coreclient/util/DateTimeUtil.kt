package com.tsypk.coreclient.util

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

fun LocalDate.formatRu(): String {
    return "${this.dayOfMonth.toDay()}.${this.month.value.toMonth()}.${this.year}"
}

fun localDateNowMoscow(): LocalDate {
    return LocalDate.ofInstant(Instant.now(), ZoneId.of("Europe/Moscow"))
}

fun Int.toDay(): String {
    if (this in 1..9) {
        return "0$this"
    }
    return this.toString()
}

fun Int.toMonth(): String {
    if (this in 1..9) {
        return "0$this"
    }
    return this.toString()
}
