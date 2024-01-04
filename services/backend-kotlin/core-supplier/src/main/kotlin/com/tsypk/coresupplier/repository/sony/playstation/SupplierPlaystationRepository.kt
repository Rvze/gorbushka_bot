package com.tsypk.coresupplier.repository.sony.playstation

import com.tsypk.coresupplier.model.sony.SupplierPlaystation
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import recognitioncommons.models.country.Country
import java.sql.PreparedStatement
import java.sql.Timestamp

@Repository
class SupplierPlaystationRepository(
    private val jdbcTemplate: JdbcTemplate
) {
    @Transactional
    fun batchInsertOrUpdate(supplierId: Long, args: List<SupplierPlaystation>, batchSize: Int = 100) {
        if (args.isEmpty())
            return
        if (batchSize <= 0)
            throw IllegalArgumentException()

        jdbcTemplate.batchUpdate(
            "insert into playstation_supplier(supplier_id, playstation_id, country, price_amount, price_currency, modified_at) VALUES " +
                    "(?,?,?,?,?,?) on conflict do nothing;".trimIndent(),
            args,
            batchSize
        ) { ps: PreparedStatement, playstation: SupplierPlaystation ->
            var index = 1
            ps.setLong(index++, playstation.supplierId)
            ps.setString(index++, playstation.id)
            ps.setString(index++, playstation.country.name)
            ps.setBigDecimal(index++, playstation.priceAmount)
            ps.setString(index++, playstation.priceCurrency)
            ps.setTimestamp(index++, Timestamp.from(playstation.modifiedAt))
        }
    }

    fun deleteAllBySupplierId(supplierId: Long) {
        jdbcTemplate.update(
            """
            delete from playstation_supplier where supplier_id =:supplierId
        """.trimIndent()
        )
    }

    fun getAll(): List<SupplierPlaystation> {
        return jdbcTemplate.query(
            """
            select * from playstation_supplier
        """.trimIndent(), ROW_MAPPER
        )
    }

    private companion object {
        private val ROW_MAPPER = RowMapper { rs, _ ->
            SupplierPlaystation(
                supplierId = rs.getLong("supplier_id"),
                id = rs.getString("playstation_id"),
                country = Country.valueOf(rs.getString("country")),
                priceAmount = rs.getBigDecimal("price_amount"),
                priceCurrency = rs.getString("price_currency")
            )
        }
    }
}