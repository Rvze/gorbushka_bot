package recognitioncommons.util

import recognitioncommons.models.apple.AppleColor
import recognitioncommons.models.apple.airpods.AirPods
import recognitioncommons.models.apple.airpods.AirPodsFullModel
import recognitioncommons.models.apple.airpods.AirPodsModel
import recognitioncommons.models.apple.airpods.AirPodsSearchModel
import recognitioncommons.models.apple.iphone.Iphone
import recognitioncommons.models.apple.iphone.IphoneFullModel
import recognitioncommons.models.apple.iphone.IphoneMemory
import recognitioncommons.models.apple.iphone.IphoneModel
import recognitioncommons.models.apple.iphone.IphoneSearchModel
import recognitioncommons.models.apple.macbook.Macbook
import recognitioncommons.models.apple.macbook.MacbookMemory
import recognitioncommons.models.apple.macbook.MacbookModel
import recognitioncommons.models.apple.macbook.MacbookSearchModel
import recognitioncommons.models.sony.PlayStationFullModel

/**
 * Iphone
 */
fun Iphone.toSearchModel(): IphoneSearchModel {
    return IphoneSearchModel(
        model = model,
        memory = memory,
        color = color,
    )
}

fun IphoneModel.isValidColor(color: AppleColor?): Boolean {
    if (color == null) {
        return true
    }
    return color in this.possibleColors
}

fun IphoneModel.isValidMemory(memory: IphoneMemory?): Boolean {
    if (memory == null) {
        return true
    }
    return memory in this.possibleMemories
}

fun Iphone.idString(): String {
    return "${model.name}/${memory.name}/${color.name}"
}

fun IphoneFullModel.idString(): String {
    return "${model.name}/${memory.name}/${color.name}"
}

/**
 * MacBook
 */

fun Macbook.idString(): String {
    return "${model.name}/${memory.name}/${ram.name}/${color.name}"
}

fun MacbookModel.isValidColor(color: AppleColor?): Boolean {
    if (color == null) {
        return true
    }
    return color in this.possibleColors
}

fun MacbookModel.isValidMemory(memory: MacbookMemory?): Boolean {
    if (memory == null) {
        return true
    }
    return memory in this.possibleMemories
}

fun Macbook.toSearchModel(): MacbookSearchModel {
    return MacbookSearchModel(
        model = model,
        memory = memory,
        ram = ram,
        color = color,
    )
}

/**
 * AirPods
 */
fun AirPods.toSearchModel(): AirPodsSearchModel {
    return AirPodsSearchModel(
        model = model,
        color = color,
        country = country,
    )
}

fun AirPodsModel.isValidColor(color: AppleColor?): Boolean {
    if (color == null)
        return true
    return color in this.possibleColors
}

fun AirPodsFullModel.idString(): String {
    if (color == AppleColor.DEFAULT) {
        return model.name
    }
    return "${model.name}/${color.name}"
}

fun AirPods.idString(): String {
    if (color == AppleColor.DEFAULT) {
        return model.name
    }
    return "${model.name}/${color.name}"
}

/**
 * Playstation
 */

fun PlayStationFullModel.idString(): String {
    return "${model.name}/${year}/${revision}"
}