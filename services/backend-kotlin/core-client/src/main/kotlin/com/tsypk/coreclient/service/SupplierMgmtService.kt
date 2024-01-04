package com.tsypk.coreclient.service

import com.tsypk.coreclient.exception.SupplierNotFoundException
import com.tsypk.coreclient.model.Supplier
import com.tsypk.coreclient.repository.SupplierRepository
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SupplierMgmtService(
    private val supplierRepository: SupplierRepository,
) {
    var idToSupplierUsernameMap: MutableMap<Long, String> = mutableMapOf()
    var allSuppliersMap: MutableMap<String, Supplier> = mutableMapOf()

    @PostConstruct
    private fun postConstruct() {
        updateAllSuppliersMap()
    }

    @Transactional
    fun create(supplier: Supplier) {
        supplierRepository.createOnConflictDoNothing(supplier)
        updateAllSuppliersMap()
    }

    fun findById(id: Long): Supplier {
        return findByIdOrNull(id)
            ?: throw SupplierNotFoundException(id)
    }

    fun findByIdOrNull(id: Long): Supplier? {
        allSuppliersMap[idToSupplierUsernameMap[id]]?.let {
            return it
        }

        return supplierRepository.findById(id)?.let {
            idToSupplierUsernameMap[id] = it.username
            allSuppliersMap[it.username] = it
            it
        }
    }

    fun findByUsername(username: String): Supplier? {
        allSuppliersMap[username]?.let {
            return it
        }

        return supplierRepository.findByUsername(username)?.let {
            idToSupplierUsernameMap[it.id] = it.username
            allSuppliersMap[it.username] = it
            it
        }
    }

    fun enrichUsernames(ids: Collection<Long>): Map<Long, String> {
        val result = mutableMapOf<Long, String>()
        val notInCache = ids.filter {
            val found = idToSupplierUsernameMap[it]
            if (found == null) {
                true
            } else {
                result[it] = found
                false
            }
        }

        supplierRepository.getAllWhereIdIn(notInCache).forEach {
            idToSupplierUsernameMap[it.id] = it.username
            allSuppliersMap[it.username] = it
            result[it.id] = it.username
        }

        return result
    }

    @Transactional
    fun delete(supplier: Supplier) {
        supplierRepository.delete(supplier)
        updateAllSuppliersMap()
    }

    private fun updateAllSuppliersMap() {
        val suppliers = supplierRepository.getAll()
        idToSupplierUsernameMap = mutableMapOf()

        allSuppliersMap = mutableMapOf(
            *suppliers.map {
                idToSupplierUsernameMap[it.id] to it.username
                it.username to it
            }.toTypedArray()
        )
    }
}
