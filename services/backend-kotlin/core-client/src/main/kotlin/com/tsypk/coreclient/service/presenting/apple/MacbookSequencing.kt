package com.tsypk.coreclient.service.presenting.apple

import com.tsypk.coreclient.util.formatPriceWithDots
import recognitioncommons.models.apple.macbook.Macbook
import recognitioncommons.models.country.CountryFlagEmojis
import recognitioncommons.util.idString

fun macbooksSortedByPriceGroupedByCountry(macbooks: List<Macbook>): List<Macbook> {
    return macbooks.groupBy {
        it.country
    }.map { i -> i.value.sortedBy { it.price } }.sortedBy {
        it.first().price
    }.flatten()
}

fun macbooksGroupByConcreteModelAndCountryAndSortedByPrice(
    macbooks: List<Macbook>,
    take: Int? = null,
): List<List<Macbook>> {
    return macbooks.groupBy {
        it.idString()
    }.map {
        val byCountry = macbooksSortedByPriceGroupedByCountry(it.value)
        if (take != null) {
            byCountry.take(take)
        } else {
            byCountry
        }
    }.filter { it.isNotEmpty() }.sortedBy {
        it.first().price
    }
}

fun groupMacbooksByCountryString(macbooks: List<Macbook>, prefix: String = "|"): String {
    return macbooks.groupBy { macbook -> macbook.country }.map { kv ->
        kv.value.joinToString(separator = "\n") {
            val flag = CountryFlagEmojis.countryToEmojiUnicode[it.country]!!
            "$prefix$flag ${formatPriceWithDots(price = it.price)}₽ — ${it.supplierUsername}"
        }
    }.joinToString("\n\n") { it }
}