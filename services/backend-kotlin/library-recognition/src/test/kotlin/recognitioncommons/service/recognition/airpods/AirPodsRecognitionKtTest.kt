package recognitioncommons.service.recognition.airpods

import org.junit.jupiter.api.Test
import recognitioncommons.service.recognition.apple.airpods.recognizeAirPodsWithErrors

class AirPodsRecognitionKtTest {
    @Test
    fun `full recognition test`() {
        val text = """
            AirPods 3 - 12.700🇭🇰
            AirPods Pro MagSafe - 14.300 🇪🇺 
            AirPods Pro 2 MagSafe - 16.200 🇭🇰
            
            AirPods 3 MagSafe - 13.900 🇭🇰
            AirPods 3 - 13.400 🇭🇰
            AirPods Pro MagSafe - 14.300 🇪🇺
            AirPods Pro 2 MagSafe - 16.200 🇭🇰
            
            AirPods2 8.400🇪🇺
            AirPods 3 14300
            AirPods Max Gray 49.000
            
            AirPods 2 8000
            AirPods 3 (MagSafe) - 13.900🇭🇰
            AirPods 3 (Lightning) - 12.900🇭🇰
            AirPods Pro -13.900🇪🇺
            AirPods Pro 2 -16.000🇪🇺
            AirPods Max Gray 4800🇺🇸
            Airpods Max Rose- 48 000 
            AirPods Max Blue 48000🇺🇸
            
            🎧AirPods 2 -8.400🇪🇺
            🎧 AirPods (3rd Gen) with MagSafe Charging Case-14.200🇭🇰
            🎧 AirPods (3rd Gen) with Lightning Charging Case-13.300🇭🇰
            🎧AirPods pro 1 (2021) MagSafe-14.300🇪🇺
            🎧AirPods pro 2 (2022) MagSafe-16.500 🇪🇺
            🎧AirPods Max Black -48.800🇺🇸
            🎧AirPods Max Blue -48.500🇺🇸
            
            AirPods Pro 13.600🇪🇺
            AirPods Pro2 16400
            
            AirPods Pro 14000
            AirPods Pro 2 16000
        """.trimIndent()

        val result = recognizeAirPodsWithErrors(text)
        result.first.forEach {
            println(it)
            // assertThat(it.price).isBetween(8199, 50501)
        }

        result.second.forEach {
            println(it)
        }
        // assertThat(result.second).isEmpty()
    }
}
