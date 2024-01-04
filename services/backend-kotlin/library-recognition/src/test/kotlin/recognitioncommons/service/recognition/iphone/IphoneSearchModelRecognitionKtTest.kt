package recognitioncommons.service.recognition.iphone

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import recognitioncommons.models.apple.AppleColor
import recognitioncommons.models.apple.iphone.IphoneMemory
import recognitioncommons.models.apple.iphone.IphoneModel
import recognitioncommons.models.apple.iphone.IphoneSearchModel
import recognitioncommons.service.recognition.apple.iphone.recognizeIphoneSearchModelsWithErrors

internal class IphoneSearchModelRecognitionKtTest {
    @Test
    internal fun searchModelRecognition() {
        val toRecognize = """
            11 128
            14 Pro Max 1 ТБ
            14 512 Black
        """.trimIndent()
        val ans = recognizeIphoneSearchModelsWithErrors(toRecognize)
        val iphones = ans.first
        assertThat(iphones.size).isEqualTo(3)
        assertThat(iphones.first()).isEqualTo(
            IphoneSearchModel(IphoneModel.IPHONE_11, IphoneMemory.GB_128, null)
        )
        assertThat(iphones[1]).isEqualTo(
            IphoneSearchModel(IphoneModel.IPHONE_14_PRO_MAX, IphoneMemory.TB_1, null)
        )
        assertThat(iphones[2]).isEqualTo(
            IphoneSearchModel(IphoneModel.IPHONE_14, IphoneMemory.GB_512, AppleColor.MIDNIGHT)
        )
    }
}
