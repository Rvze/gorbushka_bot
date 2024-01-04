package com.tsypk.coresupplier.controller.dto.macbook

import com.fasterxml.jackson.annotation.JsonProperty
import recognitioncommons.models.apple.AppleColor
import recognitioncommons.models.apple.macbook.MacbookMemory
import recognitioncommons.models.apple.macbook.MacbookModel
import recognitioncommons.models.apple.macbook.MacbookRam
import recognitioncommons.models.apple.macbook.MacbookScreen
import recognitioncommons.models.country.Country

data class MacbookFindBestRequest(
    @field:JsonProperty("model", required = true)
    val model: MacbookModel?,

    @field:JsonProperty("ram", required = true)
    val ram: MacbookRam?,

    @field:JsonProperty("memory", required = true)
    val memory: MacbookMemory?,

    @field:JsonProperty("color", required = true)
    val color: AppleColor?,

    @field:JsonProperty("country", required = false)
    val country: Country?

)
