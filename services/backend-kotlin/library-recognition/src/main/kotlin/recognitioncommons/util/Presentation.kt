package recognitioncommons.util

import recognitioncommons.models.apple.airpods.AirPodsModel
import recognitioncommons.models.apple.iphone.IphoneModel
import recognitioncommons.models.apple.macbook.MacbookModel
import recognitioncommons.models.country.Country
import recognitioncommons.models.country.CountryFlagEmojis

object Presentation {
    /**
     * Common
     */
    fun Country?.toHumanReadableString(): String {
        if (this == null) {
            return "?"
        }
        return "${this.nameRu} ${CountryFlagEmojis.countryToEmojiUnicode[this]!!}"
    }

    fun formatEnumName(text: String): String {
        return text.lowercase().split("_").map {
            it.replaceFirstChar { chr -> chr.uppercase() }
        }.joinToString(separator = " ") { it }
    }

    /**
     * Iphone
     */
    object IphonePresentation {
        private val iphoneModelToHumanReadableString = mapOf(
            null to "?",
            IphoneModel.IPHONE_14_PRO_MAX to "iPhone 14 Pro Max",
            IphoneModel.IPHONE_14_PRO to "iPhone 14 Pro",
            IphoneModel.IPHONE_14_PLUS to "iPhone 14 Plus",
            IphoneModel.IPHONE_14 to "iPhone 14",
            IphoneModel.IPHONE_SE_3 to "iPhone SE 3",
            IphoneModel.IPHONE_13_PRO_MAX to "iPhone 13 Pro Max",
            IphoneModel.IPHONE_13_PRO to "iPhone 13 Pro",
            IphoneModel.IPHONE_13 to "iPhone 13",
            IphoneModel.IPHONE_13_MINI to "iPhone 13 Mini",
            IphoneModel.IPHONE_12_PRO_MAX to "iPhone 12 Pro Max",
            IphoneModel.IPHONE_12_PRO to "iPhone 12 Pro",
            IphoneModel.IPHONE_12 to "iPhone 12",
            IphoneModel.IPHONE_12_MINI to "iPhone 12 Mini",
            IphoneModel.IPHONE_SE_2 to "iPhone SE 2",
            IphoneModel.IPHONE_11_PRO_MAX to "iPhone 11 Pro Max",
            IphoneModel.IPHONE_11_PRO to "iPhone 11 Pro",
            IphoneModel.IPHONE_11 to "iPhone 11",
        )

        fun IphoneModel?.toHumanReadableString(): String {
            return iphoneModelToHumanReadableString[this]!!
        }
    }

    /**
     * MacBook
     */
    object MacbookPresentation {
        private val macbookModelToHumanReadableString = mapOf(
            null to "?",
            MacbookModel.MACBOOK_AIR_13_M1 to "MacBook Air 13 M1",
            MacbookModel.MACBOOK_PRO_13_M1 to "MacBook Pro 13 M1",
            MacbookModel.MACBOOK_PRO_14_M1_PRO to "MacBook PRO 14 M1 Pro",
            MacbookModel.MACBOOK_PRO_14_M1_MAX to "MacBook PRO 14 M1 Max",
            MacbookModel.MACBOOK_PRO_16_M1_PRO to "MacBook PRO 16 M1 Pro",
            MacbookModel.MACBOOK_PRO_16_M1_MAX to "MacBook PRO 16 M1 Max",
            MacbookModel.MACBOOK_AIR_13_M2 to "MacBook Air 13 M2",
            MacbookModel.MACBOOK_PRO_13_M2 to "MacBook Pro 13 M2",
            MacbookModel.MACBOOK_PRO_14_M2_PRO to "MacBook Pro 14 M2 Pro",
            MacbookModel.MACBOOK_PRO_14_M2_MAX to "MacBook Pro 14 M2 Max",
            MacbookModel.MACBOOK_PRO_16_M2_PRO to "MacBook Pro 16 M2 Pro",
            MacbookModel.MACBOOK_PRO_16_M2_MAX to "MacBook Pro 16 M2 Max",
        )

        fun MacbookModel?.toHumanReadableString(): String {
            return macbookModelToHumanReadableString[this]!!
        }
    }

    /**
     * AirPods
     */

    object AirPodsPresentation {
        private val airPodsModelToHumanReadableString = mapOf(
            null to "?",
            AirPodsModel.AIRPODS_MAX to "AirPods Max", // 2020
            AirPodsModel.AIRPODS_PRO_2 to "AirPods Pro 2", // 2022
            AirPodsModel.AIRPODS_PRO_1 to "AirPods Pro 1", // 2019
            AirPodsModel.AIRPODS_3_LIGHTNING to "AirPods 3 Lightning", // 2022
            AirPodsModel.AIRPODS_3_MAGSAFE to "AirPods 3 MagSafe", // 2022
            AirPodsModel.AIRPODS_2 to "AirPods 2", // 2019
        )

        fun AirPodsModel?.toHumanReadableString(): String {
            return airPodsModelToHumanReadableString[this]!!
        }
    }
}
