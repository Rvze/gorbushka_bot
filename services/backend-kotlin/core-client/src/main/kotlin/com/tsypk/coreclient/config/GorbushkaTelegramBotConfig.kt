package com.tsypk.coreclient.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "telegram.gorbushka")
class GorbushkaTelegramBotConfig {
    lateinit var supplierToken: String
    lateinit var resellerToken: String
    lateinit var adminToken: String
}
