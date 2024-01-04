package recognitioncommons.service.recognition.apple.iphone

import recognitioncommons.exception.IphoneColorIsIncorrectException
import recognitioncommons.models.apple.AppleColor
import recognitioncommons.models.apple.iphone.IphoneModel
import recognitioncommons.util.isValidColor

fun mapColor(model: IphoneModel, color: AppleColor): AppleColor {
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

    throw IphoneColorIsIncorrectException(model, actualColor)
}

private fun AppleColor.mapIncorrect(): AppleColor {
    return when (this) {
        AppleColor.GREY -> AppleColor.GRAY
        AppleColor.SPACE_GREY -> AppleColor.SPACE_GRAY
        else -> this
    }
}


private fun mapToRealColor(model: IphoneModel, color: AppleColor): AppleColor {
    return when (model) {
        IphoneModel.IPHONE_14_PRO_MAX, IphoneModel.IPHONE_14_PRO -> {
            when (color) {
                AppleColor.WHITE, AppleColor.STARLIGHT -> AppleColor.SILVER
                AppleColor.GRAPHITE, AppleColor.MIDNIGHT, AppleColor.BLACK, AppleColor.SPACE_GRAY, AppleColor.GRAY -> AppleColor.SPACE_BLACK
                AppleColor.PURPLE -> AppleColor.DEEP_PURPLE
                else -> color
            }
        }

        IphoneModel.IPHONE_14_PLUS, IphoneModel.IPHONE_14 -> {
            when (color) {
                AppleColor.WHITE, AppleColor.SILVER -> AppleColor.STARLIGHT
                AppleColor.GRAPHITE, AppleColor.SPACE_BLACK, AppleColor.BLACK, AppleColor.SPACE_GRAY, AppleColor.GRAY -> AppleColor.MIDNIGHT
                AppleColor.DEEP_PURPLE -> AppleColor.PURPLE
                else -> color
            }
        }

        IphoneModel.IPHONE_SE_3 -> {
            when (color) {
                AppleColor.WHITE, AppleColor.SILVER -> AppleColor.STARLIGHT
                AppleColor.GRAPHITE, AppleColor.SPACE_BLACK, AppleColor.BLACK, AppleColor.SPACE_GRAY, AppleColor.GRAY -> AppleColor.MIDNIGHT
                else -> color
            }
        }

        IphoneModel.IPHONE_13_PRO_MAX, IphoneModel.IPHONE_13_PRO -> {
            when (color) {
                AppleColor.WHITE, AppleColor.STARLIGHT -> AppleColor.SILVER
                AppleColor.MIDNIGHT, AppleColor.SPACE_BLACK, AppleColor.BLACK, AppleColor.SPACE_GRAY, AppleColor.GRAY -> AppleColor.GRAPHITE
                AppleColor.SKY_BLUE, AppleColor.BLUE, AppleColor.PACIFIC_BLUE -> AppleColor.SIERRA_BLUE
                AppleColor.GREEN, AppleColor.MIDNIGHT_GREEN -> AppleColor.ALPINE_GREEN
                else -> color
            }
        }

        IphoneModel.IPHONE_13, IphoneModel.IPHONE_13_MINI -> {
            when (color) {
                AppleColor.WHITE, AppleColor.SILVER -> AppleColor.STARLIGHT
                AppleColor.GRAPHITE, AppleColor.SPACE_BLACK, AppleColor.BLACK, AppleColor.SPACE_GRAY, AppleColor.GRAY -> AppleColor.MIDNIGHT
                AppleColor.SKY_BLUE, AppleColor.SIERRA_BLUE, AppleColor.PACIFIC_BLUE -> AppleColor.BLUE
                AppleColor.ALPINE_GREEN, AppleColor.MIDNIGHT_GREEN -> AppleColor.GREEN
                else -> color
            }
        }

        IphoneModel.IPHONE_12_PRO_MAX, IphoneModel.IPHONE_12_PRO -> {
            when (color) {
                AppleColor.WHITE, AppleColor.STARLIGHT -> AppleColor.SILVER
                AppleColor.MIDNIGHT, AppleColor.SPACE_BLACK, AppleColor.BLACK, AppleColor.SPACE_GRAY, AppleColor.GRAY -> AppleColor.GRAPHITE
                AppleColor.SKY_BLUE, AppleColor.SIERRA_BLUE, AppleColor.BLUE -> AppleColor.PACIFIC_BLUE
                else -> color
            }
        }

        IphoneModel.IPHONE_12, IphoneModel.IPHONE_12_MINI -> {
            when (color) {
                AppleColor.SILVER, AppleColor.STARLIGHT -> AppleColor.WHITE
                AppleColor.MIDNIGHT, AppleColor.SPACE_BLACK, AppleColor.GRAPHITE, AppleColor.SPACE_GRAY, AppleColor.GRAY -> AppleColor.BLACK
                AppleColor.SKY_BLUE, AppleColor.SIERRA_BLUE, AppleColor.PACIFIC_BLUE -> AppleColor.BLUE
                AppleColor.DEEP_PURPLE -> AppleColor.PURPLE
                AppleColor.ALPINE_GREEN, AppleColor.MIDNIGHT_GREEN -> AppleColor.GREEN
                else -> color
            }
        }

        IphoneModel.IPHONE_SE_2 -> {
            when (color) {
                AppleColor.SILVER, AppleColor.STARLIGHT -> AppleColor.WHITE
                AppleColor.MIDNIGHT, AppleColor.SPACE_BLACK, AppleColor.GRAPHITE, AppleColor.SPACE_GRAY, AppleColor.GRAY -> AppleColor.BLACK
                else -> color
            }
        }

        IphoneModel.IPHONE_11_PRO_MAX, IphoneModel.IPHONE_11_PRO -> {
            when (color) {
                AppleColor.WHITE, AppleColor.STARLIGHT -> AppleColor.SILVER
                AppleColor.MIDNIGHT, AppleColor.SPACE_BLACK, AppleColor.GRAPHITE, AppleColor.BLACK, AppleColor.GRAY -> AppleColor.SPACE_GRAY
                AppleColor.ALPINE_GREEN, AppleColor.GREEN -> AppleColor.MIDNIGHT_GREEN
                else -> color
            }
        }

        IphoneModel.IPHONE_11 -> {
            when (color) {
                AppleColor.SILVER, AppleColor.STARLIGHT -> AppleColor.WHITE
                AppleColor.MIDNIGHT, AppleColor.SPACE_BLACK, AppleColor.GRAPHITE, AppleColor.SPACE_GRAY, AppleColor.GRAY -> AppleColor.BLACK
                AppleColor.DEEP_PURPLE -> AppleColor.PURPLE
                AppleColor.ALPINE_GREEN, AppleColor.MIDNIGHT_GREEN -> AppleColor.GREEN
                else -> color
            }
        }
    }
}
