package recognitioncommons.exception


object RecognitionExceptionMessages {
    /**
     * Common
     */
    const val onRecognitionError = "Не удалось распознать товар"

    /**
     * Iphone
     */
    const val onIphonePriceIsAbsentException = "В запросе отсутствует цена айфона, или указана неверно"
    const val onIphoneMemoryIsAbsentException = "В запросе память айфона отсутствует, или указана неверно"
    const val onIphoneColorIsAbsentException = "В запросе цвет айфона отсутствует, или указана неверно"
    const val onIphoneModelIsAbsentException = "В запросе модель айфона отсутствует, или указана неверно"
    fun onIphoneMemoryIsIncorrectException(e: IphoneMemoryIsIncorrectException) =
        "Модели айфона ${e.model} с памятью ${e.memory} не существует"

    fun onIphoneColorIsIncorrectException(e: IphoneColorIsIncorrectException) =
        "Модели айфона ${e.model} с цветом ${e.color.name} не существует"

    /**
     * AirPods
     */
    const val onAirPodsPriceIsAbsentException = "В запросе отсутствует цена эирподсов, или указана неверно"
    const val onAirPodsColorIsAbsentException = "В запросе цвет эирподсов отсутствует, или указана неверно"
    const val onAirPodsModelIsAbsentException = "В запросе модель эирподсов отсутствует, или указана неверно"

    fun onAirPodsColorIsIncorrectException(e: AirPodsColorIsIncorrectException) =
        "Модели эирподсов ${e.model} с цветом ${e.color.name} не существует"

    /**
     * MacBook
     */
    const val onMacbookPriceIsAbsentException = "В запросе отсутствует цена макбука, или указана неверно"
    const val onMacbookMemoryIsAbsentException = "В запросе память макбука отсутствует, или указана неверно"
    const val onMacbookRamIsAbsentException = "В запросе оперативная память (RAM) отсутствует, или указана неверно"
    const val onMacbookColorIsAbsentException = "В запросе цвет макбука отсутствует, или указана неверно"
    const val onMacbookModelIsAbsentException = "В запросе модель макбука отсутствует, или указана неверно"

    fun onMacbookMemoryIsIncorrectException(e: MacbookMemoryIsIncorrectException) =
        "Модели макбука ${e.model} с памятью ${e.memory} не существует"

    fun onMacbookColorIsIncorrectException(e: MacbookColorIsIncorrectException) =
        "Модели макбука ${e.model} с цветом ${e.color.name} не существует"
}

fun staffRecognitionExceptionToHumanReadable(e: StaffRecognitionException): String {
    return when (e) {
        is IphonePriceIsAbsentException -> RecognitionExceptionMessages.onIphonePriceIsAbsentException
        is IphoneMemoryIsAbsentException -> RecognitionExceptionMessages.onIphoneMemoryIsAbsentException
        is IphoneMemoryIsIncorrectException -> RecognitionExceptionMessages.onIphoneMemoryIsIncorrectException(e)
        is IphoneColorIsAbsentException -> RecognitionExceptionMessages.onIphoneColorIsAbsentException
        is IphoneColorIsIncorrectException -> RecognitionExceptionMessages.onIphoneColorIsIncorrectException(e)
        is IphoneModelIsAbsentException -> RecognitionExceptionMessages.onIphoneModelIsAbsentException

        is AirPodsPriceIsAbsentException -> RecognitionExceptionMessages.onAirPodsPriceIsAbsentException
        is AirPodsColorIsAbsentException -> RecognitionExceptionMessages.onAirPodsColorIsAbsentException
        is AirPodsModelIsAbsentException -> RecognitionExceptionMessages.onAirPodsModelIsAbsentException
        is AirPodsColorIsIncorrectException -> RecognitionExceptionMessages.onAirPodsColorIsIncorrectException(e)

        is MacbookPriceIsAbsentException -> RecognitionExceptionMessages.onMacbookPriceIsAbsentException
        is MacbookMemoryIsAbsentException -> RecognitionExceptionMessages.onMacbookMemoryIsAbsentException
        is MacbookRamIsAbsentException -> RecognitionExceptionMessages.onMacbookRamIsAbsentException
        is MacbookMemoryIsIncorrectException -> RecognitionExceptionMessages.onMacbookMemoryIsIncorrectException(e)
        is MacbookColorIsAbsentException -> RecognitionExceptionMessages.onMacbookColorIsAbsentException
        is MacbookColorIsIncorrectException -> RecognitionExceptionMessages.onMacbookColorIsIncorrectException(e)
        is MacbookModelIsAbsentException -> RecognitionExceptionMessages.onMacbookModelIsAbsentException

        else -> e.message ?: RecognitionExceptionMessages.onRecognitionError
    }
}
