package com.tsypk.coresupplier.repository.apple.iphone

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import recognitioncommons.models.apple.AppleColor
import recognitioncommons.models.apple.iphone.IphoneFullModel
import recognitioncommons.models.apple.iphone.IphoneMemory
import recognitioncommons.models.apple.iphone.IphoneModel

@Repository
class IphoneRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
) {
    fun getAll(): List<IphoneFullModel> {
        return jdbcTemplate.query(
            """
                SELECT * FROM iphone;
            """.trimIndent(),
            ROW_MAPPER,
        )
    }

    private companion object {
        val ROW_MAPPER = RowMapper<IphoneFullModel> { rs, _ ->
            IphoneFullModel(
                model = IphoneModel.valueOf(rs.getString("model")),
                color = AppleColor.valueOf(rs.getString("color")),
                memory = IphoneMemory.valueOf(rs.getString("memory")),
            )
        }
    }
}
