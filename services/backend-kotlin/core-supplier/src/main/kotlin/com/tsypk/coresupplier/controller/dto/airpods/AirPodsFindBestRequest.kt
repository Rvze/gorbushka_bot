package com.tsypk.coresupplier.controller.dto.airpods

import com.fasterxml.jackson.annotation.JsonProperty
import recognitioncommons.models.apple.AppleColor
import recognitioncommons.models.apple.airpods.AirPodsModel
import recognitioncommons.models.country.Country

data class AirPodsFindBestRequest(
    @field:JsonProperty("model", required = false) val model: AirPodsModel?,
    @field:JsonProperty("color", required = false) val color: AppleColor?,
    @field:JsonProperty("country", required = false) val country: Country?,
)
