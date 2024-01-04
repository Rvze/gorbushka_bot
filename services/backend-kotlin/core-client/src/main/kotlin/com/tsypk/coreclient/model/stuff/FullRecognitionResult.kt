package com.tsypk.coreclient.model.stuff

import recognitioncommons.models.apple.airpods.AirPods
import recognitioncommons.models.apple.iphone.Iphone
import recognitioncommons.models.apple.macbook.Macbook

data class FullRecognitionResult(
    val iphones: MutableList<Iphone> = mutableListOf(),
    val airPods: MutableList<AirPods> = mutableListOf(),
    val macbooks: MutableList<Macbook> = mutableListOf(),
    val errors: MutableList<String> = mutableListOf(),
) {
    fun allEmpty(): Boolean {
        return totalContains() == 0
    }

    fun totalContains(): Int {
        return iphones.size + airPods.size + macbooks.size
    }

    fun add(other: FullRecognitionResult): FullRecognitionResult {
        return FullRecognitionResult(
            iphones = (iphones + other.iphones).toSet().toMutableList(),
            airPods = (airPods + other.airPods).toSet().toMutableList(),
            macbooks = (macbooks + other.macbooks).toSet().toMutableList(),
        )
    }
}