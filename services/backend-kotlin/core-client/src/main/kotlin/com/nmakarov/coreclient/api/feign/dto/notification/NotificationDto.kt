package com.nmakarov.coreclient.api.feign.dto.notification

import com.fasterxml.jackson.annotation.JsonProperty

data class NotificationDto(
    @JsonProperty(value = "status")
    val status: NotificationStatus,
    @JsonProperty(value = "text")
    val text: String,
)