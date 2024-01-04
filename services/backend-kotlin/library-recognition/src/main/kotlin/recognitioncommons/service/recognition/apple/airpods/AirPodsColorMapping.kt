package recognitioncommons.service.recognition.apple.airpods

import recognitioncommons.exception.AirPodsColorIsIncorrectException
import recognitioncommons.models.apple.AppleColor
import recognitioncommons.models.apple.airpods.AirPodsModel
import recognitioncommons.util.isValidColor

fun mapColor(model: AirPodsModel, color: AppleColor): AppleColor {
    if (model.isValidColor(color))
        return color

    val notIncorrectColor = color.mapIncorrect()
    if (model.isValidColor(color)) {
        return notIncorrectColor
    }

    val actualColor = mapToRealColor(model, color)
    if (model.isValidColor(actualColor))
        return actualColor

    throw AirPodsColorIsIncorrectException(model, color)
}

private fun mapToRealColor(model: AirPodsModel, color: AppleColor): AppleColor {
    return when (model) {
        AirPodsModel.AIRPODS_MAX -> {
            when (color) {
                AppleColor.DEFAULT -> throw AirPodsColorIsIncorrectException(model, color)
                AppleColor.WHITE, AppleColor.STARLIGHT -> AppleColor.SILVER
                AppleColor.GRAPHITE, AppleColor.MIDNIGHT, AppleColor.BLACK, AppleColor.SPACE_BLACK, AppleColor.GRAY -> AppleColor.SPACE_GRAY
                AppleColor.BLUE, AppleColor.PACIFIC_BLUE, AppleColor.SIERRA_BLUE -> AppleColor.SKY_BLUE
                AppleColor.ROSE, AppleColor.CORAL, AppleColor.RED -> AppleColor.PINK
                AppleColor.ALPINE_GREEN, AppleColor.MIDNIGHT_GREEN -> AppleColor.GREEN
                else -> color
            }
        }

        else -> AppleColor.DEFAULT
    }
}

private fun AppleColor.mapIncorrect(): AppleColor {
    return when (this) {
        AppleColor.GREY -> AppleColor.GRAY
        AppleColor.SPACE_GREY -> AppleColor.SPACE_GRAY
        else -> this
    }
}