package recognitioncommons.models.apple.iphone

enum class IphoneMemory(
    var order: Int,
    var presentValue: Int,
    var gbValue: Int,
) {
    GB_32(60, 32, 32),
    GB_64(70, 64, 64),
    GB_128(80, 128, 128),
    GB_256(90, 256, 256),
    GB_512(100, 512, 512),
    TB_1(50, 1, 1024),
    ;

    override fun toString(): String {
        return if (this == TB_1) {
            "1 ТБ"
        } else {
            "${this.gbValue} ГБ"
        }
    }
}
