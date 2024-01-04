package com.tsypk.coreclient.util

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ExtensionFunctionsKtTest {

    @Test
    fun `decompose test 1`() {
        assertThat(
            listOf(
                "1", "2", "3", "4"
            ).decompose(2)
        ).isEqualTo(
            listOf(
                listOf(
                    "1", "2"
                ),
                listOf(
                    "3", "4"
                )
            )
        )
    }

    @Test
    fun `decompose test 2`() {
        assertThat(
            listOf(
                "1", "2", "3", "4", "5"
            ).decompose(2)
        ).isEqualTo(
            listOf(
                listOf(
                    "1", "2"
                ),
                listOf(
                    "3", "4"
                ),
                listOf(
                    "5"
                )
            )
        )
    }

    @Test
    fun `decompose test 3`() {
        assertThat(
            listOf(
                "1", "2", "3", "4", "5", "6", "7", "8"
            ).decompose(3)
        ).isEqualTo(
            listOf(
                listOf(
                    "1", "2", "3"
                ),
                listOf(
                    "4", "5", "6"
                ),
                listOf(
                    "7", "8"
                )
            )
        )
    }
}