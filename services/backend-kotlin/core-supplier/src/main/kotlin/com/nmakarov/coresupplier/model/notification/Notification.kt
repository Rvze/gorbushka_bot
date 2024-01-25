package com.nmakarov.coresupplier.model.notification

import com.nmakarov.coresupplier.controller.dto.notification.NotificationDto
import java.time.Instant

data class Notification(
    val id: Long,
    val userId: Long,
    val subscriptionId: Long,
    val text: String,
    var status: NotificationStatus,
    var readAt: Instant? = null,
    val createdAt: Instant = Instant.now(),
)

fun Notification.toDto(): NotificationDto = NotificationDto(text = this.text, status = this.status)