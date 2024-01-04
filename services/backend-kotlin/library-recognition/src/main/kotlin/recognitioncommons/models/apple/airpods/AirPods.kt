package recognitioncommons.models.apple.airpods

import recognitioncommons.models.apple.AppleColor
import recognitioncommons.models.country.Country

data class AirPods(
    var model: AirPodsModel,
    var color: AppleColor,
    var price: Int,
    var country: Country,
    var supplierUsername: String? = null,
    var supplierId: Long? = null,
)
