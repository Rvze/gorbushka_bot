package com.tsypk.coresupplier.controller

import com.tsypk.coresupplier.controller.dto.iphone.IphonesFindBestRequest
import com.tsypk.coresupplier.controller.dto.iphone.IphonesUpdateRequest
import com.tsypk.coresupplier.controller.dto.iphone.SupplierIphoneDto
import com.tsypk.coresupplier.services.apple.iphone.IphoneService
import com.tsypk.coresupplier.util.toSupplierIphone
import com.tsypk.coresupplier.util.toSupplierIphoneDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/iphones")
class IphonesController(
    private val iphoneService: IphoneService,
) {

    @PostMapping(
        value = ["/bot-user/update"],
        produces = ["application/json"],
        consumes = ["application/json"],
    )
    fun v1IphonesBotUserUpdate(
        @RequestBody request: IphonesUpdateRequest,
    ): ResponseEntity<Unit> {
        iphoneService.updateAllForSupplier(
            supplierId = request.supplierId,
            iphones = request.iphones.map { it.toSupplierIphone(request.supplierId) },
        )
        return ResponseEntity.ok(Unit)
    }

    @PostMapping(
        value = ["/find/best"],
        produces = ["application/json"],
        consumes = ["application/json"],
    )
    fun v1IphonesFindBest(
        @RequestBody request: IphonesFindBestRequest,
    ): ResponseEntity<List<SupplierIphoneDto>> {
        val iphones = iphoneService.findBestPrices(request)
        return ResponseEntity.ok(iphones.map { it.toSupplierIphoneDto() })
    }

    @PostMapping(
        value = ["/truncate"],
        produces = ["application/json"],
        consumes = ["application/json"],
    )
    fun v1IphonesTruncate(): ResponseEntity<Unit> {
        iphoneService.truncateSuppliersIphones()
        return ResponseEntity.ok(Unit)
    }
}
