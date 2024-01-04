package recognitioncommons.service.recognition.apple.macbook

import recognitioncommons.exception.*
import recognitioncommons.models.Locale
import recognitioncommons.models.apple.AppleColor
import recognitioncommons.models.apple.macbook.MacbookMemory
import recognitioncommons.models.apple.macbook.MacbookModel
import recognitioncommons.models.apple.macbook.MacbookRam
import recognitioncommons.models.apple.macbook.tbSet
import recognitioncommons.service.recognition.apple.macbook.registry.MacbookColorSearchParts
import recognitioncommons.service.recognition.apple.macbook.registry.MacbookModelSearchParts
import recognitioncommons.service.search.AnyMatchSearchPart
import recognitioncommons.service.search.RegexSearchPart
import recognitioncommons.util.isValidMemory

/**
 * MACBOOK RAM RECOGNITION
 */

private val POSSIBLE_RAMS = MacbookRam.values().toList()
private val RAMS_SEARCH_PART = AnyMatchSearchPart(
    searchParts = setOf(RegexSearchPart(Regex(pattern = "(8|16|32|64|96)", option = RegexOption.IGNORE_CASE)))
)

fun recognizeRam(input: String): Pair<MacbookRam, String> {
    for (memory in POSSIBLE_RAMS.reversed()) {
        val toFind = memory.gbValue.toString()
        if (input.findLast { input.contains(toFind) } != null) {
            return memory to input.replace(toFind, " ")
        }
    }

    throw MacbookRamIsAbsentException(input)
}

/**
 * MACBOOK MEMORY RECOGNITION
 */
private val POSSIBLE_MEMORIES = MacbookMemory.values().toList()
private val TB_MEMORY_SEARCH_PART = AnyMatchSearchPart(
    searchParts = setOf(
        RegexSearchPart(Regex(pattern = "(1) *(TB|ТБ|Т|T)", option = RegexOption.IGNORE_CASE)),
        RegexSearchPart(Regex(pattern = "(2) *(TB|ТБ|Т|T)", option = RegexOption.IGNORE_CASE)),
        RegexSearchPart(Regex(pattern = "(4) *(TB|ТБ|Т|T)", option = RegexOption.IGNORE_CASE)),
        RegexSearchPart(Regex(pattern = "(8) *(TB|ТБ|Т|T)", option = RegexOption.IGNORE_CASE)),
        RegexSearchPart(Regex(pattern = "(1024|2048|4096|8192)", option = RegexOption.IGNORE_CASE)),
    )
)

fun recognizeMemory(input: String): Pair<MacbookMemory, String> {
    val gbStr = TB_MEMORY_SEARCH_PART.containsAndReturn(input)
    for (memory in POSSIBLE_MEMORIES) {
        if (memory in tbSet) {
            try {
                if (gbStr != null && gbStr.contains(memory.toString())) {
                    val cut = TB_MEMORY_SEARCH_PART.findAndCut(input)
                    return memory to cut
                }
            } catch (e: SearchException) {
                continue
            }
        } else {
            val toFind = memory.presentValue.toString()
            if (input.findLast { input.contains(toFind) } != null) {
                return memory to input.replace(toFind, " ")
            }
        }
    }

    throw MacbookMemoryIsAbsentException(input)
}

/**
 * MACBOOK COLOR RECOGNITION
 */
fun recognizeColor(
    input: String,
    withRussianLocale: Boolean = false,
): Pair<AppleColor, String> {
    if (!withRussianLocale) {
        for (sp in MacbookColorSearchParts.MACBOOK_COLOR_SEARCH_PARTS) {
            if (sp.second.containsLocale(input, Locale.EN)) {
                return sp.first to sp.second.findAndCutLocale(input, Locale.EN)
            }
        }
    } else {
        for (sp in MacbookColorSearchParts.MACBOOK_COLOR_SEARCH_PARTS) {
            if (sp.second.contains(input)) {
                return sp.first to sp.second.findAndCut(input)
            }
        }
    }
    throw MacbookColorIsAbsentException(input)
}

/**
 * MACBOOK MODEL RECOGNITION
 */
fun recognizeModel(
    input: String,
    withRussianLocale: Boolean = false,
): Pair<MacbookModel, String> {
    if (!withRussianLocale) {
        for (sp in MacbookModelSearchParts.MACBOOK_MODEL_SEARCH_PARTS_NO_MACBOOK) {
            try {
                if (sp.second.containsLocale(input, Locale.EN)) {
                    return sp.first to sp.second.findAndCutLocale(input, Locale.EN)
                }
            } catch (e: SearchException) {
                continue
            }
        }
    } else {
        for (sp in MacbookModelSearchParts.MACBOOK_MODEL_SEARCH_PARTS_NO_MACBOOK) {
            try {
                if (sp.second.contains(input)) {
                    return sp.first to sp.second.findAndCut(input)
                }
            } catch (e: SearchException) {
                continue
            }
        }
    }
    throw MacbookModelIsAbsentException(input)
}

fun assertMacbookMemory(model: MacbookModel, memory: MacbookMemory) {
    if (!model.isValidMemory(memory)) {
        throw MacbookMemoryIsIncorrectException(model, memory)
    }
}
