package com.tsypk.coresupplier.repository.apple.airpods

import com.tsypk.coresupplier.model.apple.SupplierAirPods
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import recognitioncommons.models.apple.AppleColor
import recognitioncommons.models.apple.airpods.AirPodsModel
import recognitioncommons.models.country.Country
import java.sql.Timestamp

@Repository
class SupplierAirPodsRepository(
    private val jdbcTemplate: JdbcTemplate,
    private val namedJdbcTemplate: NamedParameterJdbcTemplate,
) {

    @Transactional
    fun batchInsertForSupplier(supplierId: Long, airpods: List<SupplierAirPods>, batchSize: Int = 100) {
        if (airpods.isEmpty()) {
            return
        }
        if (batchSize <= 0) {
            throw IllegalArgumentException()
        }

        jdbcTemplate.batchUpdate(
            """
                INSERT INTO supplier_airpods(supplier_id, airpods_id, country, price_amount, price_currency, modified_at)
                    VALUES (?,?,?,?,?,?)
                        ON CONFLICT DO NOTHING;
            """.trimIndent(),
            airpods,
            batchSize
        ) { ps, supplierAirPods: SupplierAirPods ->
            var index = 1
            ps.setLong(index++, supplierId)
            ps.setString(index++, supplierAirPods.id)
            ps.setString(index++, supplierAirPods.country.name)
            ps.setBigDecimal(index++, supplierAirPods.priceAmount)
            ps.setString(index++, supplierAirPods.priceCurrency)
            ps.setTimestamp(index++, Timestamp.from(supplierAirPods.modifiedAt))
        }
    }

    fun findByModelAndColorWithCountry(
        model: AirPodsModel?,
        color: AppleColor?,
        country: Country,
    ): List<SupplierAirPods> {
        val airPodsIdTemplate = idTemplate(model, color)
        return namedJdbcTemplate.query(
            """
                SELECT * FROM supplier_airpods
                    WHERE airpods_id LIKE :airpods_id
                        AND country=:country;
            """.trimIndent(),
            mapOf(
                "airpods_id" to airPodsIdTemplate,
                "country" to country.name,
            ),
            ROW_MAPPER,
        )
    }

    fun findByModelAndColor(
        model: AirPodsModel?,
        color: AppleColor?,
    ): List<SupplierAirPods> {
        val airPodsIdTemplate = idTemplate(model, color)
        return namedJdbcTemplate.query(
            """
                SELECT * FROM supplier_airpods
                    WHERE airpods_id LIKE :airpods_id;
            """.trimIndent(),
            mapOf("airpods_id" to airPodsIdTemplate),
            ROW_MAPPER,
        )
    }

    @Transactional
    fun deleteAllBySupplierId(supplierId: Long) {
        jdbcTemplate.update(
            """
                DELETE FROM supplier_airpods where supplier_id=?;
            """.trimIndent(),
            *arrayOf<Any>(supplierId),
        )
    }

    fun getAll(): List<SupplierAirPods> {
        return namedJdbcTemplate.query(
            """
                SELECT * FROM supplier_airpods;
            """.trimIndent(),
            ROW_MAPPER,
        )
    }

    @Transactional
    @Deprecated("Use for only scheduled db update")
    fun truncateTable() {
        jdbcTemplate.update(
            """
                TRUNCATE TABLE supplier_airpods;
            """.trimIndent(),
        )
    }

    private companion object {
        private val ROW_MAPPER = RowMapper { rs, _ ->
            SupplierAirPods(
                supplierId = rs.getLong("supplier_id"),
                id = rs.getString("airpods_id"),
                country = Country.valueOf(rs.getString("country")),
                priceAmount = rs.getBigDecimal("price_amount"),
                priceCurrency = rs.getString("price_currency"),
            )
        }

        private fun idTemplate(model: AirPodsModel?, color: AppleColor?): String {
            if (model == null && (color == null || color == AppleColor.DEFAULT)) {
                return "%"
            } else if (model == null && color != null) {
                return "%/${color.name}"
            } else if (model == null) {
                return "%"
            }

            return if (color == null || color == AppleColor.DEFAULT) {
                "${model.name}%"
            } else {
                "${model.name}/${color.name}"
            }
        }
    }
}
