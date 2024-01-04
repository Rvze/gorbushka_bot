package com.tsypk.coresupplier.controller.dto.airpods

import com.fasterxml.jackson.annotation.JsonProperty
import recognitioncommons.models.Money
import recognitioncommons.models.country.Country

data class SupplierAirPodsDto(
    @field:JsonProperty("id", required = true) val id: String,
    @field:JsonProperty("supplier_id", required = true) val supplierId: Long,
    @field:JsonProperty("country", required = true) val country: Country,
    @field:JsonProperty("money", required = true) val money: Money,
)
