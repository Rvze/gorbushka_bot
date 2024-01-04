package com.tsypk.coreclient.service.enrichment

import com.tsypk.coreclient.exception.InputValidationException
import com.tsypk.coreclient.exception.MaxDialogIterationsExceededException
import com.tsypk.coreclient.model.stuff.FullRecognitionResult
import com.tsypk.coreclient.model.telegram.TelegramAccountType
import com.tsypk.coreclient.service.StuffRecognitionService
import com.tsypk.coreclient.service.presenting.apple.toHumanReadableString
import com.tsypk.coreclient.util.assertChatId
import com.tsypk.coreclient.util.assertInSet
import com.tsypk.coreclient.util.assertUsername
import com.tsypk.coreclient.util.assertYesOrNo
import com.tsypk.coreclient.util.checkCancel
import com.tsypk.coreclient.util.isYes
import com.tsypk.coreclient.util.telegram.Messages
import dev.inmo.tgbotapi.extensions.api.send.sendMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.expectations.waitText
import dev.inmo.tgbotapi.types.message.HTMLParseMode
import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import dev.inmo.tgbotapi.types.message.content.MessageContent
import kotlinx.coroutines.flow.first
import org.springframework.stereotype.Service

@Service
class BotDialogEnricher(
    private val staffRecognitionService: StuffRecognitionService,
) {
    companion object {
        const val MAX_ITERATIONS = 15
    }

    suspend fun askPriceList(
        ctx: BehaviourContext,
        msg: CommonMessage<MessageContent>,
        supplierUsername: String,
    ): FullRecognitionResult {
        var result = FullRecognitionResult()
        var cnt = 0

        ctx.bot.sendMessage(
            chat = msg.chat,
            text = Messages.SUPPLIER.requireItems,
            parseMode = HTMLParseMode,
        )

        while (cnt < MAX_ITERATIONS) {
            val text = ctx.waitText().first().text
            checkCancel(text)
            cnt++
            if (text.equals("send", true)) {
                return result
            } else if (text.equals("state", true)) {
                val toSendList = Messages.COMMON.composePagedByLimitMessages(
                    prefix = "Ваш прайс-лист обновился на следующий:\n",
                    toSend = result.iphones.map { it.toHumanReadableString() } +
                        result.airPods.map { it.toHumanReadableString() },
                )
                toSendList.forEach {
                    ctx.bot.sendMessage(
                        chat = msg.chat,
                        text = "<pre>${it}</pre>",
                        parseMode = HTMLParseMode,
                    )
                }
                continue
            }

            val currentResult = staffRecognitionService.recognizeFull(text)
            result = result.add(currentResult)

            ctx.bot.sendMessage(
                chat = msg.chat,
                text = Messages.SUPPLIER.composePriceListRecognized(result)
            )

            val errorsToSend = Messages.COMMON.composePagedByLimitMessages(currentResult.errors)
            errorsToSend.forEach {
                ctx.bot.sendMessage(
                    chat = msg.chat,
                    text = it,
                )
            }
        }

        throw MaxDialogIterationsExceededException()
    }

    suspend fun askUsername(
        ctx: BehaviourContext,
        msg: CommonMessage<MessageContent>,
        additional: String? = null,
        text: String = composeAdditional(Messages.DIALOG.requireUsername, additional),
        curIter: Int = 0,
    ): String {
        checkMaxIterations(curIter)
        ctx.bot.sendMessage(
            chat = msg.chat,
            text = text,
        )
        val username = ctx.waitText().first().text
        checkCancel(username)
        return try {
            assertUsername(username)
            username
        } catch (e: InputValidationException) {
            askUsername(ctx = ctx, msg = msg, additional = e.msg, curIter = curIter + 1)
        }
    }

    suspend fun askId(
        ctx: BehaviourContext,
        msg: CommonMessage<MessageContent>,
        additional: String? = null,
        curIter: Int = 0,
    ): Long {
        ctx.bot.sendMessage(
            chat = msg.chat,
            text = composeAdditional(Messages.DIALOG.requireId, additional),
        )
        val chatId = ctx.waitText().first().text
        checkCancel(chatId)
        return try {
            assertChatId(chatId)
            chatId.toLong()
        } catch (e: InputValidationException) {
            askId(ctx, msg, e.msg, curIter + 1)
        }
    }

    suspend fun askTitle(
        ctx: BehaviourContext,
        msg: CommonMessage<MessageContent>,
        additional: String? = null,
        curIter: Int = 0,
    ): String {
        ctx.bot.sendMessage(
            chat = msg.chat,
            text = composeAdditional(Messages.DIALOG.requireTitle, additional),
        )
        val title = ctx.waitText().first().text
        checkCancel(title)
        return try {
            title
        } catch (e: InputValidationException) {
            askTitle(ctx, msg, e.msg, curIter + 1)
        }
    }

    suspend fun askAccountType(
        ctx: BehaviourContext,
        msg: CommonMessage<MessageContent>,
        additional: String? = null,
        curIter: Int = 0,
    ): TelegramAccountType {
        ctx.bot.sendMessage(
            chat = msg.chat,
            text = composeAdditional(Messages.DIALOG.requireAccountType, additional),
        )
        val accountType = ctx.waitText().first().text
        checkCancel(accountType)
        return try {
            assertInSet(check = accountType, set = setOf("1", "2", "3"))
            when (accountType) {
                "1" -> TelegramAccountType.CHANNEL
                "2" -> TelegramAccountType.GROUP
                "3" -> TelegramAccountType.USER
                else -> TelegramAccountType.UNDEFINED
            }
        } catch (e: InputValidationException) {
            askAccountType(ctx, msg, e.msg, curIter + 1)
        }
    }

    suspend fun askYesOrNo(
        ctx: BehaviourContext,
        msg: CommonMessage<MessageContent>,
        toAsk: String,
        additional: String? = null,
        curIter: Int = 0,
    ): Boolean {
        ctx.bot.sendMessage(
            chat = msg.chat,
            text = composeAdditional(toAsk, additional),
        )
        val yesOrNo = ctx.waitText().first().text
        checkCancel(yesOrNo)
        return try {
            assertYesOrNo(check = yesOrNo)
            isYes(yesOrNo)
        } catch (e: InputValidationException) {
            askYesOrNo(ctx, msg, toAsk, e.msg, curIter + 1)
        }
    }

    suspend fun askText(
        ctx: BehaviourContext,
        msg: CommonMessage<MessageContent>,
        toAsk: String,
    ): String {
        ctx.bot.sendMessage(
            chat = msg.chat,
            text = toAsk,
        )
        return ctx.waitText().first().text
    }

    private fun composeAdditional(toSend: String, additional: String?): String {
        if (additional != null) {
            return "${additional}\n${toSend}"
        }
        return toSend
    }

    private fun checkMaxIterations(curIter: Int) {
        if (curIter > MAX_ITERATIONS) {
            throw MaxDialogIterationsExceededException()
        }
    }
}
