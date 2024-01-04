package com.tsypk.coresupplier.controller.dto.airpods

import com.fasterxml.jackson.annotation.JsonProperty

data class AirPodsUpdateRequest(
    @field:JsonProperty("supplier_id") val supplierId: Long,
    @field:JsonProperty("airpods") val airpods: List<AirPodsDto>
) {
}