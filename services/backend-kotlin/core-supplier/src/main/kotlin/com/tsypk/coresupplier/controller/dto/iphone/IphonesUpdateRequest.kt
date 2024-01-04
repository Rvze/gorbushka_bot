package com.tsypk.coresupplier.controller.dto.iphone

import com.fasterxml.jackson.annotation.JsonProperty

data class IphonesUpdateRequest(
    @field:JsonProperty("supplier_id") val supplierId: Long,
    @field:JsonProperty("iphones") val iphones: List<IphoneDto>,
)
