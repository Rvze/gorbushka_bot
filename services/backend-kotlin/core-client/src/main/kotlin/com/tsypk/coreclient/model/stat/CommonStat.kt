package com.tsypk.coreclient.model.stat

data class CommonStat(
    var users: Long = 0L,
    var uniqueUsers: MutableSet<Long> = mutableSetOf(),
    var newUsers: MutableSet<Long> = mutableSetOf(),
    var idToMessages: MutableMap<Long, Int> = mutableMapOf(),
) {
    override fun toString(): String {
        val totalMessages = idToMessages.map { it.value }.sum()
        return """
            |Всего пользователей: $users
            |Всего сообщений: $totalMessages
            |Всего уникальных пользователей: ${uniqueUsers.size}
            |Всего новых пользователей: ${newUsers.size}
        """.trimMargin()
    }
}
