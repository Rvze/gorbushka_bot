package com.tsypk.coresupplier.repository.apple.airpods

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import recognitioncommons.models.apple.AppleColor
import recognitioncommons.models.apple.airpods.AirPodsFullModel
import recognitioncommons.models.apple.airpods.AirPodsModel

@Repository
class AirPodsRepository(
    private val jdbcTemplate: JdbcTemplate
) {
    fun getAll(): List<AirPodsFullModel> {
        return jdbcTemplate.query(
            """
                SELECT * FROM airpods;
            """.trimIndent(),
            ROW_MAPPER
        )
    }

    private companion object {
        val ROW_MAPPER = RowMapper<AirPodsFullModel> { rs, _ ->
            AirPodsFullModel(
                model = AirPodsModel.valueOf(rs.getString("model")),
                color = AppleColor.valueOf(rs.getString("color")),
            )
        }
    }
}