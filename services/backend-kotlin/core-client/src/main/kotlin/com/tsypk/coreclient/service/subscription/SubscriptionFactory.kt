package com.tsypk.coreclient.service.subscription

import com.tsypk.coreclient.model.ClientType
import com.tsypk.coreclient.model.subscription.Subscription
import com.tsypk.coreclient.model.subscription.SubscriptionType
import com.tsypk.coreclient.util.localDateNowMoscow
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class SubscriptionFactory {
    private companion object {
        val BETA_TEST_RESELLER_EXPIRE_DATE: LocalDate = LocalDate.of(2023, 9, 1)!!
        val INFINITE_EXPIRE_DATE: LocalDate = LocalDate.of(2099, 1, 1)
    }

    fun subscriptionForDays(
        tgBotUserId: Long,
        clientType: ClientType,
        type: SubscriptionType,
        days: Long? = null,
    ): Subscription {
        val today = localDateNowMoscow()
        return Subscription(
            tgBotUserId = tgBotUserId,
            clientType = clientType,
            type = type,
            subscriptionDate = today,
            expireDate = if (days != null) {
                today.plusDays(days)
            } else {
                today.plusDays(type.days)
            },
        )
    }

    fun manualSubscription(tgBotUserId: Long, clientType: ClientType): Subscription {
        return Subscription(
            tgBotUserId = tgBotUserId,
            clientType = clientType,
            type = SubscriptionType.SUPPLIER_MANUAL,
            subscriptionDate = localDateNowMoscow(),
            expireDate = INFINITE_EXPIRE_DATE,
        )
    }

    fun betaTestResellerSubscription(tgBotUserId: Long): Subscription? {
        val today = localDateNowMoscow()
        if (today.isAfter(BETA_TEST_RESELLER_EXPIRE_DATE)) {
            return null
        }

        return Subscription(
            tgBotUserId = tgBotUserId,
            clientType = ClientType.RESELLER,
            type = SubscriptionType.RESELLER_BETA_TEST,
            subscriptionDate = today,
            expireDate = BETA_TEST_RESELLER_EXPIRE_DATE,
        )
    }
}
