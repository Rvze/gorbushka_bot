package recognitioncommons.service.recognition.apple.macbook

import recognitioncommons.exception.MacbookColorIsIncorrectException
import recognitioncommons.models.apple.AppleColor
import recognitioncommons.models.apple.macbook.MacbookModel
import recognitioncommons.util.isValidColor

fun mapColor(model: MacbookModel, color: AppleColor): AppleColor {
    if (model.isValidColor(color)) {
        return color
    }

    val notIncorrectColor = color.mapIncorrect()
    if (model.isValidColor(color)) {
        return notIncorrectColor
    }

    val actualColor = mapToRealColor(model, notIncorrectColor)
    if (model.isValidColor(actualColor)) {
        return actualColor
    }

    throw MacbookColorIsIncorrectException(model, actualColor)
}

private fun AppleColor.mapIncorrect(): AppleColor {
    return when (this) {
        AppleColor.GREY -> AppleColor.GRAY
        AppleColor.SPACE_GREY -> AppleColor.SPACE_GRAY
        else -> this
    }
}

private fun mapToRealColor(model: MacbookModel, color: AppleColor): AppleColor {
    return when (model) {
        MacbookModel.MACBOOK_AIR_13_M1 -> {
            mapSilverAndSpaceGray(color)
        }

        MacbookModel.MACBOOK_PRO_13_M1 -> {
            mapSilverAndSpaceGray(color)
        }

        MacbookModel.MACBOOK_PRO_14_M1_PRO -> {
            mapSilverAndSpaceGray(color)
        }

        MacbookModel.MACBOOK_PRO_14_M1_MAX -> {
            mapSilverAndSpaceGray(color)
        }

        MacbookModel.MACBOOK_PRO_16_M1_PRO -> {
            mapSilverAndSpaceGray(color)
        }

        MacbookModel.MACBOOK_PRO_16_M1_MAX -> {
            mapSilverAndSpaceGray(color)
        }

        MacbookModel.MACBOOK_AIR_13_M2 -> {
            color
        }

        MacbookModel.MACBOOK_PRO_13_M2 -> {
            mapSilverAndSpaceGray(color)
        }

        MacbookModel.MACBOOK_PRO_14_M2_PRO -> {
            mapSilverAndSpaceGray(color)
        }

        MacbookModel.MACBOOK_PRO_14_M2_MAX -> {
            mapSilverAndSpaceGray(color)
        }

        MacbookModel.MACBOOK_PRO_16_M2_PRO -> {
            mapSilverAndSpaceGray(color)
        }

        MacbookModel.MACBOOK_PRO_16_M2_MAX -> {
            mapSilverAndSpaceGray(color)
        }
    }
}

private fun mapSilverAndSpaceGray(color: AppleColor): AppleColor {
    return when (color) {
        AppleColor.WHITE, AppleColor.STARLIGHT -> AppleColor.SILVER
        AppleColor.GRAPHITE, AppleColor.MIDNIGHT, AppleColor.BLACK, AppleColor.SPACE_BLACK, AppleColor.GRAY -> AppleColor.SPACE_GRAY
        else -> color
    }
}
