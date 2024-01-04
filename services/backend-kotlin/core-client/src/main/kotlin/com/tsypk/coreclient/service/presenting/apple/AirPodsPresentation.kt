package com.tsypk.coreclient.service.presenting.apple

import recognitioncommons.models.apple.AppleColor
import recognitioncommons.models.apple.airpods.AirPods
import recognitioncommons.models.apple.airpods.AirPodsModel
import recognitioncommons.util.Presentation.toHumanReadableString

fun AirPods.toHumanReadableString(): String {
    val builder = StringBuilder("<b>${this.model.shortModelStr()}</b>")
    if (color != AppleColor.DEFAULT) {
        builder.append(" $color")
    }
    builder.append(" ${this.country.toHumanReadableString()} - <b>$price</b>")
    return builder.toString()
}

fun AirPodsModel.shortModelStr(): String {
    return when (this) {
        AirPodsModel.AIRPODS_2 -> "AirPods 2"
        AirPodsModel.AIRPODS_3_MAGSAFE -> "AirPods 3 Magsafe"
        AirPodsModel.AIRPODS_3_LIGHTNING -> "AirPods 3"
        AirPodsModel.AIRPODS_PRO_1 -> "AirPods Pro"
        AirPodsModel.AIRPODS_PRO_2 -> "AirPods Pro 2"
        AirPodsModel.AIRPODS_MAX -> "AirPods Max"
    }
}
