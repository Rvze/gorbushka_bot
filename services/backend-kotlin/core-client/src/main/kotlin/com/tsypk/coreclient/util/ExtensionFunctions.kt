package com.tsypk.coreclient.util

import com.tsypk.coreclient.api.feign.dto.airpods.AirPodsBotUserUpdateRequestAirpodsDto
import com.tsypk.coreclient.api.feign.dto.iphone.IphonesBotUserUpdateRequestIphoneDto
import com.tsypk.coreclient.api.feign.dto.macbook.MacbookBotUserUpdateRequestMacbookDto
import recognitioncommons.models.Money
import recognitioncommons.models.apple.airpods.AirPods
import recognitioncommons.models.apple.iphone.Iphone
import recognitioncommons.models.apple.macbook.Macbook
import recognitioncommons.util.idString
import java.lang.Integer.min

/**
 * Common
 */
fun String?.strOrBlank(): String {
    if (this == null) {
        return ""
    }
    return this
}

fun List<String>.toSingleString(delimiter: String = "\n"): String {
    val builder = StringBuilder()
    this.forEach {
        builder.append(it)
        builder.append(delimiter)
    }
    return builder.toString()
}

/**
 * AirPods
 */
fun AirPods.toAirPodsBotUserUpdateRequestAirPodsDto(): AirPodsBotUserUpdateRequestAirpodsDto {
    return AirPodsBotUserUpdateRequestAirpodsDto(
        id = this.idString(),
        country = this.country,
        money = Money(
            amount = this.price.toBigDecimal(),
            currency = "RUB",
        )
    )
}

/**
 * Iphone
 */
fun Iphone.toIphonesBotUserUpdateRequestIphoneDto(): IphonesBotUserUpdateRequestIphoneDto {
    return IphonesBotUserUpdateRequestIphoneDto(
        id = this.idString(),
        country = this.country,
        money = Money(
            amount = this.price.toBigDecimal().setScale(4),
            currency = "RUB",
        ),
    )
}

/**
 * Macbook
 */
fun Macbook.toMacbooksBotUserUpdateRequestDto(): MacbookBotUserUpdateRequestMacbookDto {
    return MacbookBotUserUpdateRequestMacbookDto(
        id = this.idString(),
        country = this.country,
        money = Money(
            amount = this.price.toBigDecimal().setScale(4),
            currency = "RUB"
        )
    )
}

fun <T> List<T>.decompose(factor: Int = 3): List<List<T>> {
    if (this.size < factor) {
        throw RuntimeException("Cant decompose list with higher factor than it size")
    }

    val current = this
    return buildList {
        for (i in current.indices step factor) {
            add(
                current.subList(i, min((i + factor), current.size))
            )
        }
    }
}