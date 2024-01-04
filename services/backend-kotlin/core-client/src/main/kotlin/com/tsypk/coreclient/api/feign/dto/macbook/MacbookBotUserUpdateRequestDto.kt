package com.tsypk.coreclient.api.feign.dto.macbook

import com.fasterxml.jackson.annotation.JsonProperty

data class MacbookBotUserUpdateRequestDto(
    @field:JsonProperty("supplierId", required = true)
    val supplierId: Long,
    @field:JsonProperty("macbooks", required = true)
    val macbooks: List<MacbookBotUserUpdateRequestMacbookDto>
) {
}