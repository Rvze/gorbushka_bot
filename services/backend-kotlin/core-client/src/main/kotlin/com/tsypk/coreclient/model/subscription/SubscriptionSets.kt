package com.tsypk.coreclient.model.subscription

object SubscriptionSets {
    val ALL = setOf(*SubscriptionType.values())
    val DEMO = setOf(SubscriptionType.SUPPLIER_DEMO, SubscriptionType.RESELLER_DEMO)
    val STANDARD = setOf(SubscriptionType.SUPPLIER_STANDARD, SubscriptionType.RESELLER_STANDARD)

    val SUPPLIER = setOf(
        SubscriptionType.SUPPLIER_DEMO,
        SubscriptionType.SUPPLIER_STANDARD,
        SubscriptionType.ADMIN,
    )
    val RESELLER = setOf(
        SubscriptionType.RESELLER_DEMO,
        SubscriptionType.RESELLER_STANDARD,
        SubscriptionType.ADMIN,
    )
    val ADMIN = setOf(
        SubscriptionType.ADMIN
    )
}
