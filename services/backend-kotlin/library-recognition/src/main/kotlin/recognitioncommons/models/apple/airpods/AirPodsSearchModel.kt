package recognitioncommons.models.apple.airpods

import recognitioncommons.models.apple.AppleColor
import recognitioncommons.models.country.Country
import recognitioncommons.util.Presentation.AirPodsPresentation.toHumanReadableString

data class AirPodsSearchModel(
    val model: AirPodsModel,
    val color: AppleColor?,
    val country: Country? = null
) {
    override fun toString(): String {
        val builder = StringBuilder()
        builder.append(model.toHumanReadableString())
        if (color != AppleColor.DEFAULT) {
            color?.let { builder.append(" $color") }
        }
        return builder.toString()
    }
}
