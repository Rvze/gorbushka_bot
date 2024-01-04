package com.tsypk.coreclient.api.feign

import com.tsypk.coreclient.api.feign.dto.iphone.IphonesBotUserUpdateRequest
import com.tsypk.coreclient.api.feign.dto.iphone.IphonesFindBestRequest
import com.tsypk.coreclient.api.feign.dto.iphone.IphonesFindBestResponseIphoneDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(value = "supplier-iphones-api", url = "\${clients.core-supplier.url}")
interface SupplierIphonesApi {
    @PostMapping(
        value = ["/v1/iphones/bot-user/update"],
        produces = ["application/json"],
        consumes = ["application/json"],
    )
    fun v1IphonesBotUserUpdate(@RequestBody request: IphonesBotUserUpdateRequest): ResponseEntity<Unit>

    @PostMapping(
        value = ["/v1/iphones/find/best"],
        produces = ["application/json"],
        consumes = ["application/json"],
    )
    fun v1IphonesFindBest(@RequestBody request: IphonesFindBestRequest): ResponseEntity<List<IphonesFindBestResponseIphoneDto>>

    @PostMapping(
        value = ["/v1/iphones/truncate"],
        produces = ["application/json"],
        consumes = ["application/json"],
    )
    fun v1IphonesTruncate(): ResponseEntity<Unit>
}
