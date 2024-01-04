package com.tsypk.coreclient.service.presenting.subscription

import com.tsypk.coreclient.model.subscription.SubscriptionType

fun toHumanReadableString(subscriptionType: SubscriptionType): String {
    return when (subscriptionType) {
        SubscriptionType.ADMIN -> "Подписка Админа"
        SubscriptionType.SUPPLIER_DEMO -> "демо-подписка поставщика"
        SubscriptionType.SUPPLIER_STANDARD -> "стандартная подписка поставщика"
        SubscriptionType.SUPPLIER_MANUAL -> "временная подписка поставщика"
        SubscriptionType.RESELLER_DEMO -> "демо-подписка покупателя"
        SubscriptionType.RESELLER_STANDARD -> "стандартная подписка покупателя"
        SubscriptionType.RESELLER_BETA_TEST -> "подписка бета тестирования покупателя"
    }
}
