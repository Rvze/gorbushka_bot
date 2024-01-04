package recognitioncommons.util.extractor

import recognitioncommons.exception.InvalidIphoneIdException
import recognitioncommons.models.apple.AppleColor
import recognitioncommons.models.apple.iphone.IphoneFullModel
import recognitioncommons.models.apple.iphone.IphoneMemory
import recognitioncommons.models.apple.iphone.IphoneModel

fun iphoneFullModelFromId(id: String): IphoneFullModel {
    val parts = id.split("/")
    if (parts.size != 3) {
        throw InvalidIphoneIdException(id)
    }

    return try {
        IphoneFullModel(
            model = IphoneModel.valueOf(parts[0]),
            memory = IphoneMemory.valueOf(parts[1]),
            color = AppleColor.valueOf(parts[2]),
        )
    } catch (e: Exception) {
        throw InvalidIphoneIdException(id)
    }
}
