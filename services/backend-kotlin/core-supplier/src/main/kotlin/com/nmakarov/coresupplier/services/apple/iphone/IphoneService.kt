package com.nmakarov.coresupplier.services.apple.iphone

import com.nmakarov.coreclient.model.notification.PriceUpdate
import com.nmakarov.coreclient.model.notification.StuffUpdateEvent
import com.nmakarov.coreclient.model.notification.StuffUpdateType
import com.nmakarov.coresupplier.controller.dto.iphone.IphonesFindBestRequest
import com.nmakarov.coresupplier.model.apple.SupplierIphone
import com.nmakarov.coresupplier.repository.apple.iphone.SupplierIphoneRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class IphoneService(
    private val supplierIphoneRepository: SupplierIphoneRepository,
) {
    @Transactional
    fun updateAllForSupplier(supplierId: Long, iphones: List<SupplierIphone>): List<StuffUpdateEvent> {
        val before = supplierIphoneRepository.findAllBySupplierId(supplierId).groupBy { "${it.id}/${it.country.name}" }
        supplierIphoneRepository.deleteAllBySupplierId(supplierId)
        supplierIphoneRepository.batchInsertForSupplier(supplierId, iphones)
        val after = supplierIphoneRepository.findAllBySupplierId(supplierId).groupBy { "${it.id}/${it.country.name}" }

        val result = mutableMapOf<String, StuffUpdateEvent>()
        before.forEach {
            if (it.key in after) {
                val bef = it.value.first()
                val aft = after[it.key]!!.first()

                if (bef.priceAmount != aft.priceAmount) {
                    result[it.key] = StuffUpdateEvent(
                        type = StuffUpdateType.UPDATE,
                        modelId = it.key,
                        payload = PriceUpdate(
                            oldPrice = bef.priceAmount.toLong(),
                            newPrice = aft.priceAmount.toLong()
                        )
                    )
                }
            } else {
                result[it.key] = StuffUpdateEvent(
                    type = StuffUpdateType.DELETE,
                    modelId = it.key
                )
            }
        }
        after.forEach {
            if (it.key !in before) {
                result[it.key] = StuffUpdateEvent(
                    type = StuffUpdateType.CREATE,
                    modelId = it.key
                )
            }
        }
        return result.values.toList()
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

    fun findBySupplierId(supplierId: Long): List<SupplierIphone> {
        return supplierIphoneRepository.findAllBySupplierId(supplierId)
    }

    @Deprecated("Use for only scheduled db update")
    fun truncateSuppliersIphones() {
        supplierIphoneRepository.truncateTable()
    }
}
