package recognitioncommons.models.apple.macbook

import recognitioncommons.models.country.Country
import recognitioncommons.models.apple.AppleColor

data class Macbook(
    val model: MacbookModel,
    val ram: MacbookRam,
    val memory: MacbookMemory,
    val color: AppleColor,
    val price: Int,
    var country: Country,
    var supplierUsername: String? = null,
    var supplierId: Long? = null,
)
