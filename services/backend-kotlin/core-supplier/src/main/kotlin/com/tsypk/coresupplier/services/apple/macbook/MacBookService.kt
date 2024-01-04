package com.tsypk.coresupplier.services.apple.macbook

import com.tsypk.coresupplier.controller.dto.macbook.MacbookFindBestRequest
import com.tsypk.coresupplier.controller.dto.macbook.MacbookUpdateRequest
import com.tsypk.coresupplier.controller.dto.macbook.SupplierMacbookDto
import com.tsypk.coresupplier.model.apple.SupplierMacbook
import com.tsypk.coresupplier.repository.apple.macbook.SupplierMacBookRepository
import com.tsypk.coresupplier.util.toSupplierMacbookDtoMapper
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