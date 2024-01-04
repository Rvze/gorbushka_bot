package recognitioncommons.service.recognition.apple.airpods

import recognitioncommons.exception.AirPodsColorIsIncorrectException
import recognitioncommons.exception.AirPodsModelIsAbsentException
import recognitioncommons.models.Locale
import recognitioncommons.models.apple.AppleColor
import recognitioncommons.models.apple.airpods.AirPodsModel
import recognitioncommons.service.recognition.apple.airpods.registry.AirPodsColorSearchParts
import recognitioncommons.service.recognition.apple.airpods.registry.AirPodsModelSearchParts
import recognitioncommons.util.isValidColor

fun recognizeColor(input: String, withRuLocal: Boolean = false): Pair<AppleColor, String> {
    if (!withRuLocal) {
        for (sp in AirPodsColorSearchParts.AIRPODS_COLORS_SEARCH_PART_LIST) {
            if (sp.second.containsLocale(input, Locale.EN)) {
                return sp.first to sp.second.findAndCutLocale(input, Locale.EN)
            }
        }
    } else {
        for (sp in AirPodsColorSearchParts.AIRPODS_COLORS_SEARCH_PART_LIST) {
            if (sp.second.contains(input)) {
                return sp.first to sp.second.findAndCut(input)
            }
        }
    }

    return AppleColor.DEFAULT to input
}

fun recognizeModel(input: String, withRuLocal: Boolean = false): Pair<AirPodsModel, String> {
    if (!withRuLocal) {
        for (sp in AirPodsModelSearchParts.AIRPODS_MODEL_SEARCH_PART) {
            if (sp.second.containsLocale(input, Locale.EN)) {
                return sp.first to sp.second.findAndCutLocale(input, Locale.EN)
            }
        }
    } else {
        for (sp in AirPodsModelSearchParts.AIRPODS_MODEL_SEARCH_PART) {
            if (sp.second.contains(input)) {
                return sp.first to sp.second.findAndCut(input)
            }
        }
    }
    throw AirPodsModelIsAbsentException(input)
}

fun assertAirpodsColor(model: AirPodsModel, color: AppleColor) {
    if (!model.isValidColor(color)) {
        throw AirPodsColorIsIncorrectException(model, color)
    }
}
