package recognitioncommons.models.apple.macbook

import recognitioncommons.models.country.Country
import recognitioncommons.models.apple.AppleColor
import recognitioncommons.util.Presentation.MacbookPresentation.toHumanReadableString
import recognitioncommons.util.Presentation.toHumanReadableString

data class MacbookFullModel(
    val model: MacbookModel,
    val ram: MacbookRam,
    val memory: MacbookMemory,
    val color: AppleColor,
    var country: Country? = null,
) {
    override fun toString(): String {
        return "${model.toHumanReadableString()} $ram $memory $color ${country.toHumanReadableString()}"
    }
}
