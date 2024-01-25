package com.nmakarov.coresupplier.repository

import com.nmakarov.coresupplier.model.notification.Notification
import com.nmakarov.coresupplier.model.notification.NotificationStatus
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.Timestamp
import java.time.Instant

@Repository
class NotificationRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {
    fun create(notification: Notification) {
        val parameters = mapOf(
            "user_id" to notification.userId,
            "subscription_id" to notification.subscriptionId,
            "text" to notification.text,
            "status" to notification.status.name,
            "read_at" to Timestamp.from(notification.readAt),
            "created_at" to Timestamp.from(notification.createdAt),
        )

        jdbcTemplate.update(
            """
                INSERT INTO notification(user_id, subscription_id, text, status, read_at, created_at) 
                    VALUES (:user_id, :subscription_id, :text, :status, :read_at, :created_at) ON CONFLICT DO NOTHING;
            """.trimIndent(),
            parameters
        )
    }

    fun findAllForUser(userId: Long): List<Notification> {
        return jdbcTemplate.query(
            """
                SELECT * FROM notification where user_id = :user_id;
            """.trimIndent(),
            mapOf("user_id" to userId),
            ROW_MAPPER,
        ).sortedByDescending { it.createdAt }
    }

    fun markAsRead(notifications: List<Notification>) {
        notifications.forEach {
            jdbcTemplate.update(
                """
                UPDATE notification
                SET status  = :status,
                    read_at = :read_at
                WHERE user_id = :user_id
                    AND subscription_id = :subscription_id
            """.trimIndent(),
                mapOf(
                    "status" to NotificationStatus.READ.name,
                    "read_at" to Timestamp.from(Instant.now()),
                    "user_id" to it.userId,
                    "subscription_id" to it.subscriptionId,
                ),
            )
        }
    }

    private companion object {
        private val ROW_MAPPER = RowMapper { rs, _ ->
            Notification(
                id = rs.getLong("id"),
                userId = rs.getLong("user_id"),
                subscriptionId = rs.getLong("subscription_id"),
                text = rs.getString("text"),
                status = NotificationStatus.valueOf(rs.getString("status")),
                readAt = rs.getTimestamp("read_at")?.toInstant(),
                createdAt = rs.getTimestamp("created_at").toInstant(),
            )
        }
    }
}