package com.nmakarov.coresupplier.services.apple.iphone

import com.nmakarov.coresupplier.controller.dto.iphone.IphonesFindBestRequest
import com.nmakarov.coresupplier.model.apple.SupplierIphone
import com.nmakarov.coresupplier.repository.apple.iphone.SupplierIphoneRepository
import org.springframework.stereotype.Service

@Service
class IphoneService(
    private val supplierIphoneRepository: SupplierIphoneRepository,
) {
    fun updateAllForSupplier(supplierId: Long, iphones: List<SupplierIphone>) {
        supplierIphoneRepository.deleteAllBySupplierId(supplierId)
        supplierIphoneRepository.batchInsertForSupplier(supplierId, iphones)
    }

    fun findBestPrices(request: IphonesFindBestRequest): List<SupplierIphone> {
        // if (request.color == null || request.memory == null) {
        return supplierIphoneRepository.getAllLike(request.model, request.memory, request.color, request.country)
        // }

        // return supplierIphoneRepository.getAllByFullModel(
        //     iphoneFullModel = IphoneFullModel(
        //         model = request.model,
        //         memory = request.memory,
        //         color = request.color,
        //         country = request.country,
        //     )
        // )
    }

    @Deprecated("Use for only scheduled db update")
    fun truncateSuppliersIphones() {
        supplierIphoneRepository.truncateTable()
    }
}
