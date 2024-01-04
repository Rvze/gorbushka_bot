package com.tsypk.coreclient.util.telegram

import com.tsypk.coreclient.model.Supplier
import com.tsypk.coreclient.model.stat.Stat
import com.tsypk.coreclient.model.stuff.FullRecognitionResult
import com.tsypk.coreclient.model.subscription.Subscription
import com.tsypk.coreclient.service.presenting.apple.groupAirPodsByCountryString
import com.tsypk.coreclient.service.presenting.apple.groupIphoneByCountryString
import com.tsypk.coreclient.service.presenting.apple.groupMacbooksByCountryString
import com.tsypk.coreclient.service.presenting.apple.toHumanReadableString
import com.tsypk.coreclient.service.presenting.subscription.toHumanReadableString
import com.tsypk.coreclient.util.formatRu
import com.tsypk.coreclient.util.strOrBlank
import com.tsypk.coreclient.util.toSingleString
import recognitioncommons.models.apple.airpods.AirPods
import recognitioncommons.models.apple.airpods.AirPodsSearchModel
import recognitioncommons.models.apple.iphone.Iphone
import recognitioncommons.models.apple.iphone.IphoneSearchModel
import recognitioncommons.models.apple.macbook.Macbook
import recognitioncommons.util.toSearchModel

object Messages {
    object COMMON {
        val startCommand = """
            |Привет! Вводи /help что бы узнать побольше о боте
        """.trimMargin()

        private val tryAgainOrSupport = """
            |Попробуйте ещё раз или обратитесь в поддержку — ${TelegramInfo.Support.username}
        """.trimMargin()

        fun composeUnexpectedException(e: Exception): String {
            return composeUnexpectedExceptionMsg(e.message ?: "")
        }

        fun composeUnexpectedExceptionMsg(msg: String): String {
            return """
                |Ой-ой, произошла ошибка
                |
                |$tryAgainOrSupport
            """.trimMargin()
        }

        fun composeTelegramBotMovedMsg(handle: String): String {
            return """
                |Привет! Мы переехали, данные не были утеряны!
                |Переходи — $handle
            """.trimMargin()
        }

        fun composeFullExceptionInfo(e: Exception, id: Long? = null): String {
            return """
                |from id=$id
                |stacktrace:
                |${e.stackTraceToString()}
            """.trimMargin()
        }

        fun composePagedByLimitMessages(
            toSend: List<String>,
            limit: Int = 4096,
            prefix: String = "",
        ): List<String> {
            val answer = mutableListOf<String>()
            var buffer = StringBuilder(prefix)
            for (el in toSend) {
                if ((el.length + buffer.length) >= limit) {
                    answer.add(buffer.toString())
                    buffer = StringBuilder()
                }
                buffer.append(el)
                buffer.append("\n")
            }
            if (buffer.isNotEmpty()) {
                answer.add(buffer.toString())
            }
            return answer
        }
    }

    object ADMIN {
        val helpCommand = """
            |/start — начать
            |/help — помощь по командам
            |/stat - получить статистику по боту за текущий день
            |/supplier_stat - получить статистику по товарам поставщиков за текущий день
            |/suppliers — получить список поставщиков
            |/add_subscription — дать подписку поставщику (на бот поставщика)
            |/add_supplier — добавить нового поставщика
            |/delete_supplier — удалить поставщика и весь его ассортимент
            |/add <nickname> — начать сессию по обновлению прайс-листа поставщика
            |/send_resellers – отправить сообщение пользователям от имени ${TelegramInfo.ResellerBot.username} с указанными telegram id
            |/send_suppliers – отправить сообщение пользователям от имени ${TelegramInfo.SupplierBot.username} с указанными telegram id
        """.trimMargin()

        const val onReload = "Привет! Если что я был перезапущен, всё состояние в боте очищено и обновлено"

        const val plannedTruncation = "Проводится запланированное удаление товаров поставщиков"
        const val successTruncation = "Товары поставщиков были успешно удалены"

        const val plannedFirstSupplierReminder = "Отсылаем поставщикам напоминание в первый раз"
        const val plannedSecondSupplierReminder = "Отсылаем поставщикам напоминание во второй раз"

        fun plannedMailingException(e: Exception): String {
            return """
                |Произошла ошибка при постинге прайс-листа в канал
                |Детали:
                |
                ${COMMON.composeFullExceptionInfo(e)}
            """.trimMargin()
        }
    }

    object SUPPLIER {
        val helpCommand = """
            |/start — начать
            |/help — помощь по командам
            |
            |Отправляйте сюда свой прайс-лист просто текстом
            |Он автоматически будет обновлён и появится в боте ${TelegramInfo.ResellerBot.username}
            |Можно просто переслать прайс-лист из своего канала
            |
            |Новые команды и возможности будут добавляться, следите за новостями в ${TelegramInfo.Channel.username}
        """.trimMargin()

        val requireItems = """
            |Отправь или перешли несколько сообщений
            |Отправь <b>send</b> что бы обновить ассортимент
            |Отправь <b>cancel</b> что бы отменить действия
            |Что бы полностью удалить ассортимент поставщика можешь отправить какой—то мусор и отправить send
        """.trimMargin()

        val onSupplierNotFound = """
            |Поставщик не найден в списке, добавьте нового через команду /add_supplier
        """.trimMargin()

        val botIsNotPermitted = """
            |К сожалению вам не доступен этот бот
            |Обратитесь в поддержку за получением доступа — ${TelegramInfo.Support.username}
        """.trimMargin()

        val supplierIsNotInDb = """
            |По какой—либо причине вы не были занесены в реестр поставщиков
            |Обратитесь в поддержку что бы попасть туда — ${TelegramInfo.Support.username}
        """.trimMargin()

        val supplierStashDeleted = """
            |Ваша база товаров была удалена, напоминаем вам загрузить обновлённый прайс-лист
            |
            |Обновления будут происходить автоматически каждый день в 08:00 по МСК
        """.trimMargin()

        val supplierFirstReminder = """
            |Мы заметили, что вы не обновляли ваш прайс-лист, просто доброе напоминание 🙌
            |Следующее оповещение будет в 12:00 по МСК
        """.trimMargin()

        val supplierSecondReminder = """
            |Вы все ещё не обновили ваш прайс-лист, покупатели не ждут 🏅
        """.trimMargin()

        fun composeSuppliersListCommand(suppliers: Collection<Supplier>): String {
            val suppliersString = suppliers.joinToString(separator = "\n") {
                "|${it.username} — ${it.title.strOrBlank()}"
            }
            return """
                |Список добавленных поставщиков:
                $suppliersString
            """.trimMargin()
        }

        fun composeSupplierNotFound(username: String): String {
            return """
                |Поставщик с ником $username не был найден, пожалуйста проверьте в списке
            """.trimMargin()
        }

        fun composePriceListRecognized(r: FullRecognitionResult): String {
            return """
                |${r.airPods.size} эирподсов добавлено
                |${r.iphones.size} айфонов добавлено
                |${r.macbooks.size} макбуков добавлено
                |${r.errors.size} строк не получилось распознать
                |
                |Пожалуйста проверьте что все товары были добавлены в прайс-лист
                |Если возникли трудности, обратитесь пожалуйста в поддержку — ${TelegramInfo.Support.username}
            """.trimMargin()
        }
    }

    object RESELLER {
        val startCommand = """
            |<b>Добро пожаловать в поисковую систему</b> Горбушки!

            |Сервис является <b>инструментом</b> по поиску <b>информации о поставщиках</b> и использует для поиска <b>открытые и общедоступные данные</b>.

            |Сервис работает в режиме <b>реального времени</b> и формирует цены напрямую <b>«от поставщиков»</b>.
            
            |Вводи /help что бы узнать побольше о возможностях бота.
        """.trimMargin()

        val helpCommand = """
            |/start — Начать
            |/help — Помощь
            |/support — Написать в поддержку
            |/subscription_info — Полная информация о текущей подписке
            |/buy — Купить подписку
            |
            |Что бы найти товар который вас интересует, напишите запрос напрямую без команд
            |
            |Форматы запросов и примеры:
            |   Iphone:
            |       [*Модель] [Память] [Цвет] [Страна]
            |       14 Pro Max 128 Black 🇺🇸
            |       14 Макс 256 Чёрный Китай
            |   Airpods:
            |       [*Модель] [Цвет] [Страна]
            |       AirPods Max Gray
            |       Эирподс 3
            |   Macbooks:
            |       [*Модель]   [*Процессор][*Экран][Память SSD][Память RAM][Цвет][Страна]
            |       Macbook Pro  M1 Pro      16         512      16        Space Gray RUSSIA
            |       Macbook Air 13 M1
            |* Обязательно
        """.trimMargin()
    }

    object SUBSCRIPTION {
        const val subscriptionNotReadyYet = "Подписки пока не готовы, но совсем скоро уже будут :)"
        const val noSubscription = "У вас ещё нет ни одной подписки"
        fun subscriptionExpired(subscription: Subscription): String {
            val expireDate = subscription.expireDate.formatRu()
            return """
                |К сожалению ваша подписка истекла $expireDate, пожалуйста оформите новую
            """.trimMargin()
        }

        fun subscriptionInfo(subscription: Subscription): String {
            val subscriptionDate = subscription.subscriptionDate.formatRu()
            val expireDate = subscription.expireDate.formatRu()
            val typeStr = toHumanReadableString(subscription.type)
            return """
                |Вам доступна $typeStr!
                |Дата оформления: $subscriptionDate
                |Дата истечения: $expireDate
            """.trimMargin()
        }

        fun newSubscriptionAdmin(id: Long, subscription: Subscription): String {
            val expireDate = subscription.expireDate.formatRu()
            return """
                Пользователю с id $id добавлена подписка $subscription до $expireDate
            """.trimIndent()
        }
    }

    object COMMAND {
        const val support = "Пожалуйста, напишите в поддержку — ${TelegramInfo.Support.username}"
    }

    object SEARCH {
        fun onEmptySearchResult(): String {
            return """
                |К сожалению по вашему запросу ничего не нашлось
                |Вы можете предложить добавить интересующего вас поставщика обратившись в поддержку: ${TelegramInfo.Support.username}
            """.trimMargin()
        }

        fun searchResultTemplate(
            header: String,
            staff: String,
        ): String {
            return """
                |<u><b>$header</b></u>
                $staff

                |🤖 ${TelegramInfo.ResellerBot.username}
            """.trimMargin()
        }

        fun mailingResultTemplate(
            header: String,
            staff: String,
        ): String {
            return """
                |<u><b>$header</b></u>
                $staff
            """.trimMargin()
        }

        fun searchResultModelTemplate(
            header: String,
            staff: String,
        ): String {
            return """
                |<u><b>$header</b></u>
                $staff

            """.trimMargin()
        }

        fun recognitionResultTemplate(payload: String, recognized: String): String {
            return """
                |Удалось распознать $recognized:
                |$payload
                |Находим лучшие цены 🤑🤑🤑
            """.trimMargin()
        }
    }

    object AIRPODS {
        fun composeRecognizedAirPodsTgLimit(airPods: List<AirPods>): List<String> {
            return COMMON.composePagedByLimitMessages(airPods.map { it.toHumanReadableString() })
        }

        fun airPodsBestPricesForSearchModel(
            airPods: List<AirPods>,
        ): String {
            if (airPods.isEmpty()) {
                return SEARCH.onEmptySearchResult()
            }
            return airPodsListResult(airPods)
        }

        fun airPodsBestPricesMailing(
            airPods: List<AirPods>,
        ): String {
            return airPodsListResult(airPods, SEARCH::mailingResultTemplate)
        }

        fun airPodsOneMessageResult(airPods: List<AirPods>): String {
            val airPodsGroupedByCountry = groupAirPodsByCountryString(airPods)
            val dynamicHeader = airPods.first().toSearchModel()
            return SEARCH.searchResultModelTemplate(
                header = dynamicHeader.toString(),
                staff = airPodsGroupedByCountry,
            )
        }

        private fun airPodsListResult(
            airPods: List<AirPods>,
            template: (header: String, staff: String) -> String = SEARCH::searchResultTemplate,
        ): String {
            val airPodsGroupedByCountry = groupAirPodsByCountryString(airPods)
            val dynamicHeader = airPods.first().toSearchModel()
            return template(dynamicHeader.toString(), airPodsGroupedByCountry)
        }

        fun composeAddAirPodsResponseMessage(addedAirPodsCnt: Int, errorLinesCnt: Int): String {
            return """
                |Всего $addedAirPodsCnt эирподсов добавлено
                |Всего $errorLinesCnt строк не получилось распознать как эирподсы
                |
                |Пожалуйста проверьте что все эирподсы были добавлены в прайс-лист
                |Если возникли трудности, обратитесь пожалуйста в поддержку — ${TelegramInfo.Support.username}
            """.trimMargin()
        }

        fun airPodsRecognized(airPods: List<AirPodsSearchModel>): String {
            return SEARCH.recognitionResultTemplate(
                payload = airPods.map { it.toString() }.toSingleString("\n"),
                recognized = "эирподсы"
            )
        }
    }

    object IPHONE {
        fun composeRecognizedIphonesTgLimit(iphones: List<Iphone>): List<String> {
            return COMMON.composePagedByLimitMessages(iphones.map { it.toHumanReadableString() })
        }

        fun iphoneBestPricesForSearchModel(
            iphones: List<Iphone>,
        ): String {
            if (iphones.isEmpty()) {
                return SEARCH.onEmptySearchResult()
            }
            return iphonesListResult(iphones)
        }

        fun iphonesBestPricesMailing(
            iphones: List<Iphone>,
        ): String {
            return iphonesListResult(iphones, SEARCH::mailingResultTemplate)
        }

        fun iphonesOneMessageResult(iphones: List<Iphone>): String {
            val iphonesGroupedByCountry = groupIphoneByCountryString(iphones)
            val dynamicHeader = iphones.first().toSearchModel()
            return SEARCH.searchResultModelTemplate(
                header = dynamicHeader.toString(),
                staff = iphonesGroupedByCountry,
            )
        }

        private fun iphonesListResult(
            iphones: List<Iphone>,
            template: (header: String, staff: String) -> String = SEARCH::searchResultTemplate,
        ): String {
            val iphonesGroupedByCountry = groupIphoneByCountryString(iphones)
            val dynamicHeader = iphones.first().toSearchModel()
            return template(dynamicHeader.toString(), iphonesGroupedByCountry)
        }

        fun composeAddIphonesResponseMessage(addedIphonesCnt: Int, errorLinesCnt: Int): String {
            return """
                |Всего $addedIphonesCnt айфонов добавлено
                |Всего $errorLinesCnt строк не получилось распознать как айфон
                |
                |Пожалуйста проверьте что все айфоны были добавлены в прайс-лист
                |Если возникли трудности, обратитесь пожалуйста в поддержку — ${TelegramInfo.Support.username}
            """.trimMargin()
        }

        fun iphonesRecognized(iphones: List<IphoneSearchModel>): String {
            return SEARCH.recognitionResultTemplate(
                payload = iphones.map { it.toString() }.toSingleString("\n"),
                recognized = "айфоны"
            )
        }
    }

    object MACBOOK {
        fun macbooksBestPricesForSearchModel(
            macbooks: List<Macbook>,
        ): String {
            if (macbooks.isEmpty()) {
                return SEARCH.onEmptySearchResult()
            }
            return MACBOOK.macbookListResult(macbooks)
        }

        private fun macbookListResult(
            macbooks: List<Macbook>,
            template: (header: String, staff: String) -> String = SEARCH::searchResultTemplate,
        ): String {
            val macbooksGroupedByCountry = groupMacbooksByCountryString(macbooks)
            val dynamicHeader = macbooks.first().toSearchModel()
            return template(dynamicHeader.toString(), macbooksGroupedByCountry)
        }
    }

    object DIALOG {
        const val requireUsername = """
                Отправь username поставщика, например: @postavshick
            """

        const val requireId = """
                Отправь id поставщика, например: 158343256 (можно узнать в @userinfobot)
            """

        const val requireTitle = """
                Отправь название поставщика (может быть любое), например: Поставщик Вася
            """

        const val requireAccountType = """
                Отправь тип аккаунта поставщика:
                    1 — Канал
                    2 — Группа
                    3 — Пользователь
            """

        const val requireTextToSendToResellers = """
                Введите сообщение, которое хотите отправить от имени ${TelegramInfo.ResellerBot.username}
            """

        const val requireTextToSendToSuppliers = """
                Введите сообщение, которое хотите отправить от имени ${TelegramInfo.SupplierBot.username}
            """

        fun composeSubscriptionForSupplierCreated(id: Long, username: String): String {
            return """
                    |Поставщик с id=$id и username=$username получил подписку и доступ к боту ${TelegramInfo.SupplierBot.username}
                    |Подписка бессрочная до тех пор, пока не убрать её вручную
                """.trimMargin()
        }

        fun composeSupplierCreateAction(
            username: String,
            id: Long,
            title: String,
        ): String {
            return """
                    |Добавить поставщика? (y/n)
                    |       username — $username
                    |       id — $id
                    |       name — $title
                """.trimMargin()
        }

        fun composeSupplierDeleteAction(
            username: String,
            id: Long,
            title: String,
        ): String {
            return """
                    |Удалить поставщика? (y/n)
                    |       username — $username
                    |       id — $id
                    |       name — $title
                    |       
                    |Действие необратимо, будут удалены весь ассортимент поставщика и его подписка.
                """.trimMargin()
        }

        fun composeSendToListedUsers(
            ids: List<Long>,
            text: String,
        ): String {
            return """
                    |Отправить пользователям: "$ids" 
                    |
                    |сообщение: "$text"?
                """.trimMargin()
        }

        fun composeSuccessfullySentToListedUsers(ids: List<Long>): String {
            return """
                |Пользователям в списке сообщение успешно отправлено
                |
                |ids = $ids
            """.trimMargin()
        }

        fun composeUnsuccessfullySentToListedUsers(ids: List<Long>): String {
            return """
                |Пользователям в списке ниже не удалось отправить сообщение :(
                |Либо что-то с телеграмом (что маловероятно), либо они запретили отправку (что более вероятно)
                |
                |ids = $ids
            """.trimMargin()
        }

        fun composeAddedOrNo(flag: Boolean): String {
            return if (flag) {
                "Успешно добавлено ✅"
            } else {
                "Не было добавлено ❌"
            }
        }

        fun composeDeletedOrNo(flag: Boolean): String {
            return if (flag) {
                "Успешно удалено ✅"
            } else {
                "Не было удалено ❌"
            }
        }
    }

    object STAT {
        private const val statEmpty = "Видимо сервис стартанули недавно, статистика пока пуста"

        fun composeStatMessage(stat: Stat?): String {
            if (stat == null) {
                return statEmpty
            }
            return "Статистика за <b>${stat.atDate.formatRu()}</b>:\n\n" + stat.statRegistry.toStringHtml()
        }

        fun composeSuppliersStat(usernames: Collection<String>): String {
            return "Поставщики, обновляющие прайс-лист:\n" + usernames.joinToString(separator = "\n") { it }
        }

        fun composeTotalMessagesStat(idsToUsername: Map<Long, String>, idsToMessages: Map<Long, Int>): String {
            return "Количество сообщений пользователей:\n" +
                    idsToUsername
                        .map { it.value to idsToMessages[it.key] }
                        .sortedByDescending { it.second }
                        .map { "${it.first} – ${it.second}" }
                        .joinToString(separator = "\n") { it }
        }
    }
}
