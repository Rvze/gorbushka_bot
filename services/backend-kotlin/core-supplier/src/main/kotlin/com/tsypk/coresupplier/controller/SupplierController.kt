package com.tsypk.coresupplier.controller

import com.tsypk.coresupplier.controller.dto.supplier.SupplierStaffInfoDto
import com.tsypk.coresupplier.services.SupplierStaffService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/supplier")
class SupplierController(
    private val supplierStaffService: SupplierStaffService,
) {
    @PostMapping(
        value = ["/staff/list"],
        produces = ["application/json"],
        consumes = ["application/json"],
    )
    fun v1IphonesBotUserUpdate(): ResponseEntity<List<SupplierStaffInfoDto>> {
        val found = supplierStaffService.getStaffInfo()
        return ResponseEntity.ok(found)
    }
}
