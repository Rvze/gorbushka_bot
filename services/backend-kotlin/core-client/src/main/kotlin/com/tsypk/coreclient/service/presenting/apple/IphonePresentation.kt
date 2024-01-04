package com.tsypk.coreclient.service.presenting.apple

import recognitioncommons.models.apple.iphone.Iphone
import recognitioncommons.models.apple.iphone.IphoneModel
import recognitioncommons.util.Presentation.toHumanReadableString

fun Iphone.toHumanReadableString(): String {
    return "<b>${this.model.shortModelStr()}</b> ${this.memory.gbValue} ${this.color} ${this.country.toHumanReadableString()} - <b>$price</b>"
}

fun IphoneModel.shortModelStr(): String {
    return when (this) {
        IphoneModel.IPHONE_14_PRO_MAX -> "14 Pro Max"
        IphoneModel.IPHONE_14_PRO -> "14 Pro"
        IphoneModel.IPHONE_14_PLUS -> "14 Plus"
        IphoneModel.IPHONE_14 -> "14"
        IphoneModel.IPHONE_SE_3 -> "SE 3"
        IphoneModel.IPHONE_13_PRO_MAX -> "13 Pro Max"
        IphoneModel.IPHONE_13_PRO -> "13 Pro"
        IphoneModel.IPHONE_13 -> "13"
        IphoneModel.IPHONE_13_MINI -> "13 Mini"
        IphoneModel.IPHONE_12_PRO_MAX -> "12 Pro Max"
        IphoneModel.IPHONE_12_PRO -> "12 Pro"
        IphoneModel.IPHONE_12 -> "12"
        IphoneModel.IPHONE_12_MINI -> "12 Mini"
        IphoneModel.IPHONE_SE_2 -> "SE 2"
        IphoneModel.IPHONE_11_PRO_MAX -> "11 Pro Max"
        IphoneModel.IPHONE_11_PRO -> "11 Pro"
        IphoneModel.IPHONE_11 -> "11"
    }
}
