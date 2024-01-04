package com.tsypk.coreclient.application.reseller

import com.tsypk.coreclient.repository.TgBotUserRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(value = [SpringExtension::class])
@SpringBootTest
class ResellerBotApplicationTest {
    @Autowired
    lateinit var telegramBotUserRepository: TgBotUserRepository

    @Test
    fun contextLoads() {
        println("contextLoads")
        println(telegramBotUserRepository.countAll())
    }
}
