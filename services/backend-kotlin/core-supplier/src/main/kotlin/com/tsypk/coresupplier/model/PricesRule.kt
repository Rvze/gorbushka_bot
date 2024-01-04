package com.tsypk.coresupplier.model

data class PricesRule(
    val originalPrice: Int,
    val year1WarrantyPrice: Int,
    val days30WarrantyPrice: Int,
    val dropPrice: Int,
    val wholesalePrice: Int,
)
