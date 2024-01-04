package recognitioncommons.service.recognition.apple.macbook

import org.springframework.stereotype.Service
import recognitioncommons.models.apple.macbook.Macbook
import recognitioncommons.models.apple.macbook.MacbookSearchModel
import recognitioncommons.models.country.Country
import recognitioncommons.service.recognition.common.recognizeCountries

@Service
class MacbookRecognitionService {
    companion object {
        private val macbookWords = setOf(
            "Macbook", "Mac", "Mac book",
            "Макбук", "Мак бук", "Мак", "Маки",
            "MacBook", "macbook"
        )
    }

    fun isMacbookWord(line: String): Boolean {
        val countiesAndFlags = recognizeCountries(line)
        val splitted = countiesAndFlags.second.split(" ")
        splitted.forEach {
            if (macbookWords.contains(it) || serlialNumbers.keys.contains(it))
                return true
        }
        return false
    }

    fun recognize(line: String): List<Macbook> {
        return recognizeMacbooks(line)
    }

    fun containsMacbookInLine(line: String): Boolean {
        return isMacbookWord(line)
    }

    fun recognizeSearchModel(line: String): List<MacbookSearchModel> {
        return recognizeMacbookSearchModel(line)
    }
}