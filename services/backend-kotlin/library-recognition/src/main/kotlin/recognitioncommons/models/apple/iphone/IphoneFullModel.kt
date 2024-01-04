package recognitioncommons.models.apple.iphone

import recognitioncommons.models.apple.AppleColor
import recognitioncommons.models.country.Country

data class IphoneFullModel(
    var model: IphoneModel,
    var memory: IphoneMemory,
    var color: AppleColor,
    var country: Country? = null,
) {
    override fun toString(): String {
        return "${model.name} ${memory.name} ${color.name} ${country?.name}"
    }
}
