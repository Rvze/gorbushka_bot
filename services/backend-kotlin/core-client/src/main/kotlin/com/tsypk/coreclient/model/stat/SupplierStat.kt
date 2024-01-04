package com.tsypk.coreclient.model.stat

data class SupplierStat(
    var supplierMessages: Int = 0,

    var supplierPriceUpdates: Int = 0,
    var suppliersAddingIphones: MutableSet<Long> = mutableSetOf(),

    var iphonesRecognized: Int = 0,
    var iphonesRecognizeErrors: Int = 0,
) {
    override fun toString(): String {
        return """
            |Сообщений от поставщиков: $supplierMessages
            |Обновлений прайс-листа: $supplierPriceUpdates
            |Поставщиков обновили прайс-лист: ${suppliersAddingIphones.size}
            |Айфонов распознали: $iphonesRecognized
            |Ошибок при распознавании айфонов: $iphonesRecognizeErrors
        """.trimMargin()
    }
}
