package com.tsypk.coreclient.model.subscription

import com.tsypk.coreclient.model.ClientType
import com.tsypk.coreclient.service.presenting.subscription.toHumanReadableString
import com.tsypk.coreclient.util.formatRu
import com.tsypk.coreclient.util.localDateNowMoscow
import java.time.LocalDate

data class Subscription(
    val tgBotUserId: Long,
    val clientType: ClientType = ClientType.RESELLER,
    val type: SubscriptionType = SubscriptionType.RESELLER_DEMO,
    val subscriptionDate: LocalDate = localDateNowMoscow(),
    val expireDate: LocalDate = subscriptionDate.plusDays(type.days),
) {
    override fun toString(): String {
        val subscriptionDate = this.subscriptionDate.formatRu()
        val expireDate = this.expireDate.formatRu()
        val typeStr = toHumanReadableString(this.type)
        return """
                Тип подписки: $typeStr
                Дата оформления: $subscriptionDate
                Дата истечения: $expireDate
            """.trimIndent()
    }

    fun isExpired(): Boolean {
        return localDateNowMoscow().isAfter(this.expireDate)
    }
}
