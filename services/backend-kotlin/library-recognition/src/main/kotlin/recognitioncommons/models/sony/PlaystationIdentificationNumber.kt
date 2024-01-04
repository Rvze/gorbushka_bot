package recognitioncommons.models.sony

import recognitioncommons.models.country.Country
import java.lang.StringBuilder

fun getModel(revision: PlaystationRevision, edition: PlaystationEdition, country: Country?): String {
    val builder = StringBuilder()
    if (edition == PlaystationEdition.DIGITAL_EDITION) builder.append("0") else builder.append("1")
    when (revision) {
        PlaystationRevision.FIRST_REVISION -> builder.append("0")
        PlaystationRevision.SECOND_REVISION -> builder.append("1")
        else -> builder.append("2")
    }
    builder.append(findRegionByCountry(country))
    return builder.toString()
}