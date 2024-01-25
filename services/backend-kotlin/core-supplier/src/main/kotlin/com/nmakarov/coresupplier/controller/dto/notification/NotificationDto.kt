package com.nmakarov.coresupplier.controller.dto.notification

import com.fasterxml.jackson.annotation.JsonProperty
import com.nmakarov.coresupplier.model.notification.NotificationStatus

data class NotificationDto(
    @JsonProperty(value = "status")
    val status: NotificationStatus,
    @JsonProperty(value = "text")
    val text: String,
)