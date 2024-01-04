package com.tsypk.coreclient.service.client

import com.tsypk.coreclient.api.feign.SupplierApi
import com.tsypk.coreclient.api.feign.dto.supplier.SupplierStaffInfoDto
import org.springframework.stereotype.Service

@Service
class SupplierClientService(
    private val supplierApi: SupplierApi,
) {
    private companion object {
        const val DEFAULT_RETRY_COUNT = 2
    }

    fun getSupplierStaffInfo(currentRetryCount: Int = 0): List<SupplierStaffInfoDto> {
        return try {
            supplierApi.v1SupplierStaffList().body!!
        } catch (e: Exception) {
            if (currentRetryCount >= DEFAULT_RETRY_COUNT) {
                throw e
            }
            getSupplierStaffInfo(currentRetryCount + 1)
        }
    }
}
