package recognitioncommons.service.recognition.apple.airpods

import org.springframework.stereotype.Service
import recognitioncommons.models.apple.airpods.AirPods
import recognitioncommons.models.apple.airpods.AirPodsSearchModel

@Service
class AirPodsRecognitionService {

    companion object {
        private val airPodsWords = setOf(
            "AirPods", "Pods", "Air Pods",
            "Аирподс", "Аир подс", "Аирподсы", "Аир подсы",
            "Эирподсы", "Эир подсы", "Эирподс", "Эир подс",
            "Подсы",
        )

        fun isAirPodsWord(line: String): Boolean {
            airPodsWords.forEach {
                if (it.equals(line, true)) {
                    return true
                }
            }
            return false
        }
    }

    fun recognize(line: String): List<AirPods> {
        return recognizeAirPods(line)
    }

    fun recognizeSearchModel(line: String): List<AirPodsSearchModel> {
        return recognizeAirPodsSearchModels(line)
    }

    fun containsAirPodsInLine(line: String): Boolean {
        return isAirpodsInLine(line)
    }
}
