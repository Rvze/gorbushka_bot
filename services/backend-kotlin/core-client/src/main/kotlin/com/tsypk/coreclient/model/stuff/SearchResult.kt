package com.tsypk.coreclient.model.stuff

import recognitioncommons.models.apple.airpods.AirPods
import recognitioncommons.models.apple.iphone.Iphone
import recognitioncommons.models.apple.macbook.Macbook

data class SearchResult(
    val iphones: MutableList<Iphone> = mutableListOf(),
    val airPods: MutableList<AirPods> = mutableListOf(),
    val macbooks: MutableList<Macbook> = mutableListOf(),
) {
    fun allEmpty(): Boolean {
        return totalContains() == 0
    }

    fun totalContains(): Int {
        return iphones.size + airPods.size + macbooks.size
    }
}
