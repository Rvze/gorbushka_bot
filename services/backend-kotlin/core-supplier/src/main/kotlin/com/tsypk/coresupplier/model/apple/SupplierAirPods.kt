package com.tsypk.coresupplier.model.apple

import com.tsypk.coresupplier.util.airPodsFullModelFromId
import recognitioncommons.models.apple.airpods.AirPodsFullModel
import recognitioncommons.models.country.Country

import recognitioncommons.util.idString
import java.math.BigDecimal
import java.time.Instant

data class SupplierAirPods(
    val id: String,
    val supplierId: Long,
    val airPodsFullModel: AirPodsFullModel = airPodsFullModelFromId(id),
    val country: Country,
    val priceAmount: BigDecimal,
    val priceCurrency: String = "RUB",
) {
    constructor(
        supplierId: Long,
        country: Country,
        priceAmount: BigDecimal,
        priceCurrency: String = "RUB",
        airPodsFullModel: AirPodsFullModel
    ) : this(
        supplierId = supplierId,
        id = airPodsFullModel.idString(),
        country = country,
        priceAmount = priceAmount,
        priceCurrency = priceCurrency,
        airPodsFullModel = airPodsFullModel
    )

    val modifiedAt: Instant = Instant.now()
}
