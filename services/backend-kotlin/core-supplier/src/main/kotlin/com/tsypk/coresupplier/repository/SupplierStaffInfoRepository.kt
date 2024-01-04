package com.tsypk.coresupplier.repository

import com.tsypk.coresupplier.model.SupplierStaffCount
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

@Component
class SupplierStaffInfoRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
) {
    private companion object {
        private val ROW_MAPPER = RowMapper { rs, _ ->
            SupplierStaffCount(
                supplierId = rs.getLong("supplier_id"),
                count = rs.getLong("cnt"),
            )
        }
    }

    fun getStaffCountList(tableName: String): List<SupplierStaffCount> {
        return jdbcTemplate.query(
            """
                SELECT supplier_id as supplier_id, count(*) as cnt
                    from $tableName
                        group by supplier_id;
            """.trimIndent(),
            ROW_MAPPER
        )
    }
}
