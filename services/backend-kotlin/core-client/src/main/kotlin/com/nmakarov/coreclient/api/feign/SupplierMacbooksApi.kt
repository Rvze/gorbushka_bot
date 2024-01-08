package com.nmakarov.coreclient.api.feign

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
    fun v1MacbookUpdate(@RequestBody request: com.nmakarov.coreclient.api.feign.dto.macbook.MacbookBotUserUpdateRequestDto): ResponseEntity<Unit>

    @PostMapping(
        value = ["v1/macbooks/find"],
        consumes = ["application/json"],
        produces = ["application/json"]
    )
    fun v1MacbookFindBest(@RequestBody request: com.nmakarov.coreclient.api.feign.dto.macbook.MacbookFindBestRequest): ResponseEntity<List<com.nmakarov.coreclient.api.feign.dto.macbook.MacbookFindBestResponseMacbookDto>>

    @PostMapping(
        value = ["v1/macbooks/truncate"],
        consumes = ["application/json"],
        produces = ["application/json"]
    )
    fun v1MacbookTruncate(): ResponseEntity<Unit>

}