package com.tsypk.coresupplier.controller.dto.macbook

import com.fasterxml.jackson.annotation.JsonProperty

data class MacbookUpdateRequest(
    @field:JsonProperty("supplierId")
    val supplierId: Long,
    @field:JsonProperty("macbooks")
    val macbooks: List<MacbookDto>
) {
}