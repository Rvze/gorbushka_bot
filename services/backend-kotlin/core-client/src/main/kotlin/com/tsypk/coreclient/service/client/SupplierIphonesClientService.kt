package com.tsypk.coreclient.service.client

import com.tsypk.coreclient.api.feign.SupplierIphonesApi
import com.tsypk.coreclient.api.feign.dto.iphone.IphonesBotUserUpdateRequest
import com.tsypk.coreclient.api.feign.dto.iphone.IphonesFindBestRequest
import com.tsypk.coreclient.api.feign.dto.iphone.IphonesFindBestResponseIphoneDto
import com.tsypk.coreclient.util.toIphonesBotUserUpdateRequestIphoneDto
import org.springframework.stereotype.Service
import recognitioncommons.models.apple.AppleColor
import recognitioncommons.models.apple.iphone.Iphone
import recognitioncommons.models.apple.iphone.IphoneMemory
import recognitioncommons.models.apple.iphone.IphoneModel
import recognitioncommons.models.country.Country
import recognitioncommons.util.extractor.iphoneFullModelFromId

@Service
class SupplierIphonesClientService(
    private val supplierIphonesApi: SupplierIphonesApi,
) {
    private companion object {
        const val DEFAULT_RETRY_COUNT = 2
    }

    fun updateSupplierIphones(supplierId: Long, iphones: List<Iphone>, currentRetryCount: Int = 0) {
        try {
            supplierIphonesApi.v1IphonesBotUserUpdate(
                request = IphonesBotUserUpdateRequest(
                    supplierId = supplierId,
                    iphones = iphones.map { it.toIphonesBotUserUpdateRequestIphoneDto() },
                )
            )
        } catch (e: Exception) {
            if (currentRetryCount >= DEFAULT_RETRY_COUNT) {
                throw e
            }
            return updateSupplierIphones(supplierId, iphones, currentRetryCount + 1)
        }
    }

    fun iphonesFindBest(
        model: IphoneModel?,
        memory: IphoneMemory?,
        color: AppleColor?,
        country: Country?,
        currentRetryCount: Int = 0
    ): List<Iphone> {
        return try {
            val requestBody = IphonesFindBestRequest(model, memory, color, country)
            supplierIphonesApi.v1IphonesFindBest(requestBody)
                .body!!
                .map { it.toIphone() }
        } catch (e: Exception) {
            if (currentRetryCount >= DEFAULT_RETRY_COUNT) {
                throw e
            }
            iphonesFindBest(model, memory, color, country, currentRetryCount + 1)
        }
    }

    @Deprecated("Use for only scheduled db update")
    fun truncateSuppliersIphones(currentRetryCount: Int = 0) {
        try {
            supplierIphonesApi.v1IphonesTruncate()
        } catch (e: Exception) {
            if (currentRetryCount >= DEFAULT_RETRY_COUNT) {
                throw e
            }
            truncateSuppliersIphones(currentRetryCount + 1)
        }
    }

    private fun IphonesFindBestResponseIphoneDto.toIphone(): Iphone {
        val (model, memory, color) = iphoneFullModelFromId(this.id)
        return Iphone(
            model = model,
            memory = memory,
            color = color,
            price = this.money.amount.toInt(),
            country = this.country,
            supplierId = this.supplierId,
        )
    }
}
