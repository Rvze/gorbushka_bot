package recognitioncommons.models.apple.iphone

import recognitioncommons.models.apple.AppleColor

enum class IphoneModel(
    var possibleColors: Set<AppleColor>,
    var possibleMemories: Set<IphoneMemory>,
) {
    IPHONE_14_PRO_MAX(
        possibleColors = setOf(
            AppleColor.SILVER,
            AppleColor.GOLD,
            AppleColor.SPACE_BLACK,
            AppleColor.DEEP_PURPLE,
        ),
        possibleMemories = setOf(
            IphoneMemory.GB_128, IphoneMemory.GB_256, IphoneMemory.GB_512, IphoneMemory.TB_1,
        ),
    ),
    IPHONE_14_PRO(
        possibleColors = setOf(
            AppleColor.SILVER,
            AppleColor.GOLD,
            AppleColor.SPACE_BLACK,
            AppleColor.DEEP_PURPLE,
        ),
        possibleMemories = setOf(
            IphoneMemory.GB_128, IphoneMemory.GB_256, IphoneMemory.GB_512, IphoneMemory.TB_1,
        ),
    ),
    IPHONE_14_PLUS(
        possibleColors = setOf(
            AppleColor.MIDNIGHT,
            AppleColor.BLUE,
            AppleColor.STARLIGHT,
            AppleColor.PURPLE,
            AppleColor.RED,
        ),
        possibleMemories = setOf(
            IphoneMemory.GB_128, IphoneMemory.GB_256, IphoneMemory.GB_512,
        ),
    ),
    IPHONE_14(
        possibleColors = setOf(
            AppleColor.MIDNIGHT,
            AppleColor.BLUE,
            AppleColor.STARLIGHT,
            AppleColor.PURPLE,
            AppleColor.RED,
        ),
        possibleMemories = setOf(
            IphoneMemory.GB_128, IphoneMemory.GB_256, IphoneMemory.GB_512,
        ),
    ),

    IPHONE_SE_3(
        possibleColors = setOf(
            AppleColor.RED,
            AppleColor.STARLIGHT,
            AppleColor.MIDNIGHT,
        ),
        possibleMemories = setOf(
            IphoneMemory.GB_64, IphoneMemory.GB_128, IphoneMemory.GB_256,
        ),
    ),

    IPHONE_13_PRO_MAX(
        possibleColors = setOf(
            AppleColor.GRAPHITE,
            AppleColor.GOLD,
            AppleColor.SILVER,
            AppleColor.SIERRA_BLUE,
            AppleColor.ALPINE_GREEN,
        ),
        possibleMemories = setOf(
            IphoneMemory.GB_128, IphoneMemory.GB_256, IphoneMemory.GB_512, IphoneMemory.TB_1,
        ),
    ),
    IPHONE_13_PRO(
        possibleColors = setOf(
            AppleColor.GRAPHITE,
            AppleColor.GOLD,
            AppleColor.SILVER,
            AppleColor.SIERRA_BLUE,
            AppleColor.ALPINE_GREEN,
        ),
        possibleMemories = setOf(
            IphoneMemory.GB_128, IphoneMemory.GB_256, IphoneMemory.GB_512, IphoneMemory.TB_1,
        ),
    ),
    IPHONE_13(
        possibleColors = setOf(
            AppleColor.RED,
            AppleColor.STARLIGHT,
            AppleColor.MIDNIGHT,
            AppleColor.BLUE,
            AppleColor.PINK,
            AppleColor.GREEN,
        ),
        possibleMemories = setOf(
            IphoneMemory.GB_128, IphoneMemory.GB_256, IphoneMemory.GB_512,
        ),
    ),
    IPHONE_13_MINI(
        possibleColors = setOf(
            AppleColor.RED,
            AppleColor.STARLIGHT,
            AppleColor.MIDNIGHT,
            AppleColor.BLUE,
            AppleColor.PINK,
            AppleColor.GREEN,
        ),
        possibleMemories = setOf(
            IphoneMemory.GB_128, IphoneMemory.GB_256, IphoneMemory.GB_512,
        ),
    ),

    IPHONE_12_PRO_MAX(
        possibleColors = setOf(
            AppleColor.SILVER,
            AppleColor.GRAPHITE,
            AppleColor.GOLD,
            AppleColor.PACIFIC_BLUE,
        ),
        possibleMemories = setOf(
            IphoneMemory.GB_128, IphoneMemory.GB_256, IphoneMemory.GB_512,
        ),
    ),
    IPHONE_12_PRO(
        possibleColors = setOf(
            AppleColor.SILVER,
            AppleColor.GRAPHITE,
            AppleColor.GOLD,
            AppleColor.PACIFIC_BLUE,
        ),
        possibleMemories = setOf(
            IphoneMemory.GB_128, IphoneMemory.GB_256, IphoneMemory.GB_512,
        ),
    ),
    IPHONE_12(
        possibleColors = setOf(
            AppleColor.BLACK,
            AppleColor.WHITE,
            AppleColor.RED,
            AppleColor.GREEN,
            AppleColor.BLUE,
            AppleColor.PURPLE,
        ),
        possibleMemories = setOf(
            IphoneMemory.GB_64, IphoneMemory.GB_128, IphoneMemory.GB_256,
        ),
    ),
    IPHONE_12_MINI(
        possibleColors = setOf(
            AppleColor.BLACK,
            AppleColor.WHITE,
            AppleColor.RED,
            AppleColor.GREEN,
            AppleColor.BLUE,
            AppleColor.PURPLE,
        ),
        possibleMemories = setOf(
            IphoneMemory.GB_64, IphoneMemory.GB_128, IphoneMemory.GB_256,
        ),
    ),

    IPHONE_SE_2(
        possibleColors = setOf(
            AppleColor.WHITE, AppleColor.BLACK, AppleColor.RED,
        ),
        possibleMemories = setOf(
            IphoneMemory.GB_64, IphoneMemory.GB_128, IphoneMemory.GB_256,
        ),
    ),
    IPHONE_11_PRO_MAX(
        possibleColors = setOf(
            AppleColor.SILVER,
            AppleColor.SPACE_GRAY,
            AppleColor.GOLD,
            AppleColor.MIDNIGHT_GREEN,
        ),
        possibleMemories = setOf(
            IphoneMemory.GB_64, IphoneMemory.GB_256, IphoneMemory.GB_512,
        ),
    ),
    IPHONE_11_PRO(
        possibleColors = setOf(
            AppleColor.SILVER,
            AppleColor.SPACE_GRAY,
            AppleColor.GOLD,
            AppleColor.MIDNIGHT_GREEN,
        ),
        possibleMemories = setOf(
            IphoneMemory.GB_64, IphoneMemory.GB_256, IphoneMemory.GB_512,
        ),
    ),
    IPHONE_11(
        possibleColors = setOf(
            AppleColor.PURPLE,
            AppleColor.GREEN,
            AppleColor.YELLOW,
            AppleColor.BLACK,
            AppleColor.WHITE,
            AppleColor.RED,
        ),
        possibleMemories = setOf(
            IphoneMemory.GB_64, IphoneMemory.GB_128, IphoneMemory.GB_256,
        ),
    ),
    ;
}
