package recognitioncommons.util.extractor

import recognitioncommons.exception.InvalidMacbookIdException
import recognitioncommons.models.apple.AppleColor
import recognitioncommons.models.apple.macbook.MacbookFullModel
import recognitioncommons.models.apple.macbook.MacbookMemory
import recognitioncommons.models.apple.macbook.MacbookModel
import recognitioncommons.models.apple.macbook.MacbookRam

fun macbookFullModelFromId(id: String): MacbookFullModel {
    val parts = id.split("/")
    if (parts.size != 4) {
        throw InvalidMacbookIdException(id)
    }

    return try {
        MacbookFullModel(
            model = MacbookModel.valueOf(parts[0]),
            ram = MacbookRam.valueOf(parts[2]),
            memory = MacbookMemory.valueOf(parts[1]),
            color = AppleColor.valueOf(parts[3]),
        )
    } catch (e: Exception) {
        throw InvalidMacbookIdException(id)
    }
}