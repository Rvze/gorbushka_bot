package com.nmakarov.coreclient.api.feign

import com.nmakarov.coreclient.api.feign.dto.notification.NotificationDto
import com.nmakarov.coreclient.api.feign.dto.notification.NotificationRequest
import com.nmakarov.coreclient.model.notification.NotificationSubscriptionDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(value = "notifications-api", url = "\${clients.core-supplier.url}")
interface NotificationsApi {

    @GetMapping(
        value = ["v1/notification/list"],
        produces = ["application/json"],
        consumes = ["application/json"]
    )
    fun getSupplierStuffFromId(@RequestHeader("UserID") userId: Long): ResponseEntity<List<NotificationDto>>

    @GetMapping(
        value = ["v1/notification/subscription/list"],
        produces = ["application/json"],
        consumes = ["application/json"]
    )
    fun getNotificationSubscriptions(@RequestHeader("UserID") userId: Long): ResponseEntity<List<NotificationSubscriptionDto>>

    @PostMapping(
        value = ["v1/notification/stuff"],
        produces = ["application/json"],
        consumes = ["application/json"]
    )
    fun addStuffForSupplier(
        @RequestHeader("UserID") userId: Long,
        @RequestBody request: NotificationRequest
    ): ResponseEntity<Unit>

}