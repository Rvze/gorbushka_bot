package com.tsypk.coresupplier.controller.dto.playstation

import com.fasterxml.jackson.annotation.JsonProperty
import recognitioncommons.models.sony.PlaystationModel

data class PlayStationFindBestPricesDto(
    @field:JsonProperty("model", required = false) val model: PlaystationModel?,

    ) {
}