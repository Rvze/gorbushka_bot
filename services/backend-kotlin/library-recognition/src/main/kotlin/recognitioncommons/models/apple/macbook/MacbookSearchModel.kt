package recognitioncommons.models.apple.macbook

import recognitioncommons.models.country.Country
import recognitioncommons.models.apple.AppleColor
import recognitioncommons.util.Presentation.MacbookPresentation.toHumanReadableString
import recognitioncommons.util.Presentation.toHumanReadableString

data class MacbookSearchModel(
    val model: MacbookModel,
    val ram: MacbookRam?,
    val memory: MacbookMemory?,
    val color: AppleColor?,
    var country: Country? = null,
) {
    override fun toString(): String {
        val builder = StringBuilder()
        builder.append(model.toHumanReadableString())
        ram?.let { builder.append(" $ram") }
        memory?.let { builder.append(" $memory") }
        color?.let { builder.append(" $color") }
        country?.let { builder.append(" ${country.toHumanReadableString()}") }
        return builder.toString()
    }
}
