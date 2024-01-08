package com.nmakarov.coreclient.service.client

import com.nmakarov.coreclient.util.toMacbooksBotUserUpdateRequestDto
import org.springframework.stereotype.Service
import recognitioncommons.models.apple.AppleColor
import recognitioncommons.models.apple.macbook.Macbook
import recognitioncommons.models.apple.macbook.MacbookMemory
import recognitioncommons.models.apple.macbook.MacbookModel
import recognitioncommons.models.apple.macbook.MacbookRam
import recognitioncommons.models.country.Country
import recognitioncommons.util.extractor.macbookFullModelFromId

@Service
class SupplierMacbooksClientService(
    private val supplierMacbooksApi: com.nmakarov.coreclient.api.feign.SupplierMacbooksApi
) {
    private companion object {
        const val DEFAULT_RETRY_COUNT = 2
    }

    fun updateSupplierMacbooks(supplierId: Long, macbooks: List<Macbook>, currentRetryCount: Int = 0) {
        try {
            val listOfMacbooks = macbooks.map { it.toMacbooksBotUserUpdateRequestDto() }
            supplierMacbooksApi
                .v1MacbookUpdate(
                    request = com.nmakarov.coreclient.api.feign.dto.macbook.MacbookBotUserUpdateRequestDto(
                        supplierId = supplierId,
                        macbooks = listOfMacbooks
                    )
                )
        } catch (e: Exception) {
            if (currentRetryCount >= DEFAULT_RETRY_COUNT) {
                throw e
            }
            return updateSupplierMacbooks(supplierId, macbooks, currentRetryCount + 1)
        }
    }

    fun getBestMacbooks(
        model: MacbookModel?,
        ram: MacbookRam?,
        memory: MacbookMemory?,
        color: AppleColor?,
        country: Country?,
        currentRetryCount: Int = 0
    ): List<Macbook> {
        return try {
            val requestBody =
                com.nmakarov.coreclient.api.feign.dto.macbook.MacbookFindBestRequest(model, ram, memory, color, country)
            supplierMacbooksApi.v1MacbookFindBest(requestBody).body!!.map { it.toMacbook() }
        } catch (e: Exception) {
            if (currentRetryCount >= DEFAULT_RETRY_COUNT)
                throw e
            getBestMacbooks(model, ram, memory, color, country, currentRetryCount + 1)
        }
    }

    @Deprecated("Use for only scheduled db update")
    fun truncateSuppliersMacbooks(currentRetryCount: Int = 0) {
        try {
            supplierMacbooksApi.v1MacbookTruncate()
        } catch (e: Exception) {
            if (currentRetryCount >= DEFAULT_RETRY_COUNT) {
                throw e
            }
            truncateSuppliersMacbooks(currentRetryCount + 1)
        }
    }

    private fun com.nmakarov.coreclient.api.feign.dto.macbook.MacbookFindBestResponseMacbookDto.toMacbook(): Macbook {
        val (model, ram, memory, color) = macbookFullModelFromId(this.id)
        return Macbook(
            model = model,
            ram = ram,
            memory = memory,
            color = color,
            price = this.money.amount.toInt(),
            country = this.country,
            supplierId = this.supplierId
        )
    }
}