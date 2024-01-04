package com.tsypk.coreclient.model.stat

data class ResellerStat(
    var resellerMessages: Int = 0,

    var searchRequests: Int = 0,

    var iphonesRecognized: Int = 0,
    var iphonesRecognizeErrors: Int = 0,
    var iphonesRespond: Int = 0,
) {
    override fun toString(): String {
        return """
            |Сообщений от перекупов: $resellerMessages
            |Поисков: $searchRequests
            |Айфонов распознали: $iphonesRecognized
            |Ошибок при распознавании айфонов: $iphonesRecognizeErrors
            |Айфонов найдено: $iphonesRespond
        """.trimMargin()
    }
}
