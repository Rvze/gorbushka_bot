package com.tsypk.coreclient.util

import com.tsypk.coreclient.service.presenting.apple.iphonesGroupByConcreteModelAndCountryAndSortedByPrice
import com.tsypk.coreclient.util.telegram.Messages
import org.junit.jupiter.api.Test
import recognitioncommons.models.country.Country
import recognitioncommons.models.apple.AppleColor
import recognitioncommons.models.apple.iphone.Iphone
import recognitioncommons.models.apple.iphone.IphoneMemory
import recognitioncommons.models.apple.iphone.IphoneModel
import recognitioncommons.util.idString

internal class MessagesTest {
    @Test
    fun test() {
        val iphones = listOf(
            Iphone(
                model = IphoneModel.IPHONE_14_PRO_MAX,
                memory = IphoneMemory.TB_1,
                color = AppleColor.SPACE_BLACK,
                country = Country.USA,
                price = 123500,
            ),
            Iphone(
                model = IphoneModel.IPHONE_14_PRO_MAX,
                memory = IphoneMemory.TB_1,
                color = AppleColor.SILVER,
                country = Country.RUSSIA,
                price = 120000,
            ),

            Iphone(
                model = IphoneModel.IPHONE_14_PRO_MAX,
                memory = IphoneMemory.GB_512,
                color = AppleColor.BLACK,
                country = Country.HONG_KONG,
                price = 30000,
            ),
            Iphone(
                model = IphoneModel.IPHONE_14_PRO_MAX,
                memory = IphoneMemory.GB_512,
                color = AppleColor.BLUE,
                country = Country.SOUTH_KOREA,
                price = 40000,
            ),

            Iphone(
                model = IphoneModel.IPHONE_14_PRO_MAX,
                memory = IphoneMemory.GB_256,
                color = AppleColor.PINK,
                country = Country.USA,
                price = 123500,
            ),
            Iphone(
                model = IphoneModel.IPHONE_14_PRO_MAX,
                memory = IphoneMemory.GB_256,
                color = AppleColor.RED,
                country = Country.USA,
                price = 123500,
            ),
        )
        println(iphones.groupBy { it.idString() })

        val sortedIphones = iphonesGroupByConcreteModelAndCountryAndSortedByPrice(iphones)
        sortedIphones.forEach {
            println(Messages.IPHONE.iphoneBestPricesForSearchModel(it))
        }
    }
}