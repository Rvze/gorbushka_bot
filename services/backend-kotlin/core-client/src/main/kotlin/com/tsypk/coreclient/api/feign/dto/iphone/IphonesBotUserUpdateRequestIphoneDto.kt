package com.tsypk.coreclient.api.feign.dto.iphone

import com.fasterxml.jackson.annotation.JsonProperty
import recognitioncommons.models.Money
import recognitioncommons.models.country.Country

data class IphonesBotUserUpdateRequestIphoneDto(
    @field:JsonProperty("id", required = true) val id: String,
    @field:JsonProperty("country", required = true) val country: Country,
    @field:JsonProperty("money", required = true) val money: Money,
)
