package com.nmakarov.coreclient.api.feign

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(value = "supplier-api", url = "\${clients.core-supplier.url}")
interface SupplierApi {
    @PostMapping(
        value = ["v1/supplier/staff/list"],
        produces = ["application/json"],
        consumes = ["application/json"],
    )
    fun v1SupplierStaffList(): ResponseEntity<List<com.nmakarov.coreclient.api.feign.dto.supplier.SupplierStaffInfoDto>>
}
