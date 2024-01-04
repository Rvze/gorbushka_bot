package com.tsypk.coresupplier.repository.apple.macbook

import com.tsypk.coresupplier.controller.dto.macbook.MacbookFindBestRequest
import com.tsypk.coresupplier.model.apple.SupplierMacbook
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import recognitioncommons.models.apple.AppleColor
import recognitioncommons.models.apple.macbook.MacbookMemory
import recognitioncommons.models.apple.macbook.MacbookModel
import recognitioncommons.models.apple.macbook.MacbookRam
import recognitioncommons.models.country.Country
import java.sql.PreparedStatement
import java.sql.Timestamp

@Repository
class SupplierMacBookRepository(
    private val jdbcTemplate: JdbcTemplate,
    private val namedJdbcTemplate: NamedParameterJdbcTemplate,
) {
    @Transactional
    fun batchUpdateMacbooks(supplierId: Long, macbooks: List<SupplierMacbook>, batchSize: Int = 100) {
        if (macbooks.isEmpty())
            return
        if (batchSize <= 0)
            throw IllegalArgumentException()
        jdbcTemplate.batchUpdate(
            """
            insert into supplier_macbook(mac_id, supplier_id, country, price_amount, price_currency, modified_at) 
            VALUES (?,?,?,?,?,?) on conflict do nothing;
        """.trimIndent(), macbooks, batchSize
        ) { ps: PreparedStatement, macbook: SupplierMacbook ->
            var index = 1
            ps.setString(index++, macbook.macId)
            ps.setLong(index++, supplierId)
            ps.setString(index++, macbook.country.name)
            ps.setBigDecimal(index++, macbook.priceAmount)
            ps.setString(index++, macbook.priceCurrency)
            ps.setTimestamp(index++, Timestamp.from(macbook.modifiedAt))
        }
    }

    @Transactional
    fun deleteAllBySupplierId(supplierId: Long) {
        jdbcTemplate.update(
            """
            delete from supplier_macbook where supplier_id=?
        """.trimIndent(), supplierId
        )
    }

    fun getAllLike(request: MacbookFindBestRequest): List<SupplierMacbook> {
        val template = getTemplate(request.model, request.ram, request.memory, request.color)
        if (request.country == null) {
            return namedJdbcTemplate.query(
                """
            select * from supplier_macbook
            where mac_id like '$template'
        """.trimIndent(),
                ROW_MAPPER
            )
        } else {
            return namedJdbcTemplate.query(
                """
                select * from supplier_macbook
                            where mac_id like '$template'
                            and country = '${request.country.name.uppercase()}'
            """.trimIndent(), mapOf("country" to request.country.name), ROW_MAPPER
            )
        }
    }

    @Deprecated("Use for only scheduled db update")
    fun truncateTable() {
        jdbcTemplate.update(
            """
                TRUNCATE TABLE supplier_iphone;
            """.trimIndent(),
        )
    }

    private fun getTemplate(
        model: MacbookModel?,
        macbookRam: MacbookRam?,
        macbookMemory: MacbookMemory?,
        macbookColor: AppleColor?
    ): String {
        val modelTemplate = model?.name ?: PERCENT
        val ramTemplate = macbookRam?.name ?: PERCENT
        val memoryTemplate = macbookMemory?.name ?: PERCENT
        val colorTemplate = macbookColor?.name ?: PERCENT
        return "${modelTemplate}/${memoryTemplate}/${ramTemplate}/${colorTemplate}"
    }

    companion object {
        const val PERCENT = "%"

        private val ROW_MAPPER = RowMapper { rs, _ ->
            SupplierMacbook(
                supplierId = rs.getLong("supplier_id"),
                macId = rs.getString("mac_id"),
                country = Country.valueOf(rs.getString("country")),
                priceAmount = rs.getBigDecimal("price_amount"),
                priceCurrency = rs.getString("price_currency")
            )
        }
    }
}