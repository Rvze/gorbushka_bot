package recognitioncommons.models.apple.airpods

import recognitioncommons.models.apple.AppleColor

enum class AirPodsModel(
    var possibleColors: Set<AppleColor>,
) {
    AIRPODS_2(
        possibleColors = setOf(AppleColor.DEFAULT),
    ),
    AIRPODS_3_MAGSAFE(
        possibleColors = setOf(AppleColor.DEFAULT),
    ),
    AIRPODS_3_LIGHTNING(
        possibleColors = setOf(AppleColor.DEFAULT),
    ),
    AIRPODS_PRO_1(
        possibleColors = setOf(AppleColor.DEFAULT),
    ),
    AIRPODS_PRO_2(
        possibleColors = setOf(AppleColor.DEFAULT),
    ),
    AIRPODS_MAX(
        possibleColors = setOf(
            AppleColor.SILVER, AppleColor.SPACE_GRAY, AppleColor.PINK, AppleColor.GREEN, AppleColor.SKY_BLUE,
        ),
    )
}
