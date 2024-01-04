package recognitioncommons.models.apple.macbook

import recognitioncommons.models.apple.AppleColor
import recognitioncommons.models.country.Country

data class MacbookSerialNumber(
    val macbookModel: MacbookModel,
    val macbookRam: MacbookRam,
    val macbookMemory: MacbookMemory,
    val color: AppleColor
) {
}