package com.nmakarov.coresupplier.repository

import com.nmakarov.coreclient.model.notification.NotificationSubscription
import com.nmakarov.coreclient.model.notification.NotificationType
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class NotificationSubscriptionRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
) {
    fun getAllForUser(userId: Long): List<NotificationSubscription> {
        return jdbcTemplate.query(
            """
            SELECT * FROM notification_subscription
            WHERE user_id = :user_id;
        """.trimIndent(), mapOf("user_id" to userId), ROW_MAPPER
        )
    }

    fun create(notificationSubscription: NotificationSubscription) {
        jdbcTemplate.update(
            """
            INSERT INTO notification_subscription (user_id, entity_id, type) VALUES
            (:user_id, :entity_id, :type) ON CONFLICT DO NOTHING;
        """.trimIndent(), mapOf(
                "user_id" to notificationSubscription.userId,
                "entity_id" to notificationSubscription.entityId,
                "type" to notificationSubscription.type.name
            )
        )
    }

    fun delete(userId: Long, entityId: String, type: NotificationType) {
        jdbcTemplate.update(
            """
            DELETE FROM notification_subscription WHERE
            user_id = :user_id AND entity_id = :entity_id AND type = :type
        """.trimIndent(),
            mapOf(
                "user_id" to userId,
                "entity_id" to entityId,
                "type" to type
            )
        )

    }

    private companion object {
        private val ROW_MAPPER = RowMapper { rs, _ ->
            NotificationSubscription(
                id = rs.getLong("id"),
                userId = rs.getLong("user_id"),
                entityId = rs.getString("entity_id"),
                type = NotificationType.valueOf(rs.getString("type")),
            )
        }
    }
}