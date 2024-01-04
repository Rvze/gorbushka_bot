package com.tsypk.coreclient.repository

import com.tsypk.coreclient.model.ClientType
import com.tsypk.coreclient.model.subscription.Subscription
import com.tsypk.coreclient.model.subscription.SubscriptionType
import com.tsypk.coreclient.util.localDateNowMoscow
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.Date

@Repository
class SubscriptionRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
) {

    @Transactional
    fun upsert(subscription: Subscription) {
        val found = getSingle(subscription.tgBotUserId, subscription.clientType)
        if (found == null) {
            insertWithAudit(subscription)
            return
        }

        updateSubscription(subscription)
    }

    fun getByTgBotUserId(tgBotUserId: Long): List<Subscription> {
        return jdbcTemplate.query(
            """
                SELECT * FROM subscription WHERE tg_bot_user_id = :tg_bot_user_id
            """.trimIndent(),
            mapOf("tg_bot_user_id" to tgBotUserId),
            ROW_MAPPER,
        )
    }

    fun getSingle(tgBotUserId: Long, clientType: ClientType): Subscription? {
        return jdbcTemplate.query(
            """
                SELECT * FROM subscription
                    WHERE tg_bot_user_id = :tg_bot_user_id and client_type = :client_type
                    order by client_type;
            """.trimIndent(),
            mapOf(
                "tg_bot_user_id" to tgBotUserId,
                "client_type" to clientType.name,
            ),
            ROW_MAPPER,
        ).singleOrNull()
    }

    fun getSingleWithAdmin(tgBotUserId: Long, clientType: ClientType): Subscription? {
        return jdbcTemplate.query(
            """
                SELECT * FROM subscription
                    WHERE tg_bot_user_id = :tg_bot_user_id
                      and (client_type = :client_type or client_type = 'ADMIN')
                    order by client_type;
            """.trimIndent(),
            mapOf(
                "tg_bot_user_id" to tgBotUserId,
                "client_type" to clientType.name,
            ),
            ROW_MAPPER,
        ).firstOrNull()
    }

    @Transactional
    fun insertWithAudit(subscription: Subscription) {
        insert(subscription)
        insertAudit(subscription)
    }

    @Transactional
    fun updateSubscription(subscription: Subscription) {
        jdbcTemplate.update(
            """
                update subscription SET subscription_type=:subscription_type, 
                    subscription_date=:subscription_date, expire_date=:expire_date
                        WHERE tg_bot_user_id=:tg_bot_user_id and client_type=:client_type;
            """.trimIndent(),
            subscription.parameterMap()
        )
    }

    @Transactional
    fun delete(tgBotUserId: Long, clientType: ClientType) {
        jdbcTemplate.update(
            """
                DELETE FROM subscription WHERE tg_bot_user_id=:tg_bot_user_id and client_type=:client_type;
            """.trimIndent(),
            mapOf(
                "tg_bot_user_id" to tgBotUserId,
                "client_type" to clientType.name,
            ),
        )
    }

    @Transactional
    fun insert(subscription: Subscription) {
        jdbcTemplate.update(
            """
                insert into subscription(tg_bot_user_id, client_type, subscription_type, subscription_date, expire_date)
                values (:tg_bot_user_id, :client_type, :subscription_type, :subscription_date, :expire_date) ON CONFLICT DO NOTHING;
            """.trimIndent(),
            subscription.parameterMap(),
        )
    }

    @Transactional
    fun insertAudit(subscription: Subscription) {
        jdbcTemplate.update(
            """
                insert into subscription_audit(tg_bot_user_id, client_type, subscription_type, subscription_date, expire_date)
                values (:tg_bot_user_id, :client_type, :subscription_type, :subscription_date, :expire_date) ON CONFLICT DO NOTHING;
            """.trimIndent(),
            subscription.parameterMap(),
        )
    }

    fun getAllForClientType(clientType: ClientType): List<Subscription> {
        return jdbcTemplate.query(
            """
                SELECT * FROM subscription where client_type=:client_type;
            """.trimIndent(),
            mapOf("client_type" to clientType.name),
            ROW_MAPPER,
        )
    }

    fun getAllNotExpired(clientType: ClientType): List<Subscription> {
        val dateFrom = localDateNowMoscow()
        return jdbcTemplate.query(
            """
                SELECT * FROM subscription where client_type=:client_type
                    and expire_date >= :date_from;
            """.trimIndent(),
            mapOf(
                "client_type" to clientType.name,
                "date_from" to dateFrom,
            ),
            ROW_MAPPER,
        )
    }

    private companion object {
        val ROW_MAPPER = RowMapper<Subscription> { rs, _ ->
            Subscription(
                tgBotUserId = rs.getLong("tg_bot_user_id"),
                clientType = ClientType.valueOf(rs.getString("client_type")),
                type = SubscriptionType.valueOf(rs.getString("subscription_type")),
                subscriptionDate = rs.getDate("subscription_date").toLocalDate(),
                expireDate = rs.getDate("expire_date").toLocalDate(),
            )
        }
    }

    private fun Subscription.parameterMap() = mapOf(
        "tg_bot_user_id" to this.tgBotUserId,
        "client_type" to this.clientType.name,
        "subscription_type" to this.type.name,
        "subscription_date" to Date.valueOf(this.subscriptionDate),
        "expire_date" to Date.valueOf(this.expireDate),
    )
}
