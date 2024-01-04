package com.tsypk.coresupplier.model.sony

import com.tsypk.coresupplier.util.playStationFullModelFromid
import recognitioncommons.models.country.Country
import recognitioncommons.models.sony.PlayStationFullModel
import recognitioncommons.util.extractor.playStationFullModelFromId
import recognitioncommons.util.idString
import java.math.BigDecimal
import java.time.Instant

data class SupplierPlaystation(
    val id: String,
    val supplierId: Long,
    val playstationFullModel: PlayStationFullModel = playStationFullModelFromid(id),
    val country: Country,
    val priceAmount: BigDecimal,
    val priceCurrency: String = "RUB"
) {
    constructor(
        playstationFullModel: PlayStationFullModel,
        supplierId: Long,
        country: Country,
        priceCurrency: String,
        priceAmount: BigDecimal
    ) : this(
        supplierId = supplierId,
        id = playstationFullModel.idString(),
        country = country,
        priceCurrency = priceCurrency,
        priceAmount = priceAmount
    )

    var modifiedAt: Instant = Instant.now()

}