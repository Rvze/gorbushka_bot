package com.tsypk.coreclient.model.stuff

import recognitioncommons.models.apple.airpods.AirPodsSearchModel
import recognitioncommons.models.apple.iphone.IphoneSearchModel
import recognitioncommons.models.apple.macbook.MacbookSearchModel

/**
 * Pair of recognized list and error list
 */
data class SearchModelsRecognitionResult(
    val iphones: MutableList<IphoneSearchModel> = mutableListOf(),
    val airPods: MutableList<AirPodsSearchModel> = mutableListOf(),
    val macbooks: MutableList<MacbookSearchModel> = mutableListOf(),
    val errors: MutableList<String> = mutableListOf(),
) {
    fun allEmpty(): Boolean {
        return totalRecognized() == 0
    }

    fun withoutErrors(): Boolean {
        return errors.isEmpty()
    }

    fun totalRecognized(): Int {
        return iphones.size + airPods.size + macbooks.size
    }
}
