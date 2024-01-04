package com.tsypk.coreclient.repository

import com.tsypk.coreclient.model.Supplier
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp

@Repository
class SupplierRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
) {

    @Transactional
    fun createOnConflictDoNothing(supplier: Supplier) {
        jdbcTemplate.update(
            """
                insert into supplier (id, username, title, created_at)
                values (:id, :username, :title, :created_at) ON CONFLICT DO NOTHING;
            """.trimIndent(),
            supplier.parameterMap(),
        )
    }

    fun findById(id: Long): Supplier? {
        return jdbcTemplate.query(
            """
                SELECT * FROM SUPPLIER WHERE id=:id;
            """.trimIndent(),
            mapOf("id" to id),
            ROW_MAPPER,
        ).singleOrNull()
    }

    fun findByUsername(username: String): Supplier? {
        return jdbcTemplate.query(
            """
                SELECT * FROM SUPPLIER WHERE username=:username;
            """.trimIndent(),
            mapOf("username" to username),
            ROW_MAPPER,
        ).singleOrNull()
    }

    @Transactional
    fun delete(supplier: Supplier) {
        jdbcTemplate.update(
            """
                DELETE FROM supplier WHERE id=:id
            """.trimIndent(),
            mapOf("id" to supplier.id),
        )
    }

    fun getAllWhereIdIn(ids: List<Long>): List<Supplier> {
        if (ids.isEmpty()) {
            return emptyList()
        }
        val idsString = ids.joinToString(",") { it.toString() }
        return jdbcTemplate.query(
            """
                SELECT * FROM supplier 
                    WHERE id IN ($idsString);
            """.trimIndent(),
            ROW_MAPPER,
        )
    }

    fun getAll(): List<Supplier> {
        return jdbcTemplate.query(
            """
                select * from supplier;
            """.trimIndent(),
            ROW_MAPPER,
        )
    }

    private companion object {
        val ROW_MAPPER = RowMapper<Supplier> { rs, _ ->
            Supplier(
                id = rs.getLong("id"),
                username = rs.getString("username"),
                title = rs.getString("title"),
            ).apply {
                this.createdAt = rs.getTimestamp("created_at").toInstant()
            }
        }
    }

    private fun Supplier.parameterMap() = mapOf(
        "id" to this.id,
        "username" to this.username,
        "title" to this.title,
        "created_at" to Timestamp.from(this.createdAt),
    )
}
