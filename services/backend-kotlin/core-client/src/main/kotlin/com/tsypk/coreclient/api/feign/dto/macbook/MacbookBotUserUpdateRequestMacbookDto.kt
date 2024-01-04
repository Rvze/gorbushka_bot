package com.tsypk.coreclient.api.feign.dto.macbook

import com.fasterxml.jackson.annotation.JsonProperty
import recognitioncommons.models.Money
import recognitioncommons.models.country.Country

data class MacbookBotUserUpdateRequestMacbookDto(
    @field:JsonProperty("id")
    val id: String,
    @field:JsonProperty("country")
    val country: Country,
    @field:JsonProperty("money")
    val money: Money
) {
}