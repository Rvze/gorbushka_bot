package com.tsypk.coresupplier.services.apple.airpods

import com.tsypk.coresupplier.controller.dto.airpods.AirPodsFindBestRequest
import com.tsypk.coresupplier.model.apple.SupplierAirPods
import com.tsypk.coresupplier.repository.apple.airpods.SupplierAirPodsRepository
import org.springframework.stereotype.Service

@Service
class AirPodsService(
    private val supplierAirPodsRepository: SupplierAirPodsRepository,
) {
    fun updateAllForSupplier(supplierId: Long, airpods: List<SupplierAirPods>) {
        supplierAirPodsRepository.deleteAllBySupplierId(supplierId)
        supplierAirPodsRepository.batchInsertForSupplier(supplierId, airpods)
    }

    fun findBestPrices(request: AirPodsFindBestRequest): List<SupplierAirPods> {
        return if (request.country == null) {
            supplierAirPodsRepository.findByModelAndColor(
                model = request.model,
                color = request.color,
            )
        } else {
            supplierAirPodsRepository.findByModelAndColorWithCountry(
                model = request.model,
                color = request.color,
                country = request.country,
            )
        }
    }

    fun getAll(): List<SupplierAirPods> {
        return supplierAirPodsRepository.getAll()
    }

    @Deprecated("Use for only scheduled db update")
    fun truncateSuppliersIphones() {
        supplierAirPodsRepository.truncateTable()
    }
}
