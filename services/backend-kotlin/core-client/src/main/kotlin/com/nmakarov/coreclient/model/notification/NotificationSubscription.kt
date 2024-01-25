package com.nmakarov.coreclient.model.notification

data class NotificationSubscription(
    val id: Long = 0L,
    val userId: Long,
    val entityId: String,
    val type: NotificationType,
) {
    fun toDto(): NotificationSubscriptionDto {
        return NotificationSubscriptionDto(
            modelId = entityId,
            type = type
        )
    }

}
