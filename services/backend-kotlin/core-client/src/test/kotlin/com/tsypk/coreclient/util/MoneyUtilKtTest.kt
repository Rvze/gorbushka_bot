package com.tsypk.coreclient.util

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class MoneyUtilKtTest {
    @ParameterizedTest
    @CsvSource(
        "200, 200",
        "1200, 1.200",
        "8400, 8.400",
        "12325, 12.325",
        "83222, 83.222",
        "103500, 103.500",
        "103500900, 103.500.900"
    )
    fun formatPriceTest(input: Int, expected: String) {
        assertThat(formatPriceWithDots(input)).isEqualTo(expected)
    }
}
