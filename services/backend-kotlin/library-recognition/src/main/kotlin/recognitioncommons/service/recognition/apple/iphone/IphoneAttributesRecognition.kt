package recognitioncommons.service.recognition.apple.iphone

import recognitioncommons.exception.*
import recognitioncommons.models.Locale
import recognitioncommons.models.apple.AppleColor
import recognitioncommons.models.apple.iphone.IphoneMemory
import recognitioncommons.models.apple.iphone.IphoneModel
import recognitioncommons.service.recognition.apple.iphone.registry.IphoneColorSearchPartsRegistry
import recognitioncommons.service.recognition.apple.iphone.registry.IphoneModelsSearchPartsRegistry
import recognitioncommons.service.search.AnyMatchSearchPart
import recognitioncommons.service.search.RegexSearchPart
import recognitioncommons.util.isValidMemory

/**
 * IPHONE MEMORY RECOGNITION
 */
private val POSSIBLE_MEMORIES = IphoneMemory.values().sortedByDescending { it.order }.toList()
private val TB_MEMORY_SEARCH_PART = AnyMatchSearchPart(
    searchParts = setOf(
        RegexSearchPart(Regex(pattern = "TB", option = RegexOption.IGNORE_CASE)),
        RegexSearchPart(Regex(pattern = "ТБ", option = RegexOption.IGNORE_CASE)),
        RegexSearchPart(Regex(pattern = "1 ?Т", option = RegexOption.IGNORE_CASE)),
        RegexSearchPart(Regex(pattern = "1 ?T", option = RegexOption.IGNORE_CASE)),
        RegexSearchPart(Regex(pattern = "(GB)?_? ?1024_? ?G?B?", option = RegexOption.IGNORE_CASE)),
    )
)

fun recognizeMemory(input: String): Pair<IphoneMemory, String> {
    for (memory in POSSIBLE_MEMORIES) {
        if (memory in setOf(IphoneMemory.TB_1)) {
            try {
                val cut = TB_MEMORY_SEARCH_PART.findAndCut(input)
                return memory to cut
            } catch (e: SearchException) {
                continue
            }
        }

        val toFind = memory.presentValue.toString()
        if (input.findLast { input.contains(toFind) } != null) {
            return memory to input.replace(toFind, " ")
        }
    }

    throw IphoneMemoryIsAbsentException(input)
}


/**
 * IPHONE COLOR RECOGNITION
 */
fun recognizeColor(
    input: String,
    withRussianLocale: Boolean = false,
): Pair<AppleColor, String> {
    if (!withRussianLocale) {
        for (sp in IphoneColorSearchPartsRegistry.IPHONE_COLORS_SEARCH_PARTS_LIST) {
            if (sp.second.containsLocale(input, Locale.EN)) {
                return sp.first to sp.second.findAndCutLocale(input, Locale.EN)
            }
        }
    } else {
        for (sp in IphoneColorSearchPartsRegistry.IPHONE_COLORS_SEARCH_PARTS_LIST) {
            if (sp.second.contains(input)) {
                return sp.first to sp.second.findAndCut(input)
            }
        }
    }
    throw IphoneColorIsAbsentException(input)
}


/**
 * IPHONE MODEL RECOGNITION
 */
fun recognizeModel(
    input: String,
    withRussianLocale: Boolean = false,
): Pair<IphoneModel, String> {
    if (!withRussianLocale) {
        for (sp in IphoneModelsSearchPartsRegistry.IPHONE_MODELS_SEARCH_PARTS_LIST) {
            if (sp.second.containsLocale(input, Locale.EN)) {
                return sp.first to sp.second.findAndCutLocale(input, Locale.EN)
            }
        }
    } else {
        for (sp in IphoneModelsSearchPartsRegistry.IPHONE_MODELS_SEARCH_PARTS_LIST) {
            if (sp.second.contains(input)) {
                return sp.first to sp.second.findAndCut(input)
            }
        }
    }
    throw IphoneModelIsAbsentException(input)
}

fun assertIphoneMemory(model: IphoneModel, memory: IphoneMemory) {
    if (!model.isValidMemory(memory)) {
        throw IphoneMemoryIsIncorrectException(model, memory)
    }
}
