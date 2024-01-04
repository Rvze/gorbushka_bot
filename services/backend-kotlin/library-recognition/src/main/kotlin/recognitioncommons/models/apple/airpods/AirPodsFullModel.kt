package recognitioncommons.models.apple.airpods

import recognitioncommons.models.apple.AppleColor
import recognitioncommons.models.country.Country

data class AirPodsFullModel(
    var model: AirPodsModel,
    var color: AppleColor,
    var country: Country? = null
) {
    override fun toString(): String {
        return "${model.name} ${color.name} ${country?.name}"
    }
}
