package com.nmakarov.coresupplier.services.apple.macbook

import com.nmakarov.coresupplier.controller.dto.macbook.MacbookFindBestRequest
import com.nmakarov.coresupplier.controller.dto.macbook.SupplierMacbookDto
import com.nmakarov.coresupplier.model.apple.SupplierMacbook
import com.nmakarov.coresupplier.repository.apple.macbook.SupplierMacBookRepository
import com.nmakarov.coresupplier.util.toSupplierMacbookDtoMapper
import org.springframework.stereotype.Service

@Service
class MacBookService(
    private val macBookRepository: SupplierMacBookRepository
) {
    fun updateAllForSupplier(supplierId: Long, macbooks: List<SupplierMacbook>) {
        macBookRepository.deleteAllBySupplierId(supplierId)
        macBookRepository.batchUpdateMacbooks(supplierId, macbooks)
    }

    fun getFindPrices(request: MacbookFindBestRequest): List<SupplierMacbookDto> {
        return macBookRepository.getAllLike(request).map { it.toSupplierMacbookDtoMapper() }
    }

    @Deprecated("Use for only scheduled db update")
    fun truncateSuppliersIphones() {
        macBookRepository.truncateTable()
    }
}