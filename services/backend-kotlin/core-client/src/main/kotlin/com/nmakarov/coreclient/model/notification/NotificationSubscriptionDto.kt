package com.nmakarov.coreclient.model.notification

import com.fasterxml.jackson.annotation.JsonProperty

data class NotificationSubscriptionDto(
    @JsonProperty(value = "model_id")
    val modelId: String,
    @JsonProperty(value = "type")
    val type: NotificationType
)
