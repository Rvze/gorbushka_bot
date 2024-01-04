package recognitioncommons.models.apple.iphone

import recognitioncommons.models.apple.AppleColor
import recognitioncommons.models.country.Country

data class Iphone(
    var model: IphoneModel,
    var color: AppleColor,
    var memory: IphoneMemory,
    var price: Int,
    val country: Country,
    var supplierUsername: String? = null,
    var supplierId: Long? = null,
)
