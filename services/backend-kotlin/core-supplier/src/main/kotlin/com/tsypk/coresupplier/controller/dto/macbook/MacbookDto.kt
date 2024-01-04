package com.tsypk.coresupplier.controller.dto.macbook

import com.fasterxml.jackson.annotation.JsonProperty
import recognitioncommons.models.Money
import recognitioncommons.models.country.Country

data class MacbookDto(
    @field:JsonProperty("id", required = true)
    val id: String,
    @field:JsonProperty("country", required = true)
    val country: Country,
    @field:JsonProperty("money", required = true)
    val money : Money

    ) {
}