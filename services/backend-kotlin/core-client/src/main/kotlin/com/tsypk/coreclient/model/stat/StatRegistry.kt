package com.tsypk.coreclient.model.stat

data class StatRegistry(
    val commonStat: CommonStat = CommonStat(),
    val supplierStat: SupplierStat = SupplierStat(),
    val resellerStat: ResellerStat = ResellerStat(),
) {
    override fun toString(): String {
        return """
            |Общая статистика:
            |$commonStat
            |
            |Статистика перекупщиков:
            |$resellerStat
            |
            |Статистика поставщиков:
            |$supplierStat
        """.trimMargin()
    }

    fun toStringHtml(): String {
        return """
            |<u><b>Общая статистика:</b></u>
            |$commonStat
            |
            |<u><b>Статистика перекупщиков:</b></u>
            |$resellerStat
            |
            |<u><b>Статистика поставщиков:</b></u>
            |$supplierStat
        """.trimMargin()
    }
}
