package com.tsypk.coreclient.service.presenting.apple

import recognitioncommons.models.apple.AppleColor
import recognitioncommons.models.apple.macbook.Macbook
import recognitioncommons.models.apple.macbook.MacbookModel
import recognitioncommons.util.Presentation.toHumanReadableString

fun Macbook.toHumanReadableString(): String {
    val builder = StringBuilder("<b>${this.model.shortModelStr()}</b>")
    if (color != AppleColor.DEFAULT) {
        builder.append(" $color")
    }
    builder.append(" ${this.country.toHumanReadableString()} - <b>$price</b>")
    return builder.toString()
}

fun MacbookModel.shortModelStr(): String {
    return when (this) {
        MacbookModel.MACBOOK_PRO_13_M2 -> "Macbook Pro 13 M2"
        MacbookModel.MACBOOK_PRO_14_M1_PRO -> "Macbook Pro 14 M1 Pro"
        MacbookModel.MACBOOK_PRO_16_M2_PRO -> "Macbook Pro 16 M2 Pro"
        MacbookModel.MACBOOK_PRO_13_M1 -> "Macbook Pro 13 M1"
        MacbookModel.MACBOOK_AIR_13_M2 -> "Macbook Air 13 M2"
        MacbookModel.MACBOOK_AIR_13_M1 -> "Macbook Air 13 M1"
        MacbookModel.MACBOOK_PRO_14_M1_MAX -> "Macbook Pro 14 M1 Max"
        MacbookModel.MACBOOK_PRO_16_M1_PRO -> "Macbook Pro 16 M1 Pro"
        MacbookModel.MACBOOK_PRO_16_M1_MAX -> "Macbook Pro 16 M1 Max"
        MacbookModel.MACBOOK_PRO_14_M2_MAX -> "Macbook Pro 14 M2 Max"
        MacbookModel.MACBOOK_PRO_14_M2_PRO -> "Macbook Pro 14 M2 Pro"
        MacbookModel.MACBOOK_PRO_16_M2_MAX -> "MACBOOK_PRO_16_M2_Max"
    }
}