package com.nmakarov.coresupplier.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig {
    @Value("\${spring.data.redis.host}")
    private lateinit var host: String

    @Value("\${spring.data.redis.port}")
    private lateinit var port: String

    @Value("\${spring.data.redis.channels.stuff_update_channel.name}")
    private lateinit var stuffUpdateChannel: String

    /**
     * Connection to redis
     */
    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        val config = RedisStandaloneConfiguration(host, port.toInt())
        return JedisConnectionFactory(config)
    }

    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory?): RedisTemplate<String, Any> {
        val redisTemplate = RedisTemplate<String, Any>()
        redisTemplate.setConnectionFactory(redisConnectionFactory!!)
        redisTemplate.keySerializer = StringRedisSerializer()
        redisTemplate.valueSerializer = StringRedisSerializer()
        redisTemplate.afterPropertiesSet()
        return redisTemplate
    }

    /**
     * Messaging
     */
    @Bean
    fun subscriberStuffListenerAdapter(subscriberStuff: SubscriberStuff): MessageListenerAdapter {
        return MessageListenerAdapter(subscriberStuff);
    }

    @Bean
    fun stuffUpdateChannel(): ChannelTopic {
        return ChannelTopic(stuffUpdateChannel)
    }

    @Bean
    fun redisContainer(
        jedisConnectionFactory: JedisConnectionFactory,
        subscriberBuyListenerAdapter: MessageListenerAdapter,
        subscriberStuffListenerAdapter: MessageListenerAdapter,
        stuffUpdateChannel: ChannelTopic
    ): RedisMessageListenerContainer {
        val container = RedisMessageListenerContainer()
        container.setConnectionFactory(jedisConnectionFactory)

//        container.addMessageListener(subscriberStuffListenerAdapter, stuffUpdateChannel);
        return container
    }
}