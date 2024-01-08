package com.nmakarov.coresupplier.services

import com.nmakarov.coresupplier.controller.dto.supplier.StaffInfo
import com.nmakarov.coresupplier.controller.dto.supplier.SupplierStaffInfoDto
import com.nmakarov.coresupplier.repository.SupplierStaffInfoRepository
import org.springframework.stereotype.Service

@Service
class SupplierStaffService(
    private val supplierStaffInfoRepository: SupplierStaffInfoRepository
) {
    // TODO macBooks and playstations
    fun getStaffInfo(): List<SupplierStaffInfoDto> {
        val iphones = supplierStaffInfoRepository.getStaffCountList("supplier_iphone")
            .associateBy { it.supplierId }
        val airPods = supplierStaffInfoRepository.getStaffCountList("supplier_airpods")
            .associateBy { it.supplierId }
        val macBooks = supplierStaffInfoRepository.getStaffCountList("supplier_macbook")
            .associateBy { it.supplierId }

        val suppliers = airPods.keys + iphones.keys

        return suppliers.map {
            SupplierStaffInfoDto(
                supplierId = it,
                staffInfo = StaffInfo(
                    iphones = iphones[it]?.count ?: 0,
                    airPods = airPods[it]?.count ?: 0,
                )
            )
        }
    }
}
