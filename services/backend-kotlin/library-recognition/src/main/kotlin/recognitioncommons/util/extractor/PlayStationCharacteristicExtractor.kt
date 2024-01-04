package recognitioncommons.util.extractor

import recognitioncommons.exception.PlaystationInvalidIdException
import recognitioncommons.models.sony.PlayStationFullModel
import recognitioncommons.models.sony.PlaystationEdition
import recognitioncommons.models.sony.PlaystationModel
import recognitioncommons.models.sony.PlaystationRevision

fun playStationFullModelFromId(input: String): PlayStationFullModel {
    val parts = input.split("/")
    if (parts.size != 4) {
        throw PlaystationInvalidIdException(input)
    }
    return try {
        PlayStationFullModel(
            model = PlaystationModel.valueOf(parts[0]),
            edition = PlaystationEdition.valueOf(parts[1]),
            revision = PlaystationRevision.valueOf(parts[3]),
            year = parts[2].toInt()
        )
    } catch (e: Exception) {
        throw PlaystationInvalidIdException(input)
    }
}
