package com.nmakarov.coreclient.service.client

import org.springframework.stereotype.Service

@Service
class SupplierClientService(
    private val supplierApi: com.nmakarov.coreclient.api.feign.SupplierApi,
) {
    private companion object {
        const val DEFAULT_RETRY_COUNT = 2
    }

    fun getSupplierStaffInfo(currentRetryCount: Int = 0): List<com.nmakarov.coreclient.api.feign.dto.supplier.SupplierStaffInfoDto> {
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
