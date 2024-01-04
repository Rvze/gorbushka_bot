package com.tsypk.coreclient.service.client

import com.tsypk.coreclient.api.feign.SupplierAirPodsApi
import com.tsypk.coreclient.api.feign.dto.airpods.AirPodsBotUserUpdateRequest
import com.tsypk.coreclient.api.feign.dto.airpods.AirPodsFindBestRequest
import com.tsypk.coreclient.api.feign.dto.airpods.AirPodsFindBestResponseAirPodsDto
import com.tsypk.coreclient.util.toAirPodsBotUserUpdateRequestAirPodsDto
import org.springframework.stereotype.Service
import recognitioncommons.models.apple.AppleColor
import recognitioncommons.models.apple.airpods.AirPods
import recognitioncommons.models.apple.airpods.AirPodsModel
import recognitioncommons.models.country.Country
import recognitioncommons.util.extractor.airPodsFullModelFromId

@Service
class SupplierAirPodsClientService(
    private val supplierAirPodsApi: SupplierAirPodsApi
) {
    private companion object {
        const val DEFAULT_RETRY_COUNT = 2
    }

    fun updateAirPods(supplierId: Long, airpods: List<AirPods>, currentRetryCount: Int = 0) {
        try {
            supplierAirPodsApi.v1AirPodsBotUserUpdate(
                request = AirPodsBotUserUpdateRequest(
                    supplierId = supplierId,
                    airpods = airpods.map { it.toAirPodsBotUserUpdateRequestAirPodsDto() },
                )
            )
        } catch (e: Exception) {
            if (currentRetryCount >= DEFAULT_RETRY_COUNT) {
                throw e
            }
            return updateAirPods(supplierId, airpods, currentRetryCount + 1)
        }
    }

    fun airPodsFindBest(
        model: AirPodsModel?,
        color: AppleColor?,
        country: Country?,
        currentRetryCount: Int = 0,
    ): List<AirPods> {
        return try {
            val requestBody = AirPodsFindBestRequest(model, color, country)
            supplierAirPodsApi.v1AirPodsFindBest(requestBody)
                .body!!
                .map { it.toAirPods() }
        } catch (e: Exception) {
            if (currentRetryCount >= DEFAULT_RETRY_COUNT) {
                throw e
            }
            airPodsFindBest(model, color, country, currentRetryCount + 1)
        }
    }

    fun airPodsGetAll(currentRetryCount: Int = 0): List<AirPods> {
        return try {
            supplierAirPodsApi.v1AirPodsGetAll()
                .body!!
                .map { it.toAirPods() }
        } catch (e: Exception) {
            if (currentRetryCount >= DEFAULT_RETRY_COUNT) {
                throw e
            }
            airPodsGetAll(currentRetryCount + 1)
        }
    }

    @Deprecated("Use for only scheduled db update")
    fun truncateSuppliersAirPods(currentRetryCount: Int = 0) {
        try {
            supplierAirPodsApi.v1AirPodsTruncate()
        } catch (e: Exception) {
            if (currentRetryCount >= DEFAULT_RETRY_COUNT) {
                throw e
            }
            truncateSuppliersAirPods(currentRetryCount + 1)
        }
    }

    private fun AirPodsFindBestResponseAirPodsDto.toAirPods(): AirPods {
        val (model, color, _) = airPodsFullModelFromId(this.id)
        return AirPods(
            model = model,
            color = color,
            price = this.money.amount.toInt(),
            country = this.country,
            supplierId = this.supplierId,
        )
    }
}
