package com.tsypk.coreclient.service

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.tsypk.coreclient.exception.SubscriptionIsExpiredException
import com.tsypk.coreclient.exception.SubscriptionNotFoundException
import com.tsypk.coreclient.model.ClientType
import com.tsypk.coreclient.model.subscription.Subscription
import com.tsypk.coreclient.model.subscription.SubscriptionType
import com.tsypk.coreclient.repository.SubscriptionRepository
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class SubscriptionMgmtService(
    private val subscriptionRepository: SubscriptionRepository,
) {
    private val subscriptionsCache: Cache<String, Subscription> =
        CacheBuilder.newBuilder()
            .maximumSize(200L)
            .expireAfterWrite(1, TimeUnit.DAYS)
            .build()

    fun createOrUpdate(subscription: Subscription) {
        subscriptionRepository.upsert(subscription)
        val cacheKey = cacheKey(subscription.tgBotUserId, subscription.clientType)
        subscriptionsCache.put(cacheKey, subscription)
    }

    fun findMany(tgBotUserId: Long): List<Subscription> {
        return subscriptionRepository.getByTgBotUserId(tgBotUserId)
    }

    fun findOne(tgBotUserId: Long, clientType: ClientType): Subscription {
        val cacheKey = cacheKey(tgBotUserId, clientType)
        subscriptionsCache.getIfPresent(cacheKey)?.let {
            return it
        }

        subscriptionRepository.getSingleWithAdmin(
            tgBotUserId = tgBotUserId,
            clientType = clientType,
        )?.let {
            subscriptionsCache.put(cacheKey, it)
            return it
        } ?: throw SubscriptionNotFoundException(tgBotUserId = tgBotUserId, type = clientType)
    }

    fun delete(tgBotUserId: Long, clientType: ClientType) {
        subscriptionRepository.delete(tgBotUserId, clientType)
        subscriptionsCache.invalidate(cacheKey(tgBotUserId, clientType))
    }

    fun assertNotExpired(subscription: Subscription) {
        if (subscription.clientType == ClientType.ADMIN)
            return
        if (subscription.type == SubscriptionType.SUPPLIER_MANUAL)
            return
        if (subscription.isExpired()) {
            throw SubscriptionIsExpiredException(subscription)
        }
    }

    private fun cacheKey(tgBotUserId: Long, clientType: ClientType): String {
        return "${tgBotUserId}_$clientType"
    }
}
