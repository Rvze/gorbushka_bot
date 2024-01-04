package com.tsypk.coresupplier.util

import com.tsypk.coresupplier.exception.IncorrectPriceException
import com.tsypk.coresupplier.model.PricesRule

fun techStorePricesRule(price: Int): PricesRule {
    if (price <= 0) {
        throw IncorrectPriceException(price = price)
    }
    return when (price) {
        in 1..2999 -> {
            PricesRule(price, price + 500, price + 400, price + 300, price + 250)
        }

        in 3000..9999 -> {
            PricesRule(price, price + 1000, price + 800, price + 700, price + 600)
        }

        in 10000..14999 -> {
            PricesRule(price, price + 1500, price + 1200, price + 1100, price + 900)
        }

        in 15000..19999 -> {
            PricesRule(price, price + 2000, price + 1500, price + 1400, price + 1200)
        }

        in 20000..29999 -> {
            PricesRule(price, price + 3000, price + 2200, price + 2000, price + 1400)
        }

        in 30000..39999 -> {
            PricesRule(price, price + 3300, price + 2700, price + 2100, price + 1500)
        }

        in 40000..49999 -> {
            PricesRule(price, price + 3600, price + 2900, price + 2500, price + 1800)
        }

        in 50000..69999 -> {
            PricesRule(price, price + 4000, price + 3000, price + 2700, price + 2000)
        }

        in 70000..79999 -> {
            PricesRule(price, price + 4500, price + 3400, price + 3000, price + 2200)
        }

        in 80000..99999 -> {
            PricesRule(price, price + 5000, price + 3900, price + 3100, price + 2500)
        }

        in 10000..119999 -> {
            PricesRule(price, price + 6000, price + 4900, price + 3500, price + 3000)
        }

        in 120000..139999 -> {
            PricesRule(price, price + 7000, price + 5900, price + 3900, price + 3500)
        }

        else -> {
            PricesRule(price, price + 8000, price + 6900, price + 4500, price + 4000)
        }
    }
}
