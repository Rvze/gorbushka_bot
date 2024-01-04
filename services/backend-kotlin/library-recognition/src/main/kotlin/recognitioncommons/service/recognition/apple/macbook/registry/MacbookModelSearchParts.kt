package recognitioncommons.service.recognition.apple.macbook.registry

import recognitioncommons.models.Locale
import recognitioncommons.models.apple.macbook.MacbookModel
import recognitioncommons.service.search.*

object MacbookModelSearchParts {
    val MACBOOK_MODEL_SEARCH_PARTS = listOf<Pair<MacbookModel, SearchPart>>(
        MacbookModel.MACBOOK_PRO_16_M2_MAX to Macbooks.MACBOOK_PRO_16_M2_MAX_SP,
        MacbookModel.MACBOOK_PRO_14_M2_MAX to Macbooks.MACBOOK_PRO_14_M2_MAX_SP,
        MacbookModel.MACBOOK_PRO_16_M1_MAX to Macbooks.MACBOOK_PRO_16_M1_MAX_SP,
        MacbookModel.MACBOOK_PRO_14_M1_MAX to Macbooks.MACBOOK_PRO_14_M1_MAX_SP,

        MacbookModel.MACBOOK_PRO_16_M2_PRO to Macbooks.MACBOOK_PRO_16_M2_PRO_SP,
        MacbookModel.MACBOOK_PRO_14_M2_PRO to Macbooks.MACBOOK_PRO_14_M2_PRO_SP,
        MacbookModel.MACBOOK_PRO_16_M1_PRO to Macbooks.MACBOOK_PRO_16_M1_PRO_SP,
        MacbookModel.MACBOOK_PRO_14_M1_PRO to Macbooks.MACBOOK_PRO_14_M1_PRO_SP,

        MacbookModel.MACBOOK_PRO_13_M2 to Macbooks.MACBOOK_PRO_13_M2_SP,
        MacbookModel.MACBOOK_PRO_13_M1 to Macbooks.MACBOOK_PRO_13_M1_SP,

        MacbookModel.MACBOOK_AIR_13_M2 to Macbooks.MACBOOK_AIR_13_M2_SP,
        MacbookModel.MACBOOK_AIR_13_M1 to Macbooks.MACBOOK_AIR_13_M1_SP,
    )

    val MACBOOK_MODEL_SEARCH_PARTS_NO_MACBOOK = listOf<Pair<MacbookModel, SearchPart>>(
        MacbookModel.MACBOOK_PRO_16_M2_MAX to Macbooks.MACBOOK_PRO_16_M2_MAX_SP
            .apply { this.searchParts = this.searchParts - Components.MACBOOK },
        MacbookModel.MACBOOK_PRO_14_M2_MAX to Macbooks.MACBOOK_PRO_14_M2_MAX_SP
            .apply { this.searchParts = this.searchParts - Components.MACBOOK },
        MacbookModel.MACBOOK_PRO_16_M1_MAX to Macbooks.MACBOOK_PRO_16_M1_MAX_SP
            .apply { this.searchParts = this.searchParts - Components.MACBOOK },
        MacbookModel.MACBOOK_PRO_14_M1_MAX to Macbooks.MACBOOK_PRO_14_M1_MAX_SP
            .apply { this.searchParts = this.searchParts - Components.MACBOOK },

        MacbookModel.MACBOOK_PRO_16_M2_PRO to Macbooks.MACBOOK_PRO_16_M2_PRO_SP
            .apply { this.searchParts = this.searchParts - Components.MACBOOK },
        MacbookModel.MACBOOK_PRO_14_M2_PRO to Macbooks.MACBOOK_PRO_14_M2_PRO_SP
            .apply { this.searchParts = this.searchParts - Components.MACBOOK },
        MacbookModel.MACBOOK_PRO_16_M1_PRO to Macbooks.MACBOOK_PRO_16_M1_PRO_SP
            .apply { this.searchParts = this.searchParts - Components.MACBOOK },
        MacbookModel.MACBOOK_PRO_14_M1_PRO to Macbooks.MACBOOK_PRO_14_M1_PRO_SP
            .apply { this.searchParts = this.searchParts - Components.MACBOOK },

        MacbookModel.MACBOOK_PRO_13_M2 to Macbooks.MACBOOK_PRO_13_M2_SP
            .apply { this.searchParts = this.searchParts - Components.MACBOOK },
        MacbookModel.MACBOOK_PRO_13_M1 to Macbooks.MACBOOK_PRO_13_M1_SP
            .apply { this.searchParts = this.searchParts - Components.MACBOOK },

        MacbookModel.MACBOOK_AIR_13_M2 to Macbooks.MACBOOK_AIR_13_M2_SP
            .apply { this.searchParts = this.searchParts - Components.MACBOOK },
        MacbookModel.MACBOOK_AIR_13_M1 to Macbooks.MACBOOK_AIR_13_M1_SP
            .apply { this.searchParts = this.searchParts - Components.MACBOOK },
    )

    private object Macbooks {
        val MACBOOK_AIR_13_M1_SP = AllMatchSearchPart(
//          setOf(Components.MACBOOK, Components.AIR, Components.INCH_13, Components.M1)
            setOf(Components.MACBOOK, Components.AIR, Components.M1)
        )
        val MACBOOK_PRO_13_M1_SP = AllMatchSearchPart(
            setOf(Components.MACBOOK, Components.PRO, Components.INCH_13, Components.M1)
        )
        val MACBOOK_PRO_14_M1_PRO_SP = AllMatchSearchPart(
            setOf(Components.MACBOOK, Components.PRO, Components.INCH_14, Components.M1_PRO)
        )
        val MACBOOK_PRO_14_M1_MAX_SP = AllMatchSearchPart(
            setOf(Components.MACBOOK, Components.PRO, Components.INCH_14, Components.M1_MAX)
        )
        val MACBOOK_PRO_16_M1_PRO_SP = AllMatchSearchPart(
            setOf(Components.MACBOOK, Components.PRO, Components.INCH_16, Components.M1_PRO)
        )
        val MACBOOK_PRO_16_M1_MAX_SP = AllMatchSearchPart(
            setOf(Components.MACBOOK, Components.PRO, Components.INCH_16, Components.M1_MAX)
        )
        val MACBOOK_AIR_13_M2_SP = AllMatchSearchPart(
//          setOf(Components.MACBOOK, Components.AIR, Components.INCH_13, Components.M2)
            setOf(Components.MACBOOK, Components.AIR, Components.M2)
        )
        val MACBOOK_PRO_13_M2_SP = AllMatchSearchPart(
            setOf(Components.MACBOOK, Components.PRO, Components.INCH_13, Components.M2)
        )
        val MACBOOK_PRO_14_M2_PRO_SP = AllMatchSearchPart(
            setOf(Components.MACBOOK, Components.PRO, Components.INCH_14, Components.M2_PRO)
        )
        val MACBOOK_PRO_14_M2_MAX_SP = AllMatchSearchPart(
            setOf(Components.MACBOOK, Components.PRO, Components.INCH_14, Components.M2_MAX)
        )
        val MACBOOK_PRO_16_M2_PRO_SP = AllMatchSearchPart(
            setOf(Components.MACBOOK, Components.PRO, Components.INCH_16, Components.M2_PRO)
        )
        val MACBOOK_PRO_16_M2_MAX_SP = AllMatchSearchPart(
            setOf(Components.MACBOOK, Components.PRO, Components.INCH_16, Components.M2_MAX)
        )
    }

    private object Components {
        val macbookEn = AnyMatchSearchPart(
            setOf(
                SimpleSearchPart("Macbook", Locale.EN),
                SimpleSearchPart("Mac", Locale.EN),
                SimpleSearchPart("MacBook", Locale.EN)
            )
        )
        val macbookRu = AnyMatchSearchPart(
            setOf(
                SimpleSearchPart("Макбук", Locale.EN),
                SimpleSearchPart("Мак", Locale.EN),
            )
        )
        val MACBOOK = AnyMatchSearchPart(
            setOf(macbookEn, macbookRu)
        )


        val airEn = SimpleSearchPart("Air", Locale.EN)
        val airRu = AnyMatchSearchPart(
            setOf(
                SimpleSearchPart("Эир", Locale.RU),
                SimpleSearchPart("Аир", Locale.RU),
            )
        )
        val AIR = AnyMatchSearchPart(
            setOf(airEn, airRu)
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

        val m1En = WhitespaceSearchPart(Locale.EN, "M", "1")
        val m1Ru = WhitespaceSearchPart(Locale.RU, "М", "1")
        val M1 = AnyMatchSearchPart(
            setOf(m1En, m1Ru)
        )

        val m2En = WhitespaceSearchPart(Locale.EN, "M", "2")
        val m2Ru = WhitespaceSearchPart(Locale.RU, "М", "2")
        val M2 = AnyMatchSearchPart(
            setOf(m2En, m2Ru)
        )

        val M1_PRO = AllMatchSearchPart(
            setOf(M1, PRO)
        )
        val M1_MAX = AllMatchSearchPart(
            setOf(M1, MAX)
        )

        val M2_PRO = AllMatchSearchPart(
            setOf(M2, PRO)
        )
        val M2_MAX = AllMatchSearchPart(
            setOf(M2, MAX)
        )

        val INCH_13 = SimpleSearchPart("13", Locale.NO)
        val INCH_14 = SimpleSearchPart("14", Locale.NO)
        val INCH_16 = SimpleSearchPart("16", Locale.NO)
    }
}