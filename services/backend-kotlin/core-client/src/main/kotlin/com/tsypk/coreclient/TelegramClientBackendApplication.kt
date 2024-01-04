package com.tsypk.coreclient

import com.tsypk.coreclient.application.admin.AdminBotApplication
import com.tsypk.coreclient.application.reseller.ResellerBotApplication
import com.tsypk.coreclient.application.supplier.SupplierBotApplication
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.FeignAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile
import org.springframework.context.event.EventListener
import org.springframework.retry.annotation.EnableRetry
import org.springframework.scheduling.annotation.EnableScheduling
import recognitioncommons.config.RecognitionLibraryConfiguration

@SpringBootApplication
@EnableFeignClients("com.tsypk.coreclient.api.feign")
@ImportAutoConfiguration(FeignAutoConfiguration::class)
@EnableScheduling
@EnableRetry
@Import(RecognitionLibraryConfiguration::class)
class TelegramClientBackendApplication

fun main(args: Array<String>) {
    runApplication<TelegramClientBackendApplication>(*args)
}

@Configuration
@Profile("production")
class SetupBot {
    @Autowired
    lateinit var resellerBotApplication: ResellerBotApplication

    @Autowired
    lateinit var supplierBotApplication: SupplierBotApplication

    @Autowired
    lateinit var adminBotApplication: AdminBotApplication

    @EventListener(value = [ApplicationReadyEvent::class])
    fun startBots() {
        runBlocking {
            launch { adminBotApplication.listenMessages() }

            launch { resellerBotApplication.listenMessages() }

            launch { supplierBotApplication.listenMessages() }
        }
    }
}

@Configuration
@Profile("dev")
class SetupDevBot {
    @Autowired
    lateinit var resellerBotApplication: ResellerBotApplication

    @Autowired
    lateinit var supplierBotApplication: SupplierBotApplication

    @EventListener(value = [ApplicationReadyEvent::class])
    fun startBots() {
        runBlocking {
            launch { resellerBotApplication.listenMessages() }

            launch { supplierBotApplication.listenMessages() }
        }
    }
}