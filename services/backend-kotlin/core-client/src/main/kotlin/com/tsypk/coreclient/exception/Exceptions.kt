package com.tsypk.coreclient.exception

import com.tsypk.coreclient.model.ClientType
import com.tsypk.coreclient.model.subscription.Subscription
import com.tsypk.coreclient.model.subscription.SubscriptionType
import com.tsypk.coreclient.service.presenting.subscription.toHumanReadableString
import com.tsypk.coreclient.util.telegram.TelegramInfo
import com.tsypk.coreclient.util.telegram.Messages
import com.tsypk.coreclient.util.strOrBlank

abstract class BusinessException(
    val errorCode: String,
    val errorMsg: String,
) : RuntimeException(errorMsg)

fun BusinessException.message(): String {
    return this.errorMsg
}

abstract class NotFoundException(
    errorCode: String,
    errorMsg: String
) : BusinessException(
    errorCode = errorCode,
    errorMsg = errorMsg,
)

class SubscriptionNotFoundException(val tgBotUserId: Long, val type: ClientType? = null) :
    NotFoundException(
        errorCode = "SUBSCRIPTION_NOT_FOUND",
        errorMsg = "Подписка не найдена tgBotUserId=$tgBotUserId и type=${type?.name.strOrBlank()}"
    )

class SupplierNotFoundException(tgBotUserId: Long) :
    NotFoundException(
        errorCode = "SUPPLIER_NOT_FOUND",
        errorMsg = "Поставщик с id=$tgBotUserId не был найден",
    )

class CommandNotFoundException(text: String) :
    BusinessException(
        errorCode = "COMMAND_NOT_FOUND",
        errorMsg = "Команда не найдена text=$text",
    )

class TelegramBotUserNotFoundException(id: Long) :
    BusinessException(
        errorCode = "TELEGRAM_BOT_USER_NOT_FOUND",
        errorMsg = "Пользователь телеграм бота с id=$id не был найден"
    )

class TelegramBotUserNotFoundByUsernameException(username: String) :
    BusinessException(
        errorCode = "TELEGRAM_BOT_USER_NOT_FOUND",
        errorMsg = "Пользователь телеграм бота с username=$username не был найден"
    )

class SubscriptionIsExpiredException(subscription: Subscription) :
    BusinessException(
        errorCode = "SUBSCRIPTION_EXPIRED",
        errorMsg = Messages.SUBSCRIPTION.subscriptionExpired(subscription),
    )

class NotChannelSubscriberException :
    BusinessException(
        errorCode = "NOT_CHANNEL_SUBSCRIBER",
        errorMsg = "Обязательным условием данного бота является подписка на наш канал ${TelegramInfo.Channel.username}",
    )

class NotACommandException :
    BusinessException(
        errorCode = "NOT_A_COMMAND",
        errorMsg = "То что вы ввели не является командой, команда должна начинаться со знака слэша \"/\"",
    )

class SubscriptionHaveNoPermission(type: SubscriptionType) :
    BusinessException(
        errorCode = "SUBSCRIPTION_HAVE_NO_PERMISSION",
        errorMsg = "Пользователям с подпиской \"${toHumanReadableString(type)}\" не доступна эта команда",
    )

class RecognitionException(errorMsg: String) :
    BusinessException(
        errorCode = "IPHONE_RECOGNITION_ERROR",
        errorMsg = "$errorMsg\nПожалуйста, следуйте формату, указанному в /help",
    )

class MaxRequestsExceededException(cnt: Int) :
    BusinessException(
        errorCode = "MAX_REQUESTS_EXCEEDED",
        errorMsg = "Максимально за раз можно запросить $cnt айфонов",
    )

class CommandArgumentsParseException :
    BusinessException(
        errorCode = "COMMAND_ARGUMENTS_PARSE_ERROR",
        errorMsg = "Неверно указаны параметры запроса! Пожалуйста следуйте формату команды, указанного в /help",
    )

open class DialogActionException(val msg: String) : RuntimeException(msg)
class CancelException :
    DialogActionException("Действие было отменено")

class InputValidationException(msg: String) :
    DialogActionException(msg)

class MaxDialogIterationsExceededException() :
    DialogActionException("Превышено максимальное количество сообщений в одном диалоге")
