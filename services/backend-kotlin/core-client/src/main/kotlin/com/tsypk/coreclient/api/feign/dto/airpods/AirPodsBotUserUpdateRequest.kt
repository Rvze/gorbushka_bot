package com.tsypk.coreclient.api.feign.dto.airpods

import com.fasterxml.jackson.annotation.JsonProperty

data class AirPodsBotUserUpdateRequest(
    @field:JsonProperty(value = "supplier_id") val supplierId : Long,
    @field:JsonProperty(value = "airpods") val airpods : List<AirPodsBotUserUpdateRequestAirpodsDto>,
)
