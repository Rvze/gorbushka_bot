package recognitioncommons.service.recognition.apple.iphone.registry

import recognitioncommons.models.Locale
import recognitioncommons.models.apple.AppleColor
import recognitioncommons.service.search.AllMatchSearchPart
import recognitioncommons.service.search.AnyMatchSearchPart
import recognitioncommons.service.search.SearchPart
import recognitioncommons.service.search.SimpleSearchPart

object IphoneColorSearchPartsRegistry {
    val IPHONE_COLORS_SEARCH_PARTS_LIST = listOf<Pair<AppleColor, SearchPart>>(
        AppleColor.SPACE_GRAY to Colors.SPACE_GRAY,
        AppleColor.SPACE_BLACK to Colors.SPACE_BLACK,
        AppleColor.MIDNIGHT_GREEN to Colors.MIDNIGHT_GREEN,
        AppleColor.ALPINE_GREEN to Colors.ALPINE_GREEN,
        AppleColor.PACIFIC_BLUE to Colors.PACIFIC_BLUE,
        AppleColor.SIERRA_BLUE to Colors.SIERRA_BLUE,
        AppleColor.SKY_BLUE to Colors.SKY_BLUE,
        AppleColor.DEEP_PURPLE to Colors.DEEP_PURPLE,

        AppleColor.GRAY to Colors.GRAY,
        AppleColor.BLACK to Colors.BLACK,
        AppleColor.GREEN to Colors.GREEN,
        AppleColor.PURPLE to Colors.PURPLE,
        AppleColor.BLUE to Colors.BLUE,
        AppleColor.RED to Colors.RED,
        AppleColor.SILVER to Colors.SILVER,
        AppleColor.GOLD to Colors.GOLD,
        AppleColor.YELLOW to Colors.YELLOW,
        AppleColor.WHITE to Colors.WHITE,
        AppleColor.CORAL to Colors.CORAL,
        AppleColor.PINK to Colors.PINK,
        AppleColor.ROSE to Colors.ROSE,

        AppleColor.MIDNIGHT to Colors.MIDNIGHT,
        AppleColor.STARLIGHT to Colors.STARLIGHT,
        AppleColor.GRAPHITE to Colors.GRAPHITE,
    )

    object Colors {
        val GRAY = AnyMatchSearchPart(
            setOf(Common.GRAY_EN, Common.GRAY_RU)
        )
        val SPACE_GRAY = AnyMatchSearchPart(
            setOf(
                AllMatchSearchPart(
                    setOf(
                        Common.SPACE_EN,
                        Common.GRAY_EN,
                    )
                ),
                AllMatchSearchPart(
                    setOf(
                        Common.GRAY_RU,
                        Common.SPACE_RU,
                    )
                ),
            )
        )

        val BLACK = AnyMatchSearchPart(
            setOf(Common.BLACK_EN, Common.BLACK_RU)
        )
        val SPACE_BLACK = AnyMatchSearchPart(
            setOf(
                AllMatchSearchPart(
                    setOf(
                        Common.SPACE_EN,
                        Common.BLACK_EN,
                    )
                ),
                AllMatchSearchPart(
                    setOf(
                        Common.BLACK_RU,
                        Common.SPACE_RU,
                    )
                ),
            )
        )

        val GREEN = AnyMatchSearchPart(
            setOf(Common.GREEN_EN, Common.GREEN_RU)
        )
        val MIDNIGHT_GREEN = AnyMatchSearchPart(
            setOf(
                AllMatchSearchPart(
                    setOf(
                        Common.DARK_IT_RU,
                        Common.GREEN_RU,
                    )
                ),
                AllMatchSearchPart(
                    setOf(
                        Common.MIDNIGHT_EN,
                        Common.GREEN_EN,
                    )
                ),
            )
        )
        val ALPINE_GREEN = AnyMatchSearchPart(
            setOf(
                AllMatchSearchPart(
                    setOf(
                        Common.ALPINE_RU,
                        Common.GREEN_RU,
                    )
                ),
                AllMatchSearchPart(
                    setOf(
                        Common.ALPINE_EN,
                        Common.GREEN_EN,
                    )
                ),
            )
        )

        val PURPLE = AnyMatchSearchPart(
            setOf(Common.PURPLE_RU, Common.PURPLE_EN)
        )
        val DEEP_PURPLE = AnyMatchSearchPart(
            setOf(
                AllMatchSearchPart(
                    setOf(
                        Common.DARK_HE_RU,
                        Common.PURPLE_RU,
                    )
                ),
                AllMatchSearchPart(
                    setOf(
                        SimpleSearchPart("Deep", Locale.EN),
                        Common.PURPLE_EN,
                    )
                ),
            )
        )

        val BLUE = AnyMatchSearchPart(
            setOf(Common.BLUE_RU, Common.BLUE_EN)
        )
        val SIERRA_BLUE = AnyMatchSearchPart(
            setOf(
                AllMatchSearchPart(
                    setOf(
                        Common.SIERRA_RU,
                        Common.BLUE_RU,
                    )
                ),
                AllMatchSearchPart(
                    setOf(
                        Common.SIERRA_EN,
                        Common.BLUE_EN,
                    )
                ),
            )
        )
        val PACIFIC_BLUE = AnyMatchSearchPart(
            setOf(
                AllMatchSearchPart(
                    setOf(
                        Common.PACIFIC_RU,
                        Common.BLUE_RU,
                    )
                ),
                AllMatchSearchPart(
                    setOf(
                        Common.PACIFIC_EN,
                        Common.BLUE_EN,
                    )
                ),
            )
        )
        val SKY_BLUE = AnyMatchSearchPart(
            setOf(
                AllMatchSearchPart(
                    setOf(
                        Common.SKY_RU,
                        Common.BLUE_RU,
                    ),
                ),
                AllMatchSearchPart(
                    setOf(
                        Common.SKY_EN,
                        Common.BLUE_EN,
                    ),
                )
            )
        )

        val MIDNIGHT = AnyMatchSearchPart(
            setOf(
                Common.MIDNIGHT_EN,
                Common.MIDNIGHT_RU,
            )
        )

        val RED = AnyMatchSearchPart(
            setOf(Common.RED_RU, Common.RED_EN)
        )

        val STARLIGHT = AnyMatchSearchPart(
            setOf(Common.STARLIGHT_EN, Common.STARLIGHT_RU)
        )

        val GRAPHITE = AnyMatchSearchPart(
            setOf(Common.GRAPHITE_EN, Common.GRAPHITE_RU)
        )

        val SILVER = AnyMatchSearchPart(
            setOf(Common.SILVER_EN, Common.SILVER_RU)
        )

        val GOLD = AnyMatchSearchPart(
            setOf(Common.GOLD_EN, Common.GOLD_RU)
        )

        val YELLOW = AnyMatchSearchPart(
            setOf(Common.YELLOW_EN, Common.YELLOW_RU)
        )

        val WHITE = AnyMatchSearchPart(
            setOf(Common.WHITE_EN, Common.WHITE_RU)
        )

        val CORAL = AnyMatchSearchPart(
            setOf(Common.CORAL_EN, Common.CORAL_RU)
        )

        val PINK = AnyMatchSearchPart(
            setOf(Common.PINK_EN, Common.PINK_RU)
        )

        val ROSE = AnyMatchSearchPart(
            setOf(Common.ROSE_EN, Common.ROSE_RU)
        )
    }

    private object Common {
        val GRAY_EN = AnyMatchSearchPart(
            setOf(
                SimpleSearchPart("Gray", Locale.EN),
                SimpleSearchPart("Grey", Locale.EN),
            )
        )
        val GRAY_RU = SimpleSearchPart("Серый", Locale.RU)

        val SPACE_EN = SimpleSearchPart("Space", Locale.EN)
        val SPACE_RU = SimpleSearchPart("Космос", Locale.RU)

        val BLACK_EN = SimpleSearchPart("Black", Locale.EN)
        val BLACK_RU = AnyMatchSearchPart(
            setOf(
                SimpleSearchPart("Чёрный", Locale.RU),
                SimpleSearchPart("Черный", Locale.RU),
                SimpleSearchPart("Чёрн", Locale.RU),
                SimpleSearchPart("Черн", Locale.RU),
            )
        )

        val GREEN_EN = SimpleSearchPart("Green", Locale.EN)
        val GREEN_RU = AnyMatchSearchPart(
            setOf(
                SimpleSearchPart("Зелёный", Locale.RU),
                SimpleSearchPart("Зеленый", Locale.RU),
                SimpleSearchPart("Зел", Locale.RU),
            )
        )

        private val DARK_SHORT_RU = AnyMatchSearchPart(
            setOf(
                SimpleSearchPart("Тёмн", Locale.RU),
                SimpleSearchPart("Темн", Locale.RU),
            )
        )
        val DARK_IT_RU = AnyMatchSearchPart(
            setOf(
                SimpleSearchPart("Тёмно", Locale.RU),
                SimpleSearchPart("Темно", Locale.RU),
                DARK_SHORT_RU,
            )
        )
        val DARK_HE_RU = AnyMatchSearchPart(
            setOf(
                SimpleSearchPart("Темный", Locale.RU),
                SimpleSearchPart("Тёмный", Locale.RU),
                DARK_SHORT_RU,
            )
        )
        val DARK_SHE_RU = AnyMatchSearchPart(
            setOf(
                SimpleSearchPart("Темная", Locale.RU),
                SimpleSearchPart("Тёмная", Locale.RU),
                DARK_SHORT_RU,
            )
        )

        val MIDNIGHT_EN = SimpleSearchPart("Midnight", Locale.EN)
        val NIGHT_RU = SimpleSearchPart("Ночь", Locale.RU)
        val MIDNIGHT_RU = AnyMatchSearchPart(
            setOf(
                AllMatchSearchPart(
                    setOf(
                        DARK_SHE_RU,
                        NIGHT_RU,
                    )
                ),
                SimpleSearchPart("Миднайт", Locale.RU)
            )
        )

        val ALPINE_EN = SimpleSearchPart("Alpine", Locale.EN)
        val ALPINE_RU = SimpleSearchPart("Альпийский", Locale.RU)

        val PURPLE_EN = SimpleSearchPart("Purple", Locale.EN)
        val PURPLE_RU = AnyMatchSearchPart(
            setOf(
                SimpleSearchPart("Фиолетовый", Locale.RU),
                SimpleSearchPart("Фиолетовые", Locale.RU),
                SimpleSearchPart("Фиолет", Locale.RU),
                SimpleSearchPart("Сиреневый", Locale.RU),
                SimpleSearchPart("Сирен", Locale.RU),
                SimpleSearchPart("Пурпур", Locale.RU),
            )
        )

        val BLUE_EN = SimpleSearchPart("Blue", Locale.EN)
        val BLUE_RU = AnyMatchSearchPart(
            setOf(
                SimpleSearchPart("Синий", Locale.RU),
                SimpleSearchPart("Голубой", Locale.RU),
            )
        )

        val PACIFIC_EN = SimpleSearchPart("Pacific", Locale.EN)
        val PACIFIC_RU = SimpleSearchPart("Тихоокеанский", Locale.RU)
        val SIERRA_EN = SimpleSearchPart("Sierra", Locale.EN)
        val SIERRA_RU = SimpleSearchPart("Небесно", Locale.RU)
        val SKY_EN = SimpleSearchPart("Sky", Locale.EN)
        val SKY_RU = SimpleSearchPart("Небесно", Locale.RU)

        val RED_RU = AnyMatchSearchPart(
            setOf(
                SimpleSearchPart("Красный", Locale.RU),
                SimpleSearchPart("Красн", Locale.RU),
                SimpleSearchPart("Крас", Locale.RU),
            )
        )
        val RED_EN = SimpleSearchPart("Red", Locale.EN)

        val STARLIGHT_EN = SimpleSearchPart("Starlight", Locale.EN)
        val STARLIGHT_RU = AnyMatchSearchPart(
            setOf(
                AllMatchSearchPart(
                    setOf(
                        SimpleSearchPart("Сияющая", Locale.RU),
                        SimpleSearchPart("Звезда", Locale.RU),
                    )
                ),
                SimpleSearchPart("Старлайт", Locale.RU),
            )
        )

        val GRAPHITE_EN = SimpleSearchPart("Graphite", Locale.EN)
        val GRAPHITE_RU = SimpleSearchPart("Графит", Locale.RU)

        val SILVER_EN = SimpleSearchPart("Silver", Locale.EN)
        val SILVER_RU = AnyMatchSearchPart(
            setOf(
                SimpleSearchPart("Сильвер", Locale.RU),
                SimpleSearchPart("Серебристый", Locale.RU),
                SimpleSearchPart("Серебро", Locale.RU),
                SimpleSearchPart("Серебряный", Locale.RU),
                SimpleSearchPart("Серебренный", Locale.RU)
            )
        )

        val GOLD_EN = SimpleSearchPart("Gold", Locale.EN)
        val GOLD_RU = AnyMatchSearchPart(
            setOf(
                SimpleSearchPart("Золотой", Locale.RU),
                SimpleSearchPart("Золото", Locale.RU),
                SimpleSearchPart("Зол", Locale.RU)
            )
        )

        val YELLOW_EN = SimpleSearchPart("Yellow", Locale.EN)
        val YELLOW_RU = AnyMatchSearchPart(
            setOf(
                SimpleSearchPart("Жёлтый", Locale.RU),
                SimpleSearchPart("Желтный", Locale.RU),
                SimpleSearchPart("Жёлт", Locale.RU),
                SimpleSearchPart("Желт", Locale.RU),
            )
        )

        val WHITE_EN = SimpleSearchPart("White", Locale.EN)
        val WHITE_RU = SimpleSearchPart("Белый", Locale.RU)

        val CORAL_EN = SimpleSearchPart("Coral", Locale.EN)
        val CORAL_RU = SimpleSearchPart("Коралловый", Locale.RU)

        val PINK_EN = SimpleSearchPart("Pink", Locale.EN)
        val PINK_RU = AnyMatchSearchPart(
            setOf(
                SimpleSearchPart("Розовый", Locale.RU),
                SimpleSearchPart("Розов", Locale.RU),
                SimpleSearchPart("Роз", Locale.RU),
            )
        )

        val ROSE_EN = SimpleSearchPart("Rose", Locale.EN)
        val ROSE_RU = SimpleSearchPart("Роза", Locale.RU)
    }
}
