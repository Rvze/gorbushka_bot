package com.tsypk.coresupplier.controller

import com.tsypk.coresupplier.controller.dto.airpods.AirPodsFindBestRequest
import com.tsypk.coresupplier.controller.dto.airpods.AirPodsUpdateRequest
import com.tsypk.coresupplier.controller.dto.airpods.SupplierAirPodsDto
import com.tsypk.coresupplier.services.apple.airpods.AirPodsService
import com.tsypk.coresupplier.util.toSupplierAirPods
import com.tsypk.coresupplier.util.toSupplierAirPodsDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/airpods")
class AirPodsController(
    private val airPodsService: AirPodsService
) {
    @PostMapping(
        value = ["/bot-user/update"],
        produces = ["application/json"],
        consumes = ["application/json"],
    )
    fun v1AirPodsBotUserUpdate(
        @RequestBody request: AirPodsUpdateRequest,
    ): ResponseEntity<Unit> {
        airPodsService.updateAllForSupplier(
            supplierId = request.supplierId,
            airpods = request.airpods.map { it.toSupplierAirPods(request.supplierId) }
        )
        return ResponseEntity.ok(Unit)
    }

    @PostMapping(
        value = ["/find/best"],
        produces = ["application/json"],
        consumes = ["application/json"],
    )
    fun v1AirPodsFindBest(
        @RequestBody request: AirPodsFindBestRequest,
    ): ResponseEntity<List<SupplierAirPodsDto>> {
        val airPods = airPodsService.findBestPrices(request)
        return ResponseEntity.ok(airPods.map { it.toSupplierAirPodsDto() })
    }

    @PostMapping(
        value = ["/get/all"],
        produces = ["application/json"],
        consumes = ["application/json"],
    )
    fun v1AirPodsGetAll(): ResponseEntity<List<SupplierAirPodsDto>> {
        val airPods = airPodsService.getAll()
        return ResponseEntity.ok(airPods.map { it.toSupplierAirPodsDto() })
    }

    @PostMapping(
        value = ["/truncate"],
        produces = ["application/json"],
        consumes = ["application/json"],
    )
    fun v1AirPodsTruncate(
    ): ResponseEntity<Unit> {
        airPodsService.truncateSuppliersIphones()
        return ResponseEntity.ok(Unit)
    }
}
