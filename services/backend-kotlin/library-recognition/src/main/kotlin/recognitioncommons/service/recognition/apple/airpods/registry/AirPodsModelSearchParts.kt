package recognitioncommons.service.recognition.apple.airpods.registry

import recognitioncommons.models.Locale
import recognitioncommons.models.apple.airpods.AirPodsModel
import recognitioncommons.service.search.AllMatchSearchPart
import recognitioncommons.service.search.AnyMatchSearchPart
import recognitioncommons.service.search.SearchPart
import recognitioncommons.service.search.SimpleSearchPart

object AirPodsModelSearchParts {

    val AIRPODS_MODEL_SEARCH_PART = listOf<Pair<AirPodsModel, SearchPart>>(
        AirPodsModel.AIRPODS_MAX to AirPods.AIRPODS_MAX_SP,
        AirPodsModel.AIRPODS_PRO_2 to AirPods.AIRPODS_PRO_2_SP,
        AirPodsModel.AIRPODS_PRO_1 to AirPods.AIRPODS_PRO_1_SP,
        AirPodsModel.AIRPODS_3_MAGSAFE to AirPods.AIRPODS_3_MAGSAFE_SP,
        AirPodsModel.AIRPODS_3_LIGHTNING to AirPods.AIRPODS_3_LIGHTNING_SP,
        AirPodsModel.AIRPODS_2 to AirPods.AIRPODS_2_SP,
    )

    val AIRPODS_WORD_SEARCH_PART = Components.AIRPODS

    private object AirPods {
        val AIRPODS_MAX_SP = AllMatchSearchPart(
            setOf(Components.AIRPODS, Components.MAX)
        )
        val AIRPODS_PRO_2_SP = AllMatchSearchPart(
            setOf(Components.AIRPODS, Components.PRO, Components.TWO)
        )
        val AIRPODS_PRO_1_SP = AllMatchSearchPart(
            setOf(Components.AIRPODS, Components.PRO)
        )
        val AIRPODS_3_MAGSAFE_SP = AllMatchSearchPart(
            setOf(Components.AIRPODS, Components.THREE, Components.MAGSAFE)
        )
        val AIRPODS_3_LIGHTNING_SP = AllMatchSearchPart(
            setOf(Components.AIRPODS, Components.THREE)
        )
        val AIRPODS_2_SP = AllMatchSearchPart(
            setOf(Components.AIRPODS, Components.TWO)
        )
    }

    private object Components {
        val airpodsEn = AnyMatchSearchPart(
            setOf(
                SimpleSearchPart("AirPods", Locale.EN),
                SimpleSearchPart("Pods", Locale.EN),
                SimpleSearchPart("Air Pods", Locale.EN),
            )
        )
        val airPodsRu = AnyMatchSearchPart(
            setOf(
                SimpleSearchPart("Аирподс", Locale.RU),
                SimpleSearchPart("Аир подс", Locale.RU),
                SimpleSearchPart("Аирподсы", Locale.RU),
                SimpleSearchPart("Аир подсы", Locale.RU),

                SimpleSearchPart("Эирподсы", Locale.RU),
                SimpleSearchPart("Эир подсы", Locale.RU),
                SimpleSearchPart("Эирподс", Locale.RU),
                SimpleSearchPart("Эир подс", Locale.RU),
                SimpleSearchPart("Подсы", Locale.RU),
            )
        )
        val AIRPODS = AnyMatchSearchPart(
            setOf(airpodsEn, airPodsRu)
        )

        val proEn = SimpleSearchPart("Pro", Locale.EN)
        val proRu = SimpleSearchPart("Про", Locale.RU)
        val PRO = AnyMatchSearchPart(
            setOf(proEn, proRu)
        )

        val maxEn = SimpleSearchPart("Max", Locale.EN)
        val maxRu = AnyMatchSearchPart(
            setOf(
                SimpleSearchPart("Макс", Locale.RU),
                SimpleSearchPart("Мах", Locale.RU),
            )
        )
        val MAX = AnyMatchSearchPart(
            setOf(maxEn, maxRu)
        )

        val twoRu = SimpleSearchPart("Два", Locale.RU)
        val twoEn = SimpleSearchPart("Two", Locale.EN)
        val TWO = AnyMatchSearchPart(
            setOf(SimpleSearchPart("2"), twoRu, twoEn)
        )

        val threeRu = SimpleSearchPart("Три", Locale.RU)
        val threeEn = SimpleSearchPart("Three", Locale.EN)
        val THREE = AnyMatchSearchPart(
            setOf(SimpleSearchPart("3"), threeRu, threeEn)
        )

        val magSafeEng = AnyMatchSearchPart(setOf(SimpleSearchPart("MagSafe", Locale.EN)))
        val magSafeRu = AnyMatchSearchPart(
            setOf(
                SimpleSearchPart("Максейф", Locale.RU),
                SimpleSearchPart("Максэйф", Locale.RU),
                SimpleSearchPart("Магсейф", Locale.RU),
                SimpleSearchPart("Магсэйф", Locale.RU),
            )
        )
        val MAGSAFE = AnyMatchSearchPart(
            setOf(magSafeEng, magSafeRu)
        )
    }
}
