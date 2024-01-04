package com.tsypk.coreclient.service

import com.tsypk.coreclient.model.stuff.SearchModelsRecognitionResult
import com.tsypk.coreclient.model.stuff.SearchResult
import com.tsypk.coreclient.service.client.SupplierAirPodsClientService
import com.tsypk.coreclient.service.client.SupplierIphonesClientService
import com.tsypk.coreclient.service.client.SupplierMacbooksClientService
import org.springframework.stereotype.Service
import recognitioncommons.models.apple.airpods.AirPods
import recognitioncommons.models.apple.airpods.AirPodsSearchModel
import recognitioncommons.models.apple.iphone.Iphone
import recognitioncommons.models.apple.iphone.IphoneSearchModel
import recognitioncommons.models.apple.macbook.Macbook
import recognitioncommons.models.apple.macbook.MacbookSearchModel

@Service
class RecognitionResultEnrichmentService(
    private val iphonesClientService: SupplierIphonesClientService,
    private val airPodsClientService: SupplierAirPodsClientService,
    private val macbooksClientService: SupplierMacbooksClientService

) {
    suspend fun enrichForRecognitionResult(recognitionResult: SearchModelsRecognitionResult): SearchResult {
        val searchResult = SearchResult()
        recognitionResult.iphones.forEach {
            searchResult.iphones.addAll(iphones(it))
        }
        recognitionResult.airPods.forEach {
            searchResult.airPods.addAll(airPods(it))
        }
        recognitionResult.macbooks.forEach {
            searchResult.macbooks.addAll(macbooks(it))
        }
        return searchResult.copy(
            iphones = searchResult.iphones.toSet().toMutableList(),
            airPods = searchResult.airPods.toSet().toMutableList(),
            macbooks = searchResult.macbooks.toSet().toMutableList(),
        )
    }

    private suspend fun iphones(iphone: IphoneSearchModel): List<Iphone> {
        return iphonesClientService.iphonesFindBest(
            model = iphone.model,
            memory = iphone.memory,
            color = iphone.color,
            country = iphone.country,
        )
    }

    private suspend fun airPods(airpods: AirPodsSearchModel): List<AirPods> {
        return airPodsClientService.airPodsFindBest(
            model = airpods.model,
            color = airpods.color,
            country = airpods.country,
        )
    }

    private suspend fun macbooks(macbooks: MacbookSearchModel): List<Macbook> {
        return macbooksClientService.getBestMacbooks(
            model = macbooks.model,
            color = macbooks.color,
            country = macbooks.country,
            memory = macbooks.memory,
            ram = macbooks.ram
        )
    }
}
