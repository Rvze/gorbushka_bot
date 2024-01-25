package com.nmakarov.coresupplier.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.stereotype.Service

@Service
class SubscriberStuff(
    private val mapper: ObjectMapper,
) : MessageListener {
    override fun onMessage(message: Message, pattern: ByteArray?) {
        val readMsg: ObjectNode = mapper.readValue(message.body, ObjectNode::class.java)
        println(readMsg)
    }
}