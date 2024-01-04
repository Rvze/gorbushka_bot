package recognitioncommons.models.apple.macbook

import recognitioncommons.models.apple.AppleChip
import recognitioncommons.models.apple.AppleColor

enum class MacbookModel(
    val possibleColors: Set<AppleColor>,
    val possibleMemories: Set<MacbookMemory>,
    val possibleRams: Set<MacbookRam>,
    val screen: MacbookScreen,
    val appleChip: AppleChip,
) {
    // 2020
    MACBOOK_AIR_13_M1(
        possibleColors = setOf(AppleColor.SPACE_GRAY, AppleColor.GOLD, AppleColor.SILVER),
        possibleMemories = setOf(MacbookMemory.GB_256, MacbookMemory.GB_512, MacbookMemory.TB_1, MacbookMemory.TB_2),
        possibleRams = setOf(MacbookRam.GB_8, MacbookRam.GB_16),
        appleChip = AppleChip.M1,
        screen = MacbookScreen.INCH_13,
    ),
    MACBOOK_PRO_13_M1(
        possibleColors = setOf(AppleColor.SPACE_GRAY, AppleColor.SILVER),
        possibleMemories = setOf(MacbookMemory.GB_256, MacbookMemory.GB_512, MacbookMemory.TB_1, MacbookMemory.TB_2),
        possibleRams = setOf(MacbookRam.GB_8, MacbookRam.GB_16),
        appleChip = AppleChip.M1,
        screen = MacbookScreen.INCH_13,
    ),

    // 2021
    MACBOOK_PRO_14_M1_PRO(
        possibleColors = setOf(AppleColor.SPACE_GRAY, AppleColor.SILVER),
        possibleMemories = setOf(
            MacbookMemory.GB_512,
            MacbookMemory.TB_1,
            MacbookMemory.TB_2,
            MacbookMemory.TB_4,
            MacbookMemory.TB_8
        ),
        possibleRams = setOf(MacbookRam.GB_16, MacbookRam.GB_32),
        appleChip = AppleChip.M1_PRO,
        screen = MacbookScreen.INCH_14,
    ),
    MACBOOK_PRO_14_M1_MAX(
        possibleColors = setOf(AppleColor.SPACE_GRAY, AppleColor.SILVER),
        possibleMemories = setOf(
            MacbookMemory.GB_512,
            MacbookMemory.TB_1,
            MacbookMemory.TB_2,
            MacbookMemory.TB_4,
            MacbookMemory.TB_8
        ),
        possibleRams = setOf(MacbookRam.GB_16, MacbookRam.GB_32, MacbookRam.GB_64),
        appleChip = AppleChip.M1_MAX,
        screen = MacbookScreen.INCH_14,
    ),
    MACBOOK_PRO_16_M1_PRO(
        possibleColors = setOf(AppleColor.SPACE_GRAY, AppleColor.SILVER),
        possibleMemories = setOf(
            MacbookMemory.GB_512,
            MacbookMemory.TB_1,
            MacbookMemory.TB_2,
            MacbookMemory.TB_4,
            MacbookMemory.TB_8
        ),
        possibleRams = setOf(MacbookRam.GB_16, MacbookRam.GB_32),
        appleChip = AppleChip.M1_PRO,
        screen = MacbookScreen.INCH_16,
    ),
    MACBOOK_PRO_16_M1_MAX(
        possibleColors = setOf(AppleColor.SPACE_GRAY, AppleColor.SILVER),
        possibleMemories = setOf(MacbookMemory.TB_1, MacbookMemory.TB_2, MacbookMemory.TB_4, MacbookMemory.TB_8),
        possibleRams = setOf(MacbookRam.GB_32, MacbookRam.GB_64),
        appleChip = AppleChip.M1_MAX,
        screen = MacbookScreen.INCH_16,
    ),

    // 2022
    MACBOOK_AIR_13_M2(
        possibleColors = setOf(AppleColor.SILVER, AppleColor.STARLIGHT, AppleColor.SPACE_GRAY, AppleColor.MIDNIGHT),
        possibleMemories = setOf(MacbookMemory.GB_256, MacbookMemory.GB_512, MacbookMemory.TB_1, MacbookMemory.TB_2),
        possibleRams = setOf(MacbookRam.GB_8, MacbookRam.GB_16, MacbookRam.GB_24),
        appleChip = AppleChip.M2,
        screen = MacbookScreen.INCH_13,
    ),
    MACBOOK_PRO_13_M2(
        possibleColors = setOf(AppleColor.SPACE_GRAY, AppleColor.SILVER),
        possibleMemories = setOf(MacbookMemory.GB_256, MacbookMemory.GB_512, MacbookMemory.TB_1, MacbookMemory.TB_2),
        possibleRams = setOf(MacbookRam.GB_8, MacbookRam.GB_16, MacbookRam.GB_24),
        appleChip = AppleChip.M2,
        screen = MacbookScreen.INCH_13,
    ),

    // 2023
    MACBOOK_PRO_14_M2_PRO(
        possibleColors = setOf(AppleColor.SPACE_GRAY, AppleColor.SILVER),
        possibleMemories = setOf(
            MacbookMemory.GB_512,
            MacbookMemory.TB_1,
            MacbookMemory.TB_2,
            MacbookMemory.TB_4,
            MacbookMemory.TB_8
        ),
        possibleRams = setOf(MacbookRam.GB_16, MacbookRam.GB_32),
        appleChip = AppleChip.M2_PRO,
        screen = MacbookScreen.INCH_14,
    ),
    MACBOOK_PRO_14_M2_MAX(
        possibleColors = setOf(AppleColor.SPACE_GRAY, AppleColor.SILVER),
        possibleMemories = setOf(MacbookMemory.TB_1, MacbookMemory.TB_2, MacbookMemory.TB_4, MacbookMemory.TB_8),
        possibleRams = setOf(MacbookRam.GB_32, MacbookRam.GB_64, MacbookRam.GB_96),
        appleChip = AppleChip.M2_MAX,
        screen = MacbookScreen.INCH_14,
    ),
    MACBOOK_PRO_16_M2_PRO(
        possibleColors = setOf(AppleColor.SPACE_GRAY, AppleColor.SILVER),
        possibleMemories = setOf(
            MacbookMemory.GB_512,
            MacbookMemory.TB_1,
            MacbookMemory.TB_2,
            MacbookMemory.TB_4,
            MacbookMemory.TB_8
        ),
        possibleRams = setOf(MacbookRam.GB_16, MacbookRam.GB_32),
        appleChip = AppleChip.M2_PRO,
        screen = MacbookScreen.INCH_16,
    ),
    MACBOOK_PRO_16_M2_MAX(
        possibleColors = setOf(AppleColor.SPACE_GRAY, AppleColor.SILVER),
        possibleMemories = setOf(
            MacbookMemory.GB_512,
            MacbookMemory.TB_1,
            MacbookMemory.TB_2,
            MacbookMemory.TB_4,
            MacbookMemory.TB_8
        ),
        possibleRams = setOf(MacbookRam.GB_16, MacbookRam.GB_32),
        appleChip = AppleChip.M2_PRO,
        screen = MacbookScreen.INCH_16,
    )
}
