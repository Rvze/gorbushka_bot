package com.tsypk.coresupplier.model.apple

import recognitioncommons.models.country.Country
import java.math.BigDecimal
import java.sql.Timestamp
import java.time.Instant

data class SupplierMacbook(
    var macId: String,
    var supplierId: Long,
    var country: Country,
    var priceAmount: BigDecimal,
    var priceCurrency: String = "RUB"
) {
    val modifiedAt: Instant = Instant.now()
}