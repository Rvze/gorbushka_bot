package recognitioncommons.util.normalization

import org.junit.jupiter.api.Test

class TextNormalizationKtTest {
    @Test
    fun `test custom normalization`() {
        val years = setOf(
            "2019",
            "2020",
            "2021",
            "2022",
            "2023",
        )

        val result = customNormalizeText(
            """
            🎧AirPods pro 1 (2021) MagSafe-15.500🇪🇺
            🎧AirPods pro 2 (2022) MagSafe-19.000🇪🇺
            🎧AirPods pro 1 MagSafe-20220🇪🇺
            🎧AirPods pro 2 MagSafe-20210🇪🇺

            🎧AirPods Max Green-51.000🇺🇸
        """.trimIndent()
        ) {
            it.map { line ->
                var changed: String? = null
                years.forEach { year ->
                    val index = line.indexOf(year)
                    if (index >= 0 && (index + year.length + 1) < line.length && !line[index + year.length].isDigit()) {
                        changed = line.replace(year, " ")
                        return@forEach
                    }
                }
                changed ?: line
            }
        }

        result.forEach {
            println(it)
        }
    }
}