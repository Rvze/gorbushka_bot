package recognitioncommons.exception

import recognitioncommons.models.apple.AppleColor
import recognitioncommons.models.apple.airpods.AirPodsModel
import recognitioncommons.models.apple.iphone.IphoneMemory
import recognitioncommons.models.apple.iphone.IphoneModel
import recognitioncommons.models.apple.macbook.MacbookMemory
import recognitioncommons.models.apple.macbook.MacbookModel

/**
 * Common
 */
class SearchException(toSearch: String, input: String) :
    RuntimeException("Не получилось найти $toSearch в $input")

open class StaffRecognitionException(input: String) :
    RuntimeException(input)

open class NotIphoneException(input: String) :
    StaffRecognitionException(input)

open class NotMacbookException(input: String) :
    StaffRecognitionException(input)

open class NotAirPodsException(input: String) :
    StaffRecognitionException(input)

open class NotPlayStationException(input: String) :
    StaffRecognitionException(input)

/**
 * Iphone
 */
class IphonePriceIsAbsentException(input: String) :
    NotIphoneException("There is no iPhone price in input=$input")

class IphoneMemoryIsAbsentException(input: String) :
    NotIphoneException("There is no memory in input=$input")

class IphoneMemoryIsIncorrectException(val model: IphoneModel, val memory: IphoneMemory?) :
    NotIphoneException("Memory $memory is not compatible with model $model")

class IphoneColorIsAbsentException(input: String) :
    NotIphoneException("There is no color in input=$input")

class IphoneColorIsIncorrectException(val model: IphoneModel, val color: AppleColor) :
    NotIphoneException("Color $color is not compatible with model $model")

class IphoneModelIsAbsentException(input: String) :
    NotIphoneException("There is no iphone model in input=$input")

class InvalidIphoneIdException(id: String) :
    NotIphoneException("Invalid iphone identifier $id")

/**
 * Airpods
 */
class InvalidAirPodsIdException(id: String) :
    NotAirPodsException("Invalid airpods id : $id")

class AirPodsColorIsIncorrectException(val model: AirPodsModel, val color: AppleColor) :
    NotAirPodsException("Color $color is not compatible with model $model")

class AirPodsColorIsAbsentException(input: String) :
    NotAirPodsException("There is no color in input = $input")

class AirPodsModelIsAbsentException(input: String) :
    NotAirPodsException("There is no Airpods model in input = $input")

class AirPodsPriceIsAbsentException(input: String) :
    NotAirPodsException("There is no airpods price in input = $input")

/**
 * Macbook
 */
class MacbookPriceIsAbsentException(input: String) :
    NotMacbookException("There is no macbook price in input=$input")

class MacbookMemoryIsAbsentException(input: String) :
    NotMacbookException("There is no memory in input=$input")

class MacbookMemoryIsIncorrectException(val model: MacbookModel, val memory: MacbookMemory?) :
    NotMacbookException("Memory $memory is not compatible with model $model")

class MacbookRamIsAbsentException(input: String) :
    NotMacbookException("There is no MacBook RAM in input=$input")

class MacbookColorIsAbsentException(input: String) :
    NotMacbookException("There is no color in input=$input")

class MacbookColorIsIncorrectException(val model: MacbookModel, val color: AppleColor) :
    NotMacbookException("Color $color is not compatible with model $model")

class MacbookModelIsAbsentException(input: String) :
    NotMacbookException("There is no Macbook model in input=$input")

class MacbookSerialNumberNotFoundOrIncorrect(input: String) :
    NotMacbookException("There is no Macbook serial number in input=$input")

class InvalidMacbookIdException(id: String) :
    NotMacbookException("Invalid macbook id: $id")

/**
 * Playstation
 */
class PlaystationInvalidIdException(input: String) :
    NotPlayStationException("There is no Playstation model in input=$input")