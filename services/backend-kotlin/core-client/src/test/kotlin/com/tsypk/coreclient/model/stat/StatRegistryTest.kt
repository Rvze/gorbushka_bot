package com.tsypk.coreclient.model.stat

import com.google.gson.Gson
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class StatRegistryTest {
    @Test
    fun `json test`() {
        val r = StatRegistry()
        r.commonStat.newUsers.add(1L)
        r.commonStat.uniqueUsers.addAll(listOf(1L, 2L))
        r.supplierStat.suppliersAddingIphones.add(2L)
        val gson = Gson()
        val json = gson.toJson(r)

        val rBack = gson.fromJson(json, StatRegistry::class.java)
        assertEquals(r, rBack)
    }
}
