package recognitioncommons.service.recognition.apple.iphone.registry

import recognitioncommons.models.Locale
import recognitioncommons.models.apple.iphone.IphoneModel
import recognitioncommons.service.search.AllMatchSearchPart
import recognitioncommons.service.search.AnyMatchSearchPart
import recognitioncommons.service.search.SearchPart
import recognitioncommons.service.search.SimpleSearchPart

object IphoneModelsSearchPartsRegistry {
    val IPHONE_MODELS_SEARCH_PARTS_LIST = listOf<Pair<IphoneModel, SearchPart>>(
        IphoneModel.IPHONE_14_PRO_MAX to Iphones.IPHONE_14_PRO_MAX_SP,
        IphoneModel.IPHONE_13_PRO_MAX to Iphones.IPHONE_13_PRO_MAX_SP,
        IphoneModel.IPHONE_12_PRO_MAX to Iphones.IPHONE_12_PRO_MAX_SP,
        IphoneModel.IPHONE_11_PRO_MAX to Iphones.IPHONE_11_PRO_MAX_SP,

        IphoneModel.IPHONE_14_PRO to Iphones.IPHONE_14_PRO_SP,
        IphoneModel.IPHONE_13_PRO to Iphones.IPHONE_13_PRO_SP,
        IphoneModel.IPHONE_12_PRO to Iphones.IPHONE_12_PRO_SP,
        IphoneModel.IPHONE_11_PRO to Iphones.IPHONE_11_PRO_SP,

        IphoneModel.IPHONE_14_PLUS to Iphones.IPHONE_14_PLUS_SP,
        IphoneModel.IPHONE_13_MINI to Iphones.IPHONE_13_MINI_SP,
        IphoneModel.IPHONE_12_MINI to Iphones.IPHONE_12_MINI_SP,

        IphoneModel.IPHONE_14 to Iphones.IPHONE_14_SP,
        IphoneModel.IPHONE_13 to Iphones.IPHONE_13_SP,
        IphoneModel.IPHONE_12 to Iphones.IPHONE_12_SP,
        IphoneModel.IPHONE_11 to Iphones.IPHONE_11_SP,

        IphoneModel.IPHONE_SE_3 to Iphones.IPHONE_SE_3_SP,
        IphoneModel.IPHONE_SE_2 to Iphones.IPHONE_SE_2_SP,
    )

    private object Iphones {
        val IPHONE_14_PRO_MAX_SP = AllMatchSearchPart(
            setOf(Components.i14, Components.PRO, Components.MAX)
        )
        val IPHONE_14_PRO_SP = AllMatchSearchPart(
            setOf(Components.i14, Components.PRO)
        )
        val IPHONE_14_PLUS_SP = AllMatchSearchPart(
            setOf(Components.i14, Components.PLUS)
        )
        val IPHONE_14_SP = AllMatchSearchPart(
            setOf(Components.i14)
        )

        val IPHONE_SE_3_SP = AllMatchSearchPart(
            setOf(Components.SE, SimpleSearchPart("3"))
        )

        val IPHONE_13_PRO_MAX_SP = AllMatchSearchPart(
            setOf(Components.i13, Components.PRO, Components.MAX)
        )
        val IPHONE_13_PRO_SP = AllMatchSearchPart(
            setOf(Components.i13, Components.PRO)
        )
        val IPHONE_13_SP = AllMatchSearchPart(
            setOf(Components.i13)
        )
        val IPHONE_13_MINI_SP = AllMatchSearchPart(
            setOf(Components.i13, Components.MINI)
        )

        val IPHONE_12_PRO_MAX_SP = AllMatchSearchPart(
            setOf(Components.i12, Components.PRO, Components.MAX)
        )
        val IPHONE_12_PRO_SP = AllMatchSearchPart(
            setOf(Components.i12, Components.PRO)
        )
        val IPHONE_12_SP = AllMatchSearchPart(
            setOf(Components.i12)
        )
        val IPHONE_12_MINI_SP = AllMatchSearchPart(
            setOf(Components.i12, Components.MINI)
        )

        val IPHONE_SE_2_SP = AllMatchSearchPart(
            setOf(Components.SE, SimpleSearchPart("2"))
        )

        val IPHONE_11_PRO_MAX_SP = AllMatchSearchPart(
            setOf(Components.i11, Components.PRO, Components.MAX)
        )
        val IPHONE_11_PRO_SP = AllMatchSearchPart(
            setOf(Components.i11, Components.PRO)
        )
        val IPHONE_11_SP = AllMatchSearchPart(
            setOf(Components.i11)
        )
    }

    private object Components {
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

        val plusEn = SimpleSearchPart("Plus", Locale.EN)
        val plusRu = SimpleSearchPart("Плюс", Locale.RU)
        val plusSign = SimpleSearchPart("+")
        val PLUS = AnyMatchSearchPart(
            setOf(plusEn, plusRu, plusSign)
        )

        val miniEn = SimpleSearchPart("Mini", Locale.EN)
        val miniRu = SimpleSearchPart("Мини", Locale.RU)
        val MINI = AnyMatchSearchPart(
            setOf(miniEn, miniRu)
        )

        val seEn = SimpleSearchPart("SE", Locale.EN)
        val seRu = SimpleSearchPart("СЕ", Locale.RU)
        val SE = AnyMatchSearchPart(
            setOf(seEn, seRu)
        )

        val i14 = SimpleSearchPart("14")
        val i13 = SimpleSearchPart("13")
        val i12 = SimpleSearchPart("12")
        val i11 = SimpleSearchPart("11")
    }
}
