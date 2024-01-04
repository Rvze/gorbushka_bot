package recognitioncommons.models.apple.iphone

import recognitioncommons.models.apple.AppleColor
import recognitioncommons.models.country.Country
import recognitioncommons.util.Presentation.IphonePresentation.toHumanReadableString
import recognitioncommons.util.Presentation.toHumanReadableString

data class IphoneSearchModel(
    var model: IphoneModel,
    var memory: IphoneMemory?,
    var color: AppleColor?,
    var country: Country? = null,
) {
    override fun toString(): String {
        val builder = StringBuilder()
        builder.append(model.toHumanReadableString())
        memory?.let { builder.append(" ${memory.toString()}") }
        color?.let { builder.append(" ${color.toString()}") }
        country?.let { builder.append(" ${country.toHumanReadableString()}") }
        return builder.toString()
    }
}
