package com.tsypk.coresupplier.repository.apple.iphone

import com.tsypk.coresupplier.model.apple.SupplierIphone
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import recognitioncommons.models.apple.AppleColor
import recognitioncommons.models.apple.iphone.IphoneFullModel
import recognitioncommons.models.apple.iphone.IphoneMemory
import recognitioncommons.models.apple.iphone.IphoneModel
import recognitioncommons.models.country.Country
import recognitioncommons.util.idString
import java.sql.PreparedStatement
import java.sql.Timestamp

@Repository
class SupplierIphoneRepository(
    private val jdbcTemplate: JdbcTemplate,
    private val namedJdbcTemplate: NamedParameterJdbcTemplate,
) {
    @Transactional
    fun batchInsertForSupplier(supplierId: Long, iphones: List<SupplierIphone>, batchSize: Int = 100) {
        if (iphones.isEmpty()) {
            return
        }
        if (batchSize <= 0) {
            throw IllegalArgumentException()
        }

        jdbcTemplate.batchUpdate(
            """
                INSERT INTO supplier_iphone (supplier_id, iphone_id, country, price_amount, price_currency, modified_at)
                    VALUES (?, ?, ?, ?, ?, ?)
                        ON CONFLICT DO NOTHING;
            """.trimIndent(),
            iphones,
            batchSize,
        ) { ps: PreparedStatement, iphone: SupplierIphone ->
            var index = 1
            ps.setLong(index++, supplierId)
            ps.setString(index++, iphone.id)
            ps.setString(index++, iphone.country.name)
            ps.setBigDecimal(index++, iphone.priceAmount)
            ps.setString(index++, iphone.priceCurrency)
            ps.setTimestamp(index, Timestamp.from(iphone.modifiedAt))
        }
    }

    fun getAllLike(
        model: IphoneModel?,
        memory: IphoneMemory?,
        color: AppleColor?,
        country: Country?,
    ): List<SupplierIphone> {
        if (country != null) {
            return getAllLikeWithCountry(model, memory, color, country)
        }

        val template = idLikeTemplate(model, memory, color)
        return namedJdbcTemplate.query(
            """
                SELECT * FROM supplier_iphone
                    WHERE iphone_id LIKE '$template';
            """.trimIndent(),
            ROW_MAPPER,
        )
    }

    private fun getAllLikeWithCountry(
        model: IphoneModel?,
        memory: IphoneMemory?,
        color: AppleColor?,
        country: Country,
    ): List<SupplierIphone> {
        val template = idLikeTemplate(model, memory, color)
        return namedJdbcTemplate.query(
            """
                SELECT * FROM supplier_iphone
                    WHERE iphone_id LIKE '$template'
                        AND country=:country;
            """.trimIndent(),
            mapOf("country" to country.name),
            ROW_MAPPER,
        )
    }

    fun getAllByFullModel(iphoneFullModel: IphoneFullModel): List<SupplierIphone> {
        if (iphoneFullModel.country != null) {
            return getAllByFullModelWithCountry(iphoneFullModel)
        }
        return jdbcTemplate.query(
            selectQueryWithUnionOfCountries(iphoneFullModel.idString()),
            ROW_MAPPER,
        )
    }

    private fun getAllByFullModelWithCountry(iphoneFullModel: IphoneFullModel): List<SupplierIphone> {
        return namedJdbcTemplate.query(
            """
                SELECT * FROM supplier_iphone 
                    WHERE iphone_id=:iphone_id
                        AND country=:country
                        ORDER BY (price_currency, price_amount)
                        LIMIT $MAX_PER_COUNTRY_IPHONES;
            """.trimIndent(),
            mapOf(
                "iphone_id" to iphoneFullModel.idString(),
                "country" to iphoneFullModel.country!!.name,
            ),
            ROW_MAPPER,
        )
    }

    @Transactional
    fun deleteAllBySupplierId(supplierId: Long) {
        jdbcTemplate.update(
            """
                DELETE FROM supplier_iphone WHERE supplier_id=?;
            """.trimIndent(),
            *arrayOf<Any>(supplierId),
        )
    }

    @Transactional
    @Deprecated("Use for only scheduled db update")
    fun truncateTable() {
        jdbcTemplate.update(
            """
                TRUNCATE TABLE supplier_iphone;
            """.trimIndent(),
        )
    }

    private fun idLikeTemplate(
        model: IphoneModel?,
        memory: IphoneMemory?,
        color: AppleColor?,
    ): String {
        val modelTemplate = model?.name ?: PERCENT
        val memoryTemplate = memory?.name ?: PERCENT
        val colorTemplate = color?.name ?: PERCENT
        return "${modelTemplate}/${memoryTemplate}/${colorTemplate}"
    }

    private companion object {
        const val MAX_PER_COUNTRY_IPHONES = 5
        const val PERCENT = "%"

        private val ROW_MAPPER = RowMapper { rs, _ ->
            SupplierIphone(
                supplierId = rs.getLong("supplier_id"),
                id = rs.getString("iphone_id"),
                country = Country.valueOf(rs.getString("country")),
                priceAmount = rs.getBigDecimal("price_amount"),
                priceCurrency = rs.getString("price_currency"),
            )
        }

        private fun selectQueryWithUnionOfCountries(iphoneId: String): String {
            return Country.values().joinToString(
                prefix = """
                SELECT result.supplier_id, result.iphone_id, result.country, result.price_amount, result.price_currency, result.modified_at
                FROM (
            """.trimIndent(),
                postfix = """
            ) result
                ORDER BY (result.country, result.price_currency, result.price_amount);
            """.trimIndent(),
                separator = """
                    UNION
        """.trimIndent()
            ) {
                """

                  (SELECT supplier_id, iphone_id, country, price_amount, price_currency, modified_at
                   FROM supplier_iphone
                   WHERE iphone_id='$iphoneId'
                     AND country = '${it.name}'
                   ORDER BY (price_currency, price_amount)
                   LIMIT $MAX_PER_COUNTRY_IPHONES)

            """.trimIndent()
            }
        }
    }
}
