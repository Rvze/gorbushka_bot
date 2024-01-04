package com.tsypk.coreclient.service.presenting.apple

import com.tsypk.coreclient.util.formatPriceWithDots
import recognitioncommons.models.apple.iphone.Iphone
import recognitioncommons.models.apple.iphone.IphoneMemory
import recognitioncommons.models.apple.iphone.IphoneModel
import recognitioncommons.models.country.CountryFlagEmojis
import recognitioncommons.util.idString

fun groupIphoneByCountryString(iphones: List<Iphone>, prefix: String = "|"): String {
    return iphones.groupBy { iphone -> iphone.country }.map { kv ->
        kv.value.joinToString(separator = "\n") {
            val flag = CountryFlagEmojis.countryToEmojiUnicode[it.country]!!
            "$prefix$flag ${formatPriceWithDots(price = it.price)}₽ — ${it.supplierUsername}"
        }
    }.joinToString("\n") { it }
}

fun groupIphoneByColorString(iphones: List<Iphone>, prefix: String = "|"): String{
    return iphones.groupBy { iphone -> iphone.country }.map { kv ->
        kv.value.joinToString(separator = "\n") {
            val flag = CountryFlagEmojis.countryToEmojiUnicode[it.country]!!
            "$prefix$flag ${formatPriceWithDots(price = it.price)}₽ — ${it.supplierUsername}"
        }
    }.joinToString("\n") { it }
}

fun iphonesSortedByPriceGroupedByCountry(iphones: List<Iphone>): List<Iphone> {
    return iphones.groupBy {
        it.country
    }.map { i -> i.value.sortedBy { it.price } }.sortedBy {
        it.first().price
    }.flatten()
}

fun iphonesGroupByConcreteModelAndCountryAndSortedByPrice(
    iphones: List<Iphone>,
    take: Int? = null,
): List<List<Iphone>> {
    return iphones.groupBy {
        it.idString()
    }.map {
        val byCountry = iphonesSortedByPriceGroupedByCountry(it.value)
        if (take != null) {
            byCountry.take(take)
        } else {
            byCountry
        }
    }.filter { it.isNotEmpty() }.sortedBy {
        it.first().price
    }
}

fun sortIphonesGroupByByModel(iphones: List<List<Iphone>>): List<List<Iphone>> {
    return iphones.sortedBy {
        iphoneModelCompareValue(it.first().model)
    }
}

fun iphoneModelMemoryCompareValue(iphone: Iphone): Int {
    return iphoneModelCompareValue(iphone.model) + iphoneMemoryCompareValue(iphone.memory)
}

fun iphoneModelCompareValue(iphoneModel: IphoneModel): Int {
    return when (iphoneModel) {
        IphoneModel.IPHONE_14_PRO_MAX -> 21000
        IphoneModel.IPHONE_14_PRO -> 20000
        IphoneModel.IPHONE_14_PLUS -> 19000
        IphoneModel.IPHONE_14 -> 18000
        IphoneModel.IPHONE_SE_3 -> 17000
        IphoneModel.IPHONE_13_PRO_MAX -> 16000
        IphoneModel.IPHONE_13_PRO -> 15000
        IphoneModel.IPHONE_13 -> 14000
        IphoneModel.IPHONE_13_MINI -> 13000
        IphoneModel.IPHONE_12_PRO_MAX -> 12000
        IphoneModel.IPHONE_12_PRO -> 11000
        IphoneModel.IPHONE_12 -> 10000
        IphoneModel.IPHONE_12_MINI -> 9000
        IphoneModel.IPHONE_SE_2 -> 800
        IphoneModel.IPHONE_11_PRO_MAX -> 7000
        IphoneModel.IPHONE_11_PRO -> 6000
        IphoneModel.IPHONE_11 -> 5000
    }
}

fun iphoneMemoryCompareValue(iphoneMemory: IphoneMemory): Int {
    return when (iphoneMemory) {
        IphoneMemory.GB_32 -> 100
        IphoneMemory.GB_64 -> 200
        IphoneMemory.GB_128 -> 300
        IphoneMemory.GB_256 -> 400
        IphoneMemory.GB_512 -> 500
        IphoneMemory.TB_1 -> 600
    }
}