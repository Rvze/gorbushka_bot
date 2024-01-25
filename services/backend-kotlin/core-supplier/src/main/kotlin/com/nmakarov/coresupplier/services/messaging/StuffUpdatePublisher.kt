package com.nmakarov.coresupplier.services.messaging

import com.fasterxml.jackson.databind.ObjectMapper
import com.nmakarov.coreclient.model.notification.StuffUpdateBatchEvent
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.stereotype.Component

@Component
class StuffUpdatePublisher(
    private val redisTemplate: RedisTemplate<String, Any>,
    private val stuffUpdateChannel: ChannelTopic,
    private val objectMapper: ObjectMapper
) {
    fun publish(stuffUpdateBatchEvent: StuffUpdateBatchEvent) {
        if (stuffUpdateBatchEvent.updates.isEmpty()) {
            println("Skipping publishing empty ${stuffUpdateBatchEvent.updates}")
            return
        }

        println("Publishing ${stuffUpdateBatchEvent.updates}")
        redisTemplate.convertAndSend(stuffUpdateChannel.topic, objectMapper.writeValueAsString(stuffUpdateBatchEvent))
    }
}