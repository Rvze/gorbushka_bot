package com.tsypk.coreclient.api.feign.dto.iphone

import com.fasterxml.jackson.annotation.JsonProperty

data class IphonesBotUserUpdateRequest(
    @field:JsonProperty("supplier_id") val supplierId: Long,
    @field:JsonProperty("iphones") val iphones: List<IphonesBotUserUpdateRequestIphoneDto>,
)
