package recognitioncommons.models.apple.macbook

enum class MacbookMemory(
    val presentValue: Int,
    val gbValue: Int,
) {
    GB_256(256, 256),
    GB_512(512, 512),
    TB_1(1, 1024),
    TB_2(2, 2048),
    TB_4(4, 4096),
    TB_8(8, 8192),
    ;

    override fun toString(): String {
        return if (this in tbSet) {
            "${this.presentValue} ТБ"
        } else {
            "${this.gbValue} ГБ"
        }
    }
}

val tbSet = setOf(
    MacbookMemory.TB_1,
    MacbookMemory.TB_2,
    MacbookMemory.TB_4,
    MacbookMemory.TB_8,
)
