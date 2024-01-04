package com.tsypk.coreclient.service

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.tsypk.coreclient.exception.TelegramBotUserNotFoundByUsernameException
import com.tsypk.coreclient.exception.TelegramBotUserNotFoundException
import com.tsypk.coreclient.model.telegram.TgBotUser
import com.tsypk.coreclient.repository.TgBotUserRepository
import com.tsypk.coreclient.service.stat.StatisticsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit

@Service
class UserMgmtService(
    private val tgBotUserRepository: TgBotUserRepository,
    private val statisticsService: StatisticsService,
) {

    private val tgBotUserCache: Cache<Long, TgBotUser> =
        CacheBuilder.newBuilder()
            .maximumSize(200L)
            .expireAfterWrite(1, TimeUnit.DAYS)
            .build()

    private val usernameToTgId: Cache<String, Long> =
        CacheBuilder.newBuilder()
            .maximumSize(200L)
            .expireAfterWrite(1, TimeUnit.DAYS)
            .build()

    @Transactional
    fun createIfNotExistAndGet(tgBotUser: TgBotUser): TgBotUser {
        return try {
            getByTelegramBotId(tgBotUser.id)
        } catch (ignore: TelegramBotUserNotFoundException) {
            // STAT
            statisticsService.newUser(tgBotUser.id)

            tgBotUserRepository.insertDoNothingOnConflict(tgBotUser)
            updateCaches(tgBotUser)
            tgBotUser
        }
    }

    fun getByTelegramBotId(id: Long): TgBotUser {
        tgBotUserCache.getIfPresent(id)?.let {
            return it
        }

        tgBotUserRepository.getById(id)?.let {
            updateCaches(it)
            return it
        }

        throw TelegramBotUserNotFoundException(id)
    }

    fun getByUsername(username: String): TgBotUser {
        usernameToTgId.getIfPresent(username)?.let {
            return getByTelegramBotId(it)
        }

        tgBotUserRepository.getByUsername(username)?.let {
            it.username = username
            updateCaches(it)
            return it
        }

        throw TelegramBotUserNotFoundByUsernameException(username)
    }

    fun enrichUsernames(ids: Collection<Long>): Map<Long, String> {
        val result = mutableMapOf<Long, String>()
        val notInCache = ids.filter {
            val found = tgBotUserCache.getIfPresent(it)
            if (found == null) {
                true
            } else {
                result[found.id] = found.usernameOrAnyTitle()
                false
            }
        }

        tgBotUserRepository.getAllWhereIdIn(notInCache).forEach {
            result[it.id] = it.usernameOrAnyTitle()
        }

        return result
    }

    private fun updateCaches(tgBotUser: TgBotUser) {
        tgBotUserCache.put(tgBotUser.id, tgBotUser)
        tgBotUser.username?.let { username ->
            usernameToTgId.put(username, tgBotUser.id)
        }
    }
}
