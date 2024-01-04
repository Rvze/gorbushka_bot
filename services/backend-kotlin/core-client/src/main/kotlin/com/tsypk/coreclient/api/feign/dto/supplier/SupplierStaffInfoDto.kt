package com.tsypk.coreclient.api.feign.dto.supplier

import com.fasterxml.jackson.annotation.JsonProperty

data class SupplierStaffInfoDto(
    @field:JsonProperty("supplier_id", required = true) val supplierId: Long,
    @field:JsonProperty("staff_info", required = true) val staffInfo: StaffInfo,
)

data class StaffInfo(
    @field:JsonProperty("iphones", required = true) val iphones: Long = 0,
    @field:JsonProperty("airpods", required = true) val airPods: Long = 0,
    @field:JsonProperty("macbooks", required = true) val macBooks: Long = 0,
    @field:JsonProperty("playstations", required = true) val playstations: Long = 0,
)
