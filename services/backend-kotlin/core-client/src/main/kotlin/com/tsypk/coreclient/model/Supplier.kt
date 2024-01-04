package com.tsypk.coreclient.model

import java.time.Instant

data class Supplier(
    val id: Long,
    val username: String,
    val title: String? = null,
) {
    var createdAt: Instant = Instant.now()
}
