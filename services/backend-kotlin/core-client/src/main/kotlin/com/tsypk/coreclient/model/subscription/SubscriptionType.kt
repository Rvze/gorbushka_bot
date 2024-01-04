package com.tsypk.coreclient.model.subscription

import com.tsypk.coreclient.model.ClientType

enum class SubscriptionType(
    val days: Long,
    val clientType: ClientType,
) {
    ADMIN(Long.MAX_VALUE - 1, ClientType.ADMIN),

    SUPPLIER_DEMO(3, ClientType.SUPPLIER),
    SUPPLIER_STANDARD(30, ClientType.SUPPLIER),
    SUPPLIER_MANUAL(25550, ClientType.SUPPLIER),

    RESELLER_DEMO(3, ClientType.RESELLER),
    RESELLER_STANDARD(30, ClientType.RESELLER),
    RESELLER_BETA_TEST(0, ClientType.RESELLER),
    ;
}
