package recognitioncommons.models.apple.macbook

enum class MacbookScreen(val value: Int) {
    INCH_13(13),
    INCH_14(14),
    INCH_16(16),
    ;

    override fun toString(): String {
        return this.value.toString()
    }
}
