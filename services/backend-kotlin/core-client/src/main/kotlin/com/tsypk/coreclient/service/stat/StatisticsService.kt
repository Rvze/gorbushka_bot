package com.tsypk.coreclient.service.stat

import com.tsypk.coreclient.model.ClientType
import com.tsypk.coreclient.model.stat.Stat
import com.tsypk.coreclient.model.stat.StatRegistry
import com.tsypk.coreclient.repository.StatRepository
import com.tsypk.coreclient.repository.TgBotUserRepository
import com.tsypk.coreclient.util.localDateNowMoscow
import jakarta.annotation.PostConstruct
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class StatisticsService(
    private val statRepository: StatRepository,
    private val tgBotUserRepository: TgBotUserRepository,
) {
    var r: StatRegistry = StatRegistry()

    @PostConstruct
    fun initial() {
        val now = localDateNowMoscow()
        val current = statRepository.get(now)
        r = if (current == null) {
            val newStat = Stat(StatRegistry(), now)
            newStat.statRegistry.commonStat.users = tgBotUserRepository.countAll()
            statRepository.insert(newStat)
            newStat.statRegistry
        } else {
            current.statRegistry
        }
    }

    @Scheduled(initialDelay = 1L, fixedDelay = 1L, timeUnit = TimeUnit.MINUTES)
    fun update() {
        val now = localDateNowMoscow()
        val current = statRepository.get(now)
        if (current == null) {
            r = StatRegistry()
            statRepository.insert(Stat(r, now))
        } else {
            r.commonStat.users = tgBotUserRepository.countAll()
            statRepository.update(Stat(r, now))
        }
    }

    fun newMessage(from: Long, clientType: ClientType) {
        when (clientType) {
            ClientType.RESELLER -> r.resellerStat.resellerMessages++
            ClientType.SUPPLIER -> r.supplierStat.supplierMessages++
            else -> {}
        }

        r.commonStat.idToMessages.putIfAbsent(from, 0)
        r.commonStat.idToMessages[from] = r.commonStat.idToMessages[from]!! + 1

        r.commonStat.uniqueUsers.add(from)
    }

    fun newIphoneRecognitionReseller(iphones: Int, errors: Int) {
        r.resellerStat.iphonesRecognized += iphones
        r.resellerStat.iphonesRecognizeErrors += errors
        r.resellerStat.searchRequests++
    }

    fun newIphoneRecognitionSupplier(from: Long, iphones: Int, errors: Int) {
        r.supplierStat.iphonesRecognized += iphones
        r.supplierStat.iphonesRecognizeErrors += errors
        r.supplierStat.supplierPriceUpdates++
        r.supplierStat.suppliersAddingIphones.add(from)
    }

    fun newIphonesResponse(iphones: Int) {
        r.resellerStat.iphonesRespond += iphones
    }

    fun newUser(id: Long) {
        r.commonStat.newUsers.add(id)
    }

    override fun toString(): String {
        return r.toString()
    }
}
