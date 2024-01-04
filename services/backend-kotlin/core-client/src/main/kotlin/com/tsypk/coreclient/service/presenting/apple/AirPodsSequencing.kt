package com.tsypk.coreclient.service.presenting.apple

import com.tsypk.coreclient.util.formatPriceWithDots
import recognitioncommons.models.apple.airpods.AirPods
import recognitioncommons.models.apple.airpods.AirPodsModel
import recognitioncommons.models.country.CountryFlagEmojis
import recognitioncommons.util.idString

fun groupAirPodsByCountryString(airPods: List<AirPods>, prefix: String = "|"): String {
    return airPods.groupBy { airPod -> airPod.country }.map { kv ->
        kv.value.joinToString(separator = "\n") {
            val flag = CountryFlagEmojis.countryToEmojiUnicode[it.country]!!
            "$prefix$flag ${formatPriceWithDots(price = it.price)}₽ — ${it.supplierUsername}"
        }
    }.joinToString("\n\n") { it }
}

fun airPodsSortedByPriceGroupedByCountry(airPods: List<AirPods>): List<AirPods> {
    return airPods.groupBy {
        it.country
    }.map { i -> i.value.sortedBy { it.price } }.sortedBy {
        it.first().price
    }.flatten()
}

fun airPodsGroupByConcreteModelAndCountryAndSortedByPrice(
    airPods: List<AirPods>,
    take: Int? = null,
): List<List<AirPods>> {
    return airPods.groupBy {
        it.idString()
    }.map {
        val byCountry = airPodsSortedByPriceGroupedByCountry(it.value)
        if (take != null) {
            byCountry.take(take)
        } else {
            byCountry
        }
    }.filter { it.isNotEmpty() }.sortedBy {
        it.first().price
    }
}

fun sortAirPodsGroupByByModel(airPods: List<List<AirPods>>): List<List<AirPods>> {
    return airPods.sortedBy {
        airPodsModelCompareValue(it.first().model)
    }
}

fun airPodsModelCompareValue(airPodsModel: AirPodsModel): Int {
    return when (airPodsModel) {
        AirPodsModel.AIRPODS_MAX -> 100_000
        AirPodsModel.AIRPODS_PRO_2 -> 90_000
        AirPodsModel.AIRPODS_3_MAGSAFE -> 80_000
        AirPodsModel.AIRPODS_3_LIGHTNING -> 70_000
        AirPodsModel.AIRPODS_PRO_1 -> 60_000
        AirPodsModel.AIRPODS_2 -> 50_000
    }
}