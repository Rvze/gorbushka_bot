package com.tsypk.coreclient.exception

import com.tsypk.coreclient.util.telegram.Messages
import com.tsypk.coreclient.util.telegram.TelegramInfo
import dev.inmo.tgbotapi.extensions.api.send.sendMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.types.ChatId
import dev.inmo.tgbotapi.types.message.HTMLParseMode
import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import dev.inmo.tgbotapi.types.message.content.MessageContent

suspend fun withErrorHandling(
    ctx: BehaviourContext,
    msg: CommonMessage<MessageContent>,
    action: suspend () -> Unit,
) {
    val chatId = msg.chat.id.chatId
    try {
        action.invoke()
    } catch (e: DialogActionException) {
        ctx.bot.sendMessage(
            chat = msg.chat,
            text = e.msg,
            parseMode = HTMLParseMode,
        )
    } catch (e: NotChannelSubscriberException) {
        println(e)
        ctx.bot.sendMessage(
            chat = msg.chat,
            text = e.errorMsg,
            parseMode = HTMLParseMode,
        )
    } catch (e: BusinessException) {
        println(e)
        val toSend = if (msg.chat.id.chatId in TelegramInfo.ADMIN_IDS) {
            Messages.COMMON.composeFullExceptionInfo(e, chatId)
        } else {
            Messages.COMMON.composeUnexpectedExceptionMsg(e.message())
        }

        ctx.bot.sendMessage(
            chat = msg.chat,
            text = toSend,
            parseMode = HTMLParseMode,
        )
        notifyAdminsAboutError(ctx, e, chatId)
    } catch (e: Exception) {
        println(e)
        val toSend = if (msg.chat.id.chatId in TelegramInfo.ADMIN_IDS) {
            Messages.COMMON.composeFullExceptionInfo(e, chatId)
        } else {
            Messages.COMMON.composeUnexpectedException(e)
        }

        ctx.bot.sendMessage(
            chat = msg.chat,
            text = toSend,
            parseMode = HTMLParseMode,
        )
        notifyAdminsAboutError(ctx, e, chatId)
    }
}

private suspend fun notifyAdminsAboutError(
    ctx: BehaviourContext,
    e: Exception,
    chatId: Long,
) {
    val toSend = Messages.COMMON.composeFullExceptionInfo(e, chatId)
    TelegramInfo.ADMIN_IDS.forEach {
        ctx.bot.sendMessage(
            chatId = ChatId(it),
            text = toSend,
        )
    }
}
