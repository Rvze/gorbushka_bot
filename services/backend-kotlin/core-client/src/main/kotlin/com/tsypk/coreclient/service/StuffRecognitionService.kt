package com.tsypk.coreclient.service

import com.tsypk.coreclient.model.stuff.FullRecognitionResult
import com.tsypk.coreclient.model.stuff.SearchModelsRecognitionResult
import com.tsypk.coreclient.service.stat.StatisticsService
import org.springframework.stereotype.Service
import recognitioncommons.exception.StaffRecognitionException
import recognitioncommons.exception.staffRecognitionExceptionToHumanReadable
import recognitioncommons.service.recognition.apple.airpods.AirPodsRecognitionService
import recognitioncommons.service.recognition.apple.iphone.IphoneRecognitionService
import recognitioncommons.service.recognition.apple.macbook.MacbookRecognitionService
import recognitioncommons.util.normalization.customNormalizeText
import recognitioncommons.util.normalization.normalizeText

@Service
class StuffRecognitionService(
    private val iphoneRecognitionService: IphoneRecognitionService,
    private val airPodsRecognitionService: AirPodsRecognitionService,
    private val statisticsService: StatisticsService,
    private val macbookRecognitionService: MacbookRecognitionService
) {

    private companion object {
        val years = setOf(
            "2019",
            "2020",
            "2021",
            "2022",
            "2023",
        )
    }

    fun recognizeSearchModels(text: String): SearchModelsRecognitionResult {
        val result = SearchModelsRecognitionResult()
        normalizeText(text).forEach {
            try {
                if (airPodsRecognitionService.containsAirPodsInLine(it)) {
                    val recognized = airPodsRecognitionService.recognizeSearchModel(it)
                    result.airPods.addAll(recognized)
                } else if (macbookRecognitionService.containsMacbookInLine(it)) {
                    val recognized = macbookRecognitionService.recognizeSearchModel(it)
                    result.macbooks.addAll(recognized)
                } else {
                    val recognized = iphoneRecognitionService.recognizeSearchModel(it)
                    result.iphones.addAll(recognized)
                }
            } catch (e: StaffRecognitionException) {
                result.errors.add(
                    "$it - ${staffRecognitionExceptionToHumanReadable(e)}"
                )
            }
        }
        statisticsService.newIphoneRecognitionReseller(result.iphones.size, result.errors.size)
        return result.copy(
            iphones = result.iphones.toSet().toMutableList(),
            airPods = result.airPods.toSet().toMutableList(),
            macbooks = result.macbooks.toSet().toMutableList(),
            errors = result.errors.toSet().toMutableList()
        )
    }

    fun recognizeFull(text: String, supplierId: Long? = null): FullRecognitionResult {
        val result = FullRecognitionResult()
        customNormalizeText(text) {
            it.map { line ->
                var changed: String? = null
                years.forEach { year ->
                    val index = line.indexOf(year)
                    if (index >= 0 && (index + year.length + 1) < line.length && !line[index + year.length].isDigit()) {
                        changed = line.replace(year, " ")
                        return@forEach
                    }
                }
                changed ?: line
            }
        }.forEach {
            try {
                if (airPodsRecognitionService.containsAirPodsInLine(it)) {
                    val recognized = airPodsRecognitionService.recognize(it)
                    result.airPods.addAll(recognized)
                } else if (macbookRecognitionService.containsMacbookInLine(it)) {
                    val recognized = macbookRecognitionService.recognize(it)
                    result.macbooks.addAll(recognized)
                } else {
                    val recognized = iphoneRecognitionService.recognize(it)
                    result.iphones.addAll(recognized)
                }
            } catch (e: StaffRecognitionException) {
                result.errors.add(
                    "$it - ${staffRecognitionExceptionToHumanReadable(e)}"
                )
            }
        }
        supplierId?.let { statisticsService.newIphoneRecognitionSupplier(it, result.iphones.size, result.errors.size) }
        return result.copy(
            iphones = result.iphones.toSet().toMutableList(),
            airPods = result.airPods.toSet().toMutableList(),
            macbooks = result.macbooks.toSet().toMutableList(),
        )
    }
}
