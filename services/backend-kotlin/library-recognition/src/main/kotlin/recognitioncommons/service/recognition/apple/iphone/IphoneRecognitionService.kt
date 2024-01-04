package recognitioncommons.service.recognition.apple.iphone

import org.springframework.stereotype.Service
import recognitioncommons.models.apple.iphone.Iphone
import recognitioncommons.models.apple.iphone.IphoneSearchModel

@Service
class IphoneRecognitionService {
    fun recognize(line: String): List<Iphone> {
        return recognizeIphones(line)
    }

    fun recognizeSearchModel(line: String): List<IphoneSearchModel> {
        return recognizeIphoneSearchModels(line)
    }
}
