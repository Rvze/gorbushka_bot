package com.nmakarov.coreclient.service.client

import com.nmakarov.coreclient.api.feign.NotificationsApi
import com.nmakarov.coreclient.api.feign.dto.notification.NotificationRequest
import org.springframework.stereotype.Service

@Service
class NotificationClientService(
    private val notificationsApi: NotificationsApi
) {
    private companion object {
        const val DEFAULT_RETRY_COUNT = 2
    }

    fun subscribeOnItem(userId: Long, notificationRequest: NotificationRequest, currencyRetryCount: Int = 0) {
        try {
            notificationsApi.addStuffForSupplier(userId, notificationRequest)
        } catch (e: Exception) {
            if (currencyRetryCount >= DEFAULT_RETRY_COUNT) {
                throw e
            }
            return subscribeOnItem(userId, notificationRequest, currencyRetryCount + 1)
        }
    }
}