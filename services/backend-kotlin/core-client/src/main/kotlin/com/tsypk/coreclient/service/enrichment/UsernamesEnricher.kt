package com.tsypk.coreclient.service.enrichment

import com.tsypk.coreclient.model.stuff.SearchResult
import com.tsypk.coreclient.service.SupplierMgmtService
import com.tsypk.coreclient.service.UserMgmtService
import org.springframework.stereotype.Service

@Service
class UsernamesEnricher(
    private val userMgmtService: UserMgmtService,
    private val supplierMgmtService: SupplierMgmtService,
) {
    fun enrichSupplierUsernames(searchResult: SearchResult) {
        val allIds = searchResult.iphones.map { it.supplierId!! }
            .plus(searchResult.airPods.map { it.supplierId!! })
            .plus(searchResult.macbooks.map { it.supplierId!! })

        val usernames = supplierMgmtService.enrichUsernames(allIds)

        searchResult.iphones.forEach {
            it.apply {
                this.supplierUsername = usernames[it.supplierId!!]
            }
        }

        searchResult.airPods.forEach {
            it.apply {
                this.supplierUsername = usernames[it.supplierId!!]
            }
        }

        searchResult.macbooks.forEach {
            it.apply {
                this.supplierUsername = usernames[it.supplierId!!]
            }
        }
    }
}