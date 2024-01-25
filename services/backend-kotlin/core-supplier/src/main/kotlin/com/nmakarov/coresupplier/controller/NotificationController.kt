package com.nmakarov.coresupplier.controller

import com.nmakarov.coreclient.model.notification.NotificationSubscription
import com.nmakarov.coreclient.model.notification.NotificationSubscriptionDto
import com.nmakarov.coreclient.model.notification.NotificationType
import com.nmakarov.coresupplier.controller.dto.notification.NotificationActionType
import com.nmakarov.coresupplier.controller.dto.notification.NotificationDto
import com.nmakarov.coresupplier.controller.dto.notification.NotificationRequest
import com.nmakarov.coresupplier.model.notification.toDto
import com.nmakarov.coresupplier.repository.NotificationRepository
import com.nmakarov.coresupplier.repository.NotificationSubscriptionRepository
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("v1/notification")
class NotificationController(
    private val subscriptionRepository: NotificationSubscriptionRepository,
    private val notificationRepository: NotificationRepository,
) {

    @GetMapping(value = ["/list"])
    @Transactional
    fun getSupplierStuffFromId(@RequestHeader("UserID") userId: Long): ResponseEntity<List<NotificationDto>> {
        val found = notificationRepository.findAllForUser(userId)
        return ResponseEntity.ok(found.map { it.toDto() })
    }

    @GetMapping(value = ["/subscription/list"])
    fun getNotificationSubscriptions(
        @RequestHeader("UserID") userId: Long,
    ): ResponseEntity<List<NotificationSubscriptionDto>> {
        val found = subscriptionRepository.getAllForUser(userId)
        return ResponseEntity.ok(found.map { it.toDto() })
    }

    @PostMapping(
        value = ["/stuff"],
        consumes = ["application/json"],
        produces = ["application/json"]
    )
    fun addStuffForSupplier(
        @RequestHeader("UserID") userId: Long,
        @RequestBody request: NotificationRequest
    ): ResponseEntity<Unit> {
        if (request.actionType == NotificationActionType.SUBSCRIBE) {
            subscriptionRepository.create(
                notificationSubscription = NotificationSubscription(
                    userId = userId,
                    entityId = request.modelId,
                    type = NotificationType.STUFF_SUBSCRIPTION,
                )
            )
        } else {
            subscriptionRepository.delete(
                userId = userId,
                entityId = request.modelId,
                type = NotificationType.STUFF_SUBSCRIPTION,
            )
        }

        return ResponseEntity.ok(Unit)
    }
}