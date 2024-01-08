package com.nmakarov.coresupplier.services.sony.playstation

import com.nmakarov.coresupplier.controller.dto.playstation.PlayStationFindBestPricesDto
import com.nmakarov.coresupplier.model.sony.SupplierPlaystation
import com.nmakarov.coresupplier.repository.sony.playstation.SupplierPlaystationRepository
import org.springframework.stereotype.Service

@Service
class PlaystationService(
    private val supplierPlaystationRepository: SupplierPlaystationRepository
) {
    fun updateAllForSupplier(supplierId: Long, args: List<SupplierPlaystation>) {
        supplierPlaystationRepository.deleteAllBySupplierId(supplierId)
        supplierPlaystationRepository.batchInsertOrUpdate(supplierId, args)
    }

    fun getAll(): List<SupplierPlaystation> {
        return supplierPlaystationRepository.getAll()
    }

    //TODO
    fun findBestPrices(request: PlayStationFindBestPricesDto): List<SupplierPlaystation> {
        return listOf()
    }
}