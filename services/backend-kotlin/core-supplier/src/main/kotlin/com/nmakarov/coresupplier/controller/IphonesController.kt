package com.nmakarov.coresupplier.controller

import com.nmakarov.coreclient.model.notification.StuffUpdateBatchEvent
import com.nmakarov.coresupplier.controller.dto.iphone.IphonesFindBestRequest
import com.nmakarov.coresupplier.controller.dto.iphone.IphonesUpdateRequest
import com.nmakarov.coresupplier.controller.dto.iphone.SupplierIphoneDto
import com.nmakarov.coresupplier.services.apple.iphone.IphoneService
import com.nmakarov.coresupplier.services.messaging.StuffUpdatePublisher
import com.nmakarov.coresupplier.util.toSupplierIphone
import com.nmakarov.coresupplier.util.toSupplierIphoneDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/iphones")
class IphonesController(
    private val iphoneService: IphoneService,
    private val stuffUpdatePublisher: StuffUpdatePublisher
) {

    @PostMapping(
        value = ["/bot-user/update"],
        produces = ["application/json"],
        consumes = ["application/json"],
    )
    fun v1IphonesBotUserUpdate(
        @RequestBody request: IphonesUpdateRequest,
    ): ResponseEntity<Unit> {
        val updateEvent = iphoneService.updateAllForSupplier(
            supplierId = request.supplierId,
            iphones = request.iphones.map { it.toSupplierIphone(request.supplierId) },
        )
        stuffUpdatePublisher.publish(StuffUpdateBatchEvent(updateEvent))
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

    @GetMapping(
        value = ["/{supplier_id}"],
        produces = ["application/json"],
        consumes = ["application/json"],
    )
    fun v1IphonesBySupplierId(@PathVariable("supplier_id") supplierId: Long): ResponseEntity<List<SupplierIphoneDto>> {
        val iphones = iphoneService.findBySupplierId(supplierId)
        return ResponseEntity.ok(iphones.map { it.toSupplierIphoneDto() })
    }
}
