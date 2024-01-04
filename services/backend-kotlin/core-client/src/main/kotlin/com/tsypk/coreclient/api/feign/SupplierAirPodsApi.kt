package com.tsypk.coreclient.api.feign

import com.tsypk.coreclient.api.feign.dto.airpods.AirPodsBotUserUpdateRequest
import com.tsypk.coreclient.api.feign.dto.airpods.AirPodsFindBestRequest
import com.tsypk.coreclient.api.feign.dto.airpods.AirPodsFindBestResponseAirPodsDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(value = "supplier-airpods-api", url = "\${clients.core-supplier.url}")
interface SupplierAirPodsApi {

    @PostMapping(
        value = ["/v1/airpods/bot-user/update"],
        produces = ["application/json"],
        consumes = ["application/json"],
    )
    fun v1AirPodsBotUserUpdate(@RequestBody request: AirPodsBotUserUpdateRequest): ResponseEntity<Unit>

    @PostMapping(
        value = ["/v1/airpods/find/best"],
        produces = ["application/json"],
        consumes = ["application/json"],
    )
    fun v1AirPodsFindBest(@RequestBody request: AirPodsFindBestRequest): ResponseEntity<List<AirPodsFindBestResponseAirPodsDto>>

    @PostMapping(
        value = ["/v1/airpods/get/all"],
        produces = ["application/json"],
        consumes = ["application/json"],
    )
    fun v1AirPodsGetAll(): ResponseEntity<List<AirPodsFindBestResponseAirPodsDto>>

    @PostMapping(
        value = ["/v1/airpods/truncate"],
        produces = ["application/json"],
        consumes = ["application/json"],
    )
    fun v1AirPodsTruncate(): ResponseEntity<Unit>
}
