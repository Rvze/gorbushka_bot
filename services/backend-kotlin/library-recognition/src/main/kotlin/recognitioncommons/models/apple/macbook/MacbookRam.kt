package recognitioncommons.models.apple.macbook

enum class MacbookRam(
    val gbValue: Int,
) {
    GB_8(8),
    GB_16(16),
    GB_24(24),
    GB_32(32),
    GB_64(64),
    GB_96(96),
    ;

    override fun toString(): String {
        return "${this.gbValue} ГБ"
    }
}
