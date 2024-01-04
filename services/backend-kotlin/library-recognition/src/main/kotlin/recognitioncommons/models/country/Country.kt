package recognitioncommons.models.country

enum class Country(
    val nameRu: String,
) {
    HONG_KONG("Гонконг"),
    JAPAN("Япония"),
    USA("США"),
    SOUTH_KOREA("Южная Корея"),
    EUROPE("Европа"),
    ENGLAND("Англия"),
    SINGAPORE("Сингапур"),
    INDIA("Индия"),
    CHINA("Китай"),
    UAE("ОАЭ"),
    RUSSIA("Россия"),
    AUSTRALIA("Австралия"),
    KAZAKHSTAN("Казахстан"),
    CANADA("Канада"),
    VIETNAM("Вьетнам"),
    UNITED_KINGDOM("Великобритания"),
    THAILAND("Таиланд"),
    MEXICO("Мексика"),
    BRAZIL("Бразилия"),
    MACAU("Макао"),
    PARAGUAY("Парагвай"),
    SAUDI_ARABIA("Саудовская Аравия"),
    NEW_ZEALAND("Новая Зеландия"),
    UKRAINE("Украина"),
    KYRGYZ_REPUBLIC("Киргизия"),
    TAJIKISTAN("Таджикистан"),
    TURKMENISTAN("Туркменистан"),
    UZBEKISTAN("Узбекистан"),
    GEORGIA("Грузия"),
    ;

    override fun toString(): String {
        return this.nameRu
    }
}
