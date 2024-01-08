package com.nmakarov.coresupplier.controller.dto.iphone

import com.fasterxml.jackson.annotation.JsonProperty
import recognitioncommons.models.apple.AppleColor
import recognitioncommons.models.apple.iphone.IphoneMemory
import recognitioncommons.models.apple.iphone.IphoneModel
import recognitioncommons.models.country.Country

data class IphonesFindBestRequest(
    @field:JsonProperty("model", required = false) val model: IphoneModel?,
    @field:JsonProperty("memory", required = false) val memory: IphoneMemory?,
    @field:JsonProperty("color", required = false) val color: AppleColor?,
    @field:JsonProperty("country", required = false) val country: Country?,
)
