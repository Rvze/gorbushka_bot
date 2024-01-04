package com.tsypk.coreclient.repository

import com.google.gson.Gson
import com.tsypk.coreclient.model.stat.Stat
import com.tsypk.coreclient.model.stat.StatRegistry
import com.tsypk.coreclient.util.localDateNowMoscow
import org.jetbrains.annotations.TestOnly
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Repository
class StatRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
) {
    fun get(atDate: LocalDate = localDateNowMoscow()): Stat? {
        return jdbcTemplate.query(
            """
                SELECT * FROM stats where at_date=:at_date;
            """.trimIndent(),
            mapOf("at_date" to atDate),
            ROW_MAPPER,
        ).singleOrNull()
    }

    @TestOnly
    fun getAllWithin(dateFrom: LocalDate, dateTo: LocalDate): List<Stat> {
        return jdbcTemplate.query(
            """
                SELECT * FROM stats where at_date >= :dateFrom
                    and at_date <= :dateTo;
            """.trimIndent(),
            mapOf(
                "dateFrom" to dateFrom,
                "dateTo" to dateTo,
            ),
            ROW_MAPPER,
        )
    }

    @Transactional
    fun insert(stat: Stat) {
        jdbcTemplate.update(
            """
                INSERT INTO stats(at_date, raw_json) 
                    VALUES(:at_date, :raw_json) ON CONFLICT DO NOTHING; 
            """.trimIndent(),
            mapOf(
                "at_date" to localDateNowMoscow(),
                "raw_json" to gson.toJson(stat.statRegistry),
            ),
        )
    }

    @Transactional
    fun update(stat: Stat) {
        jdbcTemplate.update(
            """
                UPDATE stats SET raw_json=:raw_json
                    WHERE at_date=:at_date;
            """.trimIndent(),
            mapOf(
                "at_date" to localDateNowMoscow(),
                "raw_json" to gson.toJson(stat.statRegistry),
            )
        )
    }

    private companion object {
        val gson = Gson()
        val ROW_MAPPER = RowMapper<Stat> { rs, _ ->
            val rawText = rs.getString("raw_json")
            Stat(
                atDate = rs.getDate("at_date").toLocalDate(),
                statRegistry = gson.fromJson(rawText, StatRegistry::class.java),
            )
        }
    }
}
