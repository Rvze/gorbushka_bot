package recognitioncommons.service.recognition.sony.ps5.registry

import recognitioncommons.models.Locale
import recognitioncommons.models.sony.PlaystationModel
import recognitioncommons.service.search.*

object PlaystationModelSearchPartsRegistry {

    val PS5_SEARCH_MODEL_PARTS_LIST = listOf<Pair<PlaystationModel, SearchPart>>(
        PlaystationModel.PS5_DIGITAL_EDITION_2023 to Playstation.PS5_DIGITAL_2023_3,
        PlaystationModel.PS5_DIGITAL_EDITION_2022 to Playstation.PS5_DIGITAL_2022_2,
        PlaystationModel.PS5_DISK_EDITION_2022 to Playstation.PS5_DISK_2022_2,
        PlaystationModel.PS5_DISK_EDITION_2023 to Playstation.PS5_DISK_2023_3
    )

    private object Component {
        val playstationEn = AnyMatchSearchPart(
            setOf(
                SimpleSearchPart("Playstation", Locale.EN),
                SimpleSearchPart("PlayStation", Locale.EN),
                SimpleSearchPart("PS", Locale.EN),
                SimpleSearchPart("PS5", Locale.EN),
                SimpleSearchPart("Play station", Locale.EN)
            )
        )
        val playstationRu = AnyMatchSearchPart(
            setOf(
                SimpleSearchPart("Плойка", Locale.RU),
                SimpleSearchPart("Плейстейшн", Locale.RU),
                SimpleSearchPart("Плейстэйшн", Locale.RU),
                SimpleSearchPart("Пс", Locale.RU)
            )
        )

        val PLAYSTATION = AnyMatchSearchPart(setOf(playstationEn, playstationRu))

        val digitalEn = SimpleSearchPart("Digital", Locale.EN)
        val digitalRu = SimpleSearchPart("Диджитал", Locale.RU)

        val DIGITAL = AnyMatchSearchPart(setOf(digitalRu, digitalEn))

        val diskEn = SimpleSearchPart("Disk", Locale.EN)
        val diskRu = SimpleSearchPart("Диск", Locale.RU)

        val DISK = AnyMatchSearchPart(setOf(diskEn, diskRu))

        val TWENTY_TWENTY_TWO = SimpleSearchPart("2022")
        val TWENTY_TWENTY_THREE = SimpleSearchPart("2023")

        val secondRevisionEn = AnyMatchSearchPart(
            setOf(
                WhitespaceSearchPart(Locale.EN, "2", "revision"),
                WhitespaceSearchPart(Locale.EN, "second", "revision"),
                WhitespaceSearchPart(Locale.EN, "2", "rev"),
                WhitespaceSearchPart(Locale.EN, "second", "rev")
            )
        )
        val secondRevisionRu = AnyMatchSearchPart(
            setOf(
                WhitespaceSearchPart(Locale.RU, "вторая", "ревизия"),
                WhitespaceSearchPart(Locale.RU, "2", "ревизия"),
                WhitespaceSearchPart(Locale.RU, "2", "рев")
            )
        )
        val SECOND_REVISION = AnyMatchSearchPart(setOf(secondRevisionEn, secondRevisionRu))

        val thirdRevisionEn = AnyMatchSearchPart(
            setOf(
                WhitespaceSearchPart(Locale.EN, "3", "revision"),
                WhitespaceSearchPart(Locale.EN, "third", "revision"),
                WhitespaceSearchPart(Locale.EN, "3", "rev"),
                WhitespaceSearchPart(Locale.EN, "third", "rev")
            )
        )
        val thirdRevisionRu = AnyMatchSearchPart(
            setOf(
                WhitespaceSearchPart(Locale.RU, "третья", "ревизия"),
                WhitespaceSearchPart(Locale.RU, "3", "ревизия"),
                WhitespaceSearchPart(Locale.RU, "3", "рев")
            )
        )

        val THIRD_REVISION = AnyMatchSearchPart(
            setOf(
                thirdRevisionEn, thirdRevisionRu
            )
        )
    }

    private object Playstation {
        val PS5_DIGITAL_2022_2 = AllMatchSearchPart(
            setOf(Component.PLAYSTATION, Component.DIGITAL, Component.TWENTY_TWENTY_TWO, Component.SECOND_REVISION)
        )

        val PS5_DIGITAL_2023_3 = AllMatchSearchPart(
            setOf(Component.PLAYSTATION, Component.DIGITAL, Component.TWENTY_TWENTY_THREE, Component.THIRD_REVISION)
        )
        val PS5_DISK_2022_2 = AllMatchSearchPart(
            setOf(Component.PLAYSTATION, Component.DISK, Component.TWENTY_TWENTY_TWO, Component.SECOND_REVISION)
        )
        val PS5_DISK_2023_3 = AllMatchSearchPart(
            setOf(Component.PLAYSTATION, Component.DISK, Component.TWENTY_TWENTY_THREE, Component.THIRD_REVISION)
        )

    }
}