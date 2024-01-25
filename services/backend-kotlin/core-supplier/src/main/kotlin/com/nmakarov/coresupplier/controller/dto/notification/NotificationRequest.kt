package com.nmakarov.coresupplier.controller.dto.notification

import com.fasterxml.jackson.annotation.JsonProperty

data class NotificationRequest(
    @JsonProperty("action_type")
    val actionType: NotificationActionType,
    @JsonProperty("model_id")
    val modelId: String,
)