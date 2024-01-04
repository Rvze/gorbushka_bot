package com.tsypk.coreclient.config

import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.bot.ktor.KtorRequestsExecutorBuilder
import dev.inmo.tgbotapi.bot.settings.limiters.CommonLimiter
import dev.inmo.tgbotapi.utils.TelegramAPIUrlsKeeper
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(value = [GorbushkaTelegramBotConfig::class])
class ApplicationConfiguration(
    private val gorbushkaTelegramBotConfig: GorbushkaTelegramBotConfig,
) {
    @Bean
    fun supplierBot(): TelegramBot {
        val urlsKeeper = TelegramAPIUrlsKeeper(
            token = gorbushkaTelegramBotConfig.supplierToken,
            testServer = false,
        )
        val builder = KtorRequestsExecutorBuilder(urlsKeeper)
        return builder.build()
    }

    @Bean
    fun resellerBot(): TelegramBot {
        val urlsKeeper = TelegramAPIUrlsKeeper(
            token = gorbushkaTelegramBotConfig.resellerToken,
            testServer = false,
        )
        val builder = KtorRequestsExecutorBuilder(urlsKeeper)
        builder.requestsLimiter = CommonLimiter()
        return builder.build()
    }

    @Bean
    fun adminBot(): TelegramBot {
        val urlsKeeper = TelegramAPIUrlsKeeper(
            token = gorbushkaTelegramBotConfig.adminToken,
            testServer = false,
        )
        val builder = KtorRequestsExecutorBuilder(urlsKeeper)
        builder.requestsLimiter = CommonLimiter()
        return builder.build()
    }
}
