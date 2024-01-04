package recognitioncommons.service.recognition

import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import recognitioncommons.models.apple.AppleColor
import recognitioncommons.models.apple.macbook.MacbookFullModel
import recognitioncommons.models.apple.macbook.MacbookMemory
import recognitioncommons.models.apple.macbook.MacbookModel
import recognitioncommons.models.apple.macbook.MacbookRam

@Service
class CsvRegistryExtractor {
    data class MacbookCsvEntry(
        val partNum: String,
        val model: MacbookModel,
        val ram: MacbookModel,
        val memory: MacbookMemory,
        val color: AppleColor,
    )

    fun extractMacbooks(): List<Pair<String, MacbookFullModel>> {
        val reader = ClassPathResource("models.csv").file.bufferedReader()
        val header = reader.readLine()
        return reader.lineSequence()
            .filter { it.isNotBlank() }
            .map {
                val (partNum, model, ram, memory, color) = it.split(",", ignoreCase = false, limit = 5)
                    .map { part -> part.trim() }
                partNum to MacbookFullModel(
                    model = MacbookModel.valueOf(model),
                    ram = MacbookRam.valueOf(ram),
                    memory = MacbookMemory.valueOf(memory),
                    color = AppleColor.valueOf(color),
                )
            }.toList()
    }
}
