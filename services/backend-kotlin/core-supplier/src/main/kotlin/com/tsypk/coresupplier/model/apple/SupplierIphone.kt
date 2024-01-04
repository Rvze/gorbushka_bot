package com.tsypk.coresupplier.model.apple

import com.tsypk.coresupplier.util.iphoneFullModelFromId
import recognitioncommons.models.country.Country
import recognitioncommons.models.apple.iphone.IphoneFullModel
import recognitioncommons.util.idString
import java.math.BigDecimal
import java.time.Instant

data class SupplierIphone(
    val id: String,
    val supplierId: Long,
    val iphoneFullModel: IphoneFullModel = iphoneFullModelFromId(id),
    val country: Country,
    val priceAmount: BigDecimal,
    val priceCurrency: String = "RUB",
) {
    constructor(
        iphoneFullModel: IphoneFullModel,
        supplierId: Long,
        country: Country,
        priceAmount: BigDecimal,
        priceCurrency: String = "RUB",
    ) : this(
        id = iphoneFullModel.idString(),
        supplierId = supplierId,
        iphoneFullModel = iphoneFullModel,
        country = country,
        priceAmount = priceAmount,
        priceCurrency = priceCurrency,
    )

    val modifiedAt: Instant = Instant.now()
}
