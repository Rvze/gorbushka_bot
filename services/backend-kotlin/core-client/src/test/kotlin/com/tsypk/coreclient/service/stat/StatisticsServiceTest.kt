package com.tsypk.coreclient.service.stat

import com.tsypk.coreclient.model.stat.StatRegistry
import com.tsypk.coreclient.repository.StatRepository
import com.tsypk.coreclient.util.localDateNowMoscow
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Profile
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDate
import java.time.Month

@ExtendWith(value = [SpringExtension::class])
@SpringBootTest
class StatisticsServiceTest {
    @Autowired
    private lateinit var statRepository: StatRepository

    val getAllStatisticsEnabled = false

    @Test
    @Profile("test")
    @Disabled
    fun getAllStatistics() {
        if (getAllStatisticsEnabled) {
            val from = LocalDate.of(2023, Month.FEBRUARY, 28)
            val to = localDateNowMoscow()

            val result = statRepository.getAllWithin(from, to)
            println(
                "дата, пользователей всего, уникальных пользователей, новых пользователей, сообщений в поиске, " +
                    "запросов на поиск, айфонов распознано, айфонов найдено, сообщений в поставщике, обновлений прайс-листа, " +
                    "айфонов распознано у поставщиков, поставщики обновляющие прайс лист"
            )
            result.sortedBy { it.atDate }.forEach {
                println(csvLine(it.atDate, it.statRegistry))
            }
        }
    }

    private fun csvLine(date: LocalDate, statRegistry: StatRegistry): String {
        return listOf(
            date.toString(),
            statRegistry.commonStat.users,
            statRegistry.commonStat.uniqueUsers.size,
            statRegistry.commonStat.newUsers.size,
            statRegistry.resellerStat.resellerMessages,
            statRegistry.resellerStat.searchRequests,
            statRegistry.resellerStat.iphonesRecognized,
            statRegistry.resellerStat.iphonesRespond,
            statRegistry.supplierStat.supplierMessages,
            statRegistry.supplierStat.supplierPriceUpdates,
            statRegistry.supplierStat.iphonesRecognized,
            statRegistry.supplierStat.suppliersAddingIphones.size
        ).joinToString(separator = ", ") { it.toString() }
    }
}
