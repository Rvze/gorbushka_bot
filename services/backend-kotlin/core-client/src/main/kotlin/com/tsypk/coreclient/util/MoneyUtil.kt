package com.tsypk.coreclient.util

fun formatPriceWithDots(price: Int): String {
    val priceStr = price.toString()
    return priceStr.reversed().mapIndexed { ind, c ->
        if (ind % 3 == 2 && ind != (priceStr.length - 1)) {
            ("$c.")
        } else {
            c.toString()
        }
    }.joinToString(separator = "") { it }.reversed()
}
