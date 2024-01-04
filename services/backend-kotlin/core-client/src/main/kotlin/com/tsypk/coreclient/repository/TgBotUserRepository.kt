package com.tsypk.coreclient.repository

import com.tsypk.coreclient.model.telegram.TelegramAccountType
import com.tsypk.coreclient.model.telegram.TgBotUser
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp

@Repository
class TgBotUserRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
) {
    @Transactional
    fun insertDoNothingOnConflict(tgBotUser: TgBotUser) {
        jdbcTemplate.update(
            """
                insert into tg_bot_user (id, account_type, username, firstname, lastname, title, created_at)
                values (:id, :account_type, :username, :firstname, :lastname, :title, :created_at) ON CONFLICT DO NOTHING 
            """.trimIndent(),
            tgBotUser.parameterMap(),
        )
    }

    fun getById(id: Long): TgBotUser? {
        return jdbcTemplate.query(
            """
                select * from tg_bot_user where id=:id;
            """.trimIndent(),
            mapOf("id" to id),
            ROW_MAPPER,
        ).singleOrNull()
    }

    fun getByUsername(username: String): TgBotUser? {
        return jdbcTemplate.query(
            """
                select * from tg_bot_user where username=:username;
            """.trimIndent(),
            mapOf("username" to username),
            ROW_MAPPER,
        ).singleOrNull()
    }

    fun getAllWhereIdIn(ids: List<Long>): List<TgBotUser> {
        if (ids.isEmpty()) {
            return emptyList()
        }
        val idsString = ids.joinToString(",") { it.toString() }
        return jdbcTemplate.query(
            """
                select * from tg_bot_user where id in ($idsString);
            """.trimIndent(),
            ROW_MAPPER,
        )
    }

    @Transactional
    fun updateInfoWhereNotNull(tgBotUser: TgBotUser) {
        val parametersAndValues = tgBotUser.notNullParameterMap()
        if (parametersAndValues.isEmpty()) {
            return
        }

        val queryParameters = parametersAndValues.map {
            "${it.key}=:${it.key}"
        }.joinToString(separator = ", ") { it }

        jdbcTemplate.update(
            """
                update tg_bot_user SET $queryParameters
                    WHERE id=${tgBotUser.id}
            """.trimIndent(),
            parametersAndValues,
        )
    }

    fun countAll(): Long {
        return jdbcTemplate.queryForObject(
            """
                SELECT COUNT(*) FROM tg_bot_user;
            """.trimIndent(),
            emptyMap<String, String>(),
            Long::class.java,
        ) ?: 0L
    }

    private companion object {
        val ROW_MAPPER = RowMapper<TgBotUser> { rs, _ ->
            TgBotUser(
                id = rs.getLong("id"),
                accountType = TelegramAccountType.valueOf(rs.getString("account_type")),
                username = rs.getString("username"),
                firstname = rs.getString("firstname"),
                lastname = rs.getString("lastname"),
                title = rs.getString("title"),
            ).apply {
                this.createdAt = rs.getTimestamp("created_at").toInstant()
            }
        }
    }

    private fun TgBotUser.parameterMap() = mapOf(
        "id" to this.id,
        "account_type" to this.accountType.name,
        "username" to this.username,
        "firstname" to this.firstname,
        "lastname" to this.lastname,
        "title" to this.title,
        "created_at" to Timestamp.from(this.createdAt),
    )

    private fun TgBotUser.notNullParameterMap() =
        buildMap {
            if (username != null) {
                put("username", username)
            }
            if (firstname != null) {
                put("firstname", firstname)
            }
            if (lastname != null) {
                put("lastname", lastname)
            }
            if (title != null) {
                put("title", title)
            }
        }
}
