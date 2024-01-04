package com.tsypk.coreclient.api.feign

import com.tsypk.coreclient.api.feign.dto.macbook.MacbookBotUserUpdateRequestDto
import com.tsypk.coreclient.api.feign.dto.macbook.MacbookFindBestRequest
import com.tsypk.coreclient.api.feign.dto.macbook.MacbookFindBestResponseMacbookDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(value = "supplier-macbooks-api", url = "\${clients.core-supplier.url}")
interface SupplierMacbooksApi {
    @PostMapping(
        value = ["v1/macbooks/update"],
        consumes = ["application/json"],
        produces = ["application/json"]
    )
    fun v1MacbookUpdate(@RequestBody request: MacbookBotUserUpdateRequestDto): ResponseEntity<Unit>

    @PostMapping(
        value = ["v1/macbooks/find"],
        consumes = ["application/json"],
        produces = ["application/json"]
    )
    fun v1MacbookFindBest(@RequestBody request: MacbookFindBestRequest): ResponseEntity<List<MacbookFindBestResponseMacbookDto>>

    @PostMapping(
        value = ["v1/macbooks/truncate"],
        consumes = ["application/json"],
        produces = ["application/json"]
    )
    fun v1MacbookTruncate(): ResponseEntity<Unit>

}