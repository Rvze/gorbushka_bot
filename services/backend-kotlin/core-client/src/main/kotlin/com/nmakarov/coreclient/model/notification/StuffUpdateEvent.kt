package com.nmakarov.coreclient.model.notification

import com.fasterxml.jackson.annotation.JsonProperty

enum class StuffUpdateType {
    UPDATE,
    CREATE,
    DELETE
}

data class PriceUpdate(
    @JsonProperty("old_price")
    val oldPrice: Long,
    @JsonProperty("new_price")
    val newPrice: Long,
)

data class StuffUpdateEvent(
    @JsonProperty("type")
    val type: StuffUpdateType,
    @JsonProperty("model_id")
    val modelId: String,
    @JsonProperty("payload")
    val payload: PriceUpdate? = null,
)

data class StuffUpdateBatchEvent(
    @JsonProperty("updates")
    val updates: List<StuffUpdateEvent>,
)
