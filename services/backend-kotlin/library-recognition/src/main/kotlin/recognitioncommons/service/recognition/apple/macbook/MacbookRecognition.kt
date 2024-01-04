package recognitioncommons.service.recognition.apple.macbook

import recognitioncommons.exception.MacbookSerialNumberNotFoundOrIncorrect
import recognitioncommons.exception.NotMacbookException
import recognitioncommons.exception.staffRecognitionExceptionToHumanReadable
import recognitioncommons.models.apple.AppleColor
import recognitioncommons.models.apple.macbook.*
import recognitioncommons.models.country.Country
import recognitioncommons.service.recognition.common.recognizeCountries
import recognitioncommons.service.recognition.common.recognizeMacbookPrice
import recognitioncommons.util.normalization.filterInput
import recognitioncommons.util.normalization.normalizeText

fun recognizeMacbooksWithErrors(text: String): Pair<List<Macbook>, List<String>> {
    val macbooks = mutableListOf<Macbook>()
    val errors = mutableListOf<String>()
    normalizeText(text)
        .forEach { line ->
            try {
                val recognized = recognizeMacbooks(line)
                macbooks.addAll(recognized)
            } catch (e: NotMacbookException) {
                val msg = "${staffRecognitionExceptionToHumanReadable(e)} - $line"
                errors.add(msg)
            }
        }

    return macbooks to errors
}

fun recognizeMacbooks(line: String): List<Macbook> {
    var inputTemp = line

    val countryResult = recognizeCountries(inputTemp)
    val countries = countryResult.first.let { it.ifEmpty { listOf(Country.USA) } }
    inputTemp = filterInput(countryResult.second)

    val priceResult = recognizeMacbookPrice(inputTemp)
    val price = priceResult.first
    inputTemp = filterInput(priceResult.second)

    val modelBySerialResult = recognizeSerialNumber(inputTemp)
    if (modelBySerialResult != null) {
        return countries.map {
            Macbook(
                model = modelBySerialResult.macbookModel,
                ram = modelBySerialResult.macbookRam,
                memory = modelBySerialResult.macbookMemory,
                color = mapColor(modelBySerialResult.macbookModel, modelBySerialResult.color),
                price = price,
                country = it,
            )
        }
    }

    val colorResult = recognizeColor(inputTemp)
    val color = colorResult.first
    inputTemp = filterInput(colorResult.second)

    val ramResult = recognizeRam(inputTemp)
    val ram = ramResult.first
    inputTemp = filterInput(ramResult.second)

    val memoryResult = recognizeMemory(inputTemp)
    val memory = memoryResult.first
    inputTemp = filterInput(memoryResult.second)

    val modelResult = recognizeModel(input = inputTemp)
    val model = modelResult.first
    assertMacbookMemory(model = model, memory = memory)

    return countries.map {
        Macbook(
            model = model,
            ram = ram,
            memory = memory,
            color = mapColor(model, color),
            price = price,
            country = it,
        )
    }
}

fun recognizeSerialNumber(text: String): MacbookSerialNumber? {
    val arrayOfMacAttr = text.split(" ")
    arrayOfMacAttr.forEach {
        if (it in serlialNumbers.keys) {
            return serlialNumbers[it]
        }
    }
    return null
}

internal fun recognizeMacbookSearchModel(text: String): List<MacbookSearchModel> {
    var inputTemp = text

    val countryResult = recognizeCountries(inputTemp)
    val countries = countryResult.first
    inputTemp = filterInput(countryResult.second)

    val color = try {
        val colorResult = recognizeColor(inputTemp, true)
        inputTemp = filterInput(colorResult.second)
        colorResult.first
    } catch (e: NotMacbookException) {
        null
    }

    val memory = try {
        val memoryResult = recognizeMemory(inputTemp)
        inputTemp = filterInput(memoryResult.second)
        memoryResult.first
    } catch (e: NotMacbookException) {
        null
    }

    val modelResult = recognizeModel(inputTemp, true)
    val model = modelResult.first
    memory?.let { assertMacbookMemory(memory = it, model = model) }
    inputTemp = filterInput(modelResult.second)

    val ram = try {
        val ramResult = recognizeRam(inputTemp)
        ramResult.first
    } catch (e: NotMacbookException) {
        null
    }

    if (countries.isEmpty()) {
        return listOf(
            MacbookSearchModel(
                model = model,
                memory = memory,
                color = color?.let { mapColor(model, it) },
                country = null,
                ram = ram
            )
        )
    }
    return countries.map {
        MacbookSearchModel(
            model = model,
            memory = memory,
            color = color?.let { clr -> mapColor(model, clr) },
            country = it,
            ram = ram
        )
    }
}

/**
 * <SerialNumber, listOf(MacbookModel, MacbookRam, MacbookMemory, AppleColor)>
 */
val serlialNumbers = mapOf(
    //Macbook Air 13 M1 2020
    Pair(
        "MGN63",
        MacbookSerialNumber(
            MacbookModel.MACBOOK_AIR_13_M1,
            MacbookRam.GB_8,
            MacbookMemory.GB_256,
            AppleColor.SPACE_GRAY
        )
    ),
    Pair(
        "MGN93",
        MacbookSerialNumber(MacbookModel.MACBOOK_AIR_13_M1, MacbookRam.GB_8, MacbookMemory.GB_256, AppleColor.SILVER)
    ),
    Pair(
        "MGND3",
        MacbookSerialNumber(MacbookModel.MACBOOK_AIR_13_M1, MacbookRam.GB_8, MacbookMemory.GB_256, AppleColor.GOLD)
    ),
    Pair(
        "MGN73",
        MacbookSerialNumber(
            MacbookModel.MACBOOK_AIR_13_M1,
            MacbookRam.GB_8,
            MacbookMemory.GB_512,
            AppleColor.SPACE_GRAY
        )
    ),
    Pair(
        "MGNA3",
        MacbookSerialNumber(MacbookModel.MACBOOK_AIR_13_M1, MacbookRam.GB_8, MacbookMemory.GB_512, AppleColor.SILVER)
    ),
    Pair(
        "MGNE3",
        MacbookSerialNumber(MacbookModel.MACBOOK_AIR_13_M1, MacbookRam.GB_8, MacbookMemory.GB_512, AppleColor.GOLD)
    ),

    //Macbook Air 13 M2 2022
    Pair(
        "MLXW3",
        MacbookSerialNumber(
            MacbookModel.MACBOOK_AIR_13_M2,
            MacbookRam.GB_8,
            MacbookMemory.GB_256,
            AppleColor.SPACE_GRAY
        )
    ),
    Pair(
        "MLXX3",
        MacbookSerialNumber(
            MacbookModel.MACBOOK_AIR_13_M2,
            MacbookRam.GB_8,
            MacbookMemory.GB_512,
            AppleColor.SPACE_GRAY
        )
    ),
    Pair(
        "MLXY3",
        MacbookSerialNumber(MacbookModel.MACBOOK_AIR_13_M2, MacbookRam.GB_8, MacbookMemory.GB_256, AppleColor.SILVER)
    ),
    Pair(
        "MLY03",
        MacbookSerialNumber(MacbookModel.MACBOOK_AIR_13_M2, MacbookRam.GB_8, MacbookMemory.GB_512, AppleColor.SILVER)
    ),
    Pair(
        "MLY13",
        MacbookSerialNumber(MacbookModel.MACBOOK_AIR_13_M2, MacbookRam.GB_8, MacbookMemory.GB_256, AppleColor.STARLIGHT)
    ),
    Pair(
        "MLY23",
        MacbookSerialNumber(MacbookModel.MACBOOK_AIR_13_M2, MacbookRam.GB_8, MacbookMemory.GB_512, AppleColor.STARLIGHT)
    ),
    Pair(
        "MLY33",
        MacbookSerialNumber(MacbookModel.MACBOOK_AIR_13_M2, MacbookRam.GB_8, MacbookMemory.GB_256, AppleColor.MIDNIGHT)
    ),
    Pair(
        "MLY43",
        MacbookSerialNumber(MacbookModel.MACBOOK_AIR_13_M2, MacbookRam.GB_8, MacbookMemory.GB_512, AppleColor.MIDNIGHT)
    ),

    //Macbook Pro 13 M1 2020
    Pair(
        "MYD83",
        MacbookSerialNumber(
            MacbookModel.MACBOOK_PRO_13_M1,
            MacbookRam.GB_8,
            MacbookMemory.GB_256,
            AppleColor.SPACE_GRAY
        )
    ),
    Pair(
        "MYD92",
        MacbookSerialNumber(
            MacbookModel.MACBOOK_PRO_13_M1,
            MacbookRam.GB_8,
            MacbookMemory.GB_512,
            AppleColor.SPACE_GRAY
        )
    ),
    Pair(
        "MYDA2",
        MacbookSerialNumber(MacbookModel.MACBOOK_PRO_13_M1, MacbookRam.GB_8, MacbookMemory.GB_256, AppleColor.SILVER)
    ),
    Pair(
        "MYDC2",
        MacbookSerialNumber(MacbookModel.MACBOOK_PRO_13_M1, MacbookRam.GB_8, MacbookMemory.GB_512, AppleColor.SILVER)
    ),

    //Macbook Pro 16 2021
    Pair(
        "MK183",
        MacbookSerialNumber(
            MacbookModel.MACBOOK_PRO_16_M1_PRO,
            MacbookRam.GB_16,
            MacbookMemory.GB_512,
            AppleColor.SPACE_GRAY
        )
    ),
    Pair(
        "MK193",
        MacbookSerialNumber(
            MacbookModel.MACBOOK_PRO_16_M1_PRO,
            MacbookRam.GB_16,
            MacbookMemory.TB_1,
            AppleColor.SPACE_GRAY
        )
    ),
    Pair(
        "MK233",
        MacbookSerialNumber(
            MacbookModel.MACBOOK_PRO_16_M1_MAX,
            MacbookRam.GB_64,
            MacbookMemory.TB_4,
            AppleColor.SPACE_GRAY
        )
    ),
    Pair(
        "MK1A3",
        MacbookSerialNumber(
            MacbookModel.MACBOOK_PRO_16_M1_MAX,
            MacbookRam.GB_32,
            MacbookMemory.TB_1,
            AppleColor.SPACE_GRAY
        )
    ),
    Pair(
        "MK1E3",
        MacbookSerialNumber(
            MacbookModel.MACBOOK_PRO_16_M1_PRO,
            MacbookRam.GB_16,
            MacbookMemory.GB_512,
            AppleColor.SILVER
        )
    ),
    Pair(
        "MK1F3",
        MacbookSerialNumber(MacbookModel.MACBOOK_PRO_16_M1_PRO, MacbookRam.GB_16, MacbookMemory.TB_1, AppleColor.SILVER)
    ),
    Pair(
        "MK1H3",
        MacbookSerialNumber(MacbookModel.MACBOOK_PRO_16_M1_MAX, MacbookRam.GB_32, MacbookMemory.TB_1, AppleColor.SILVER)
    ),

    //Macbook Pro 14 2021
    Pair(
        "MKGP3",
        MacbookSerialNumber(
            MacbookModel.MACBOOK_PRO_14_M1_PRO,
            MacbookRam.GB_16,
            MacbookMemory.GB_512,
            AppleColor.SPACE_GRAY
        )
    ),
    Pair(
        "MKGQ3",
        MacbookSerialNumber(
            MacbookModel.MACBOOK_PRO_14_M1_PRO,
            MacbookRam.GB_16,
            MacbookMemory.TB_1,
            AppleColor.SPACE_GRAY
        )
    ),
    Pair(
        "MKGR3",
        MacbookSerialNumber(
            MacbookModel.MACBOOK_PRO_14_M1_PRO,
            MacbookRam.GB_16,
            MacbookMemory.GB_512,
            AppleColor.SILVER
        )
    ),
    Pair(
        "MKGT3",
        MacbookSerialNumber(MacbookModel.MACBOOK_PRO_14_M1_PRO, MacbookRam.GB_16, MacbookMemory.TB_1, AppleColor.SILVER)
    ),
    Pair(
        "MKH53",
        MacbookSerialNumber(
            MacbookModel.MACBOOK_PRO_14_M1_MAX,
            MacbookRam.GB_64,
            MacbookMemory.TB_2,
            AppleColor.SPACE_GRAY
        )
    ),
    Pair(
        "MK1E3",
        MacbookSerialNumber(
            MacbookModel.MACBOOK_PRO_14_M1_PRO,
            MacbookRam.GB_16,
            MacbookMemory.GB_512,
            AppleColor.SILVER
        )
    ),
    Pair(
        "MK1F3",
        MacbookSerialNumber(MacbookModel.MACBOOK_PRO_14_M1_PRO, MacbookRam.GB_16, MacbookMemory.TB_1, AppleColor.SILVER)
    ),
    Pair(
        "MK1H3",
        MacbookSerialNumber(MacbookModel.MACBOOK_PRO_14_M1_MAX, MacbookRam.GB_32, MacbookMemory.TB_1, AppleColor.SILVER)
    ),

    //Macbook Pro 13 M2 2022
    Pair(
        "MNEH3",
        MacbookSerialNumber(
            MacbookModel.MACBOOK_PRO_13_M2,
            MacbookRam.GB_8,
            MacbookMemory.GB_256,
            AppleColor.SPACE_GRAY
        )
    ),
    Pair(
        "MNEJ3",
        MacbookSerialNumber(
            MacbookModel.MACBOOK_PRO_13_M2,
            MacbookRam.GB_8,
            MacbookMemory.GB_512,
            AppleColor.SPACE_GRAY
        )
    ),
    Pair(
        "MNEP3",
        MacbookSerialNumber(MacbookModel.MACBOOK_PRO_13_M2, MacbookRam.GB_8, MacbookMemory.GB_256, AppleColor.SILVER)
    ),
    Pair(
        "MNEQ3",
        MacbookSerialNumber(MacbookModel.MACBOOK_PRO_13_M2, MacbookRam.GB_8, MacbookMemory.GB_512, AppleColor.SILVER)
    ),

    //Macbook Pro 16 2023
    Pair(
        "MNW93",
        MacbookSerialNumber(
            MacbookModel.MACBOOK_PRO_16_M2_PRO,
            MacbookRam.GB_16,
            MacbookMemory.TB_1,
            AppleColor.SPACE_GRAY
        )
    ),
    Pair(
        "MNWD3",
        MacbookSerialNumber(MacbookModel.MACBOOK_PRO_16_M2_PRO, MacbookRam.GB_16, MacbookMemory.TB_1, AppleColor.SILVER)
    ),
    Pair(
        "MNW83",
        MacbookSerialNumber(
            MacbookModel.MACBOOK_PRO_16_M2_PRO,
            MacbookRam.GB_16,
            MacbookMemory.GB_512,
            AppleColor.SPACE_GRAY
        )
    ),
    Pair(
        "MNWC3",
        MacbookSerialNumber(
            MacbookModel.MACBOOK_PRO_16_M2_PRO,
            MacbookRam.GB_16,
            MacbookMemory.GB_512,
            AppleColor.SILVER
        )
    ),

    //Macbook Pro 14 2023
    Pair(
        "MPHE3",
        MacbookSerialNumber(
            MacbookModel.MACBOOK_PRO_14_M2_PRO,
            MacbookRam.GB_16,
            MacbookMemory.GB_512,
            AppleColor.SPACE_GRAY
        )
    ),
    Pair(
        "MPHF3",
        MacbookSerialNumber(
            MacbookModel.MACBOOK_PRO_14_M2_PRO,
            MacbookRam.GB_16,
            MacbookMemory.TB_1,
            AppleColor.SPACE_GRAY
        )
    ),
    Pair(
        "MPHG3",
        MacbookSerialNumber(
            MacbookModel.MACBOOK_PRO_14_M2_MAX,
            MacbookRam.GB_32,
            MacbookMemory.TB_1,
            AppleColor.SPACE_GRAY
        )
    ),
    Pair(
        "MPHH3",
        MacbookSerialNumber(
            MacbookModel.MACBOOK_PRO_14_M2_PRO,
            MacbookRam.GB_16,
            MacbookMemory.GB_512,
            AppleColor.SILVER
        )
    ),
    Pair(
        "MPHJ3",
        MacbookSerialNumber(MacbookModel.MACBOOK_PRO_14_M2_PRO, MacbookRam.GB_16, MacbookMemory.TB_1, AppleColor.SILVER)
    ),
    Pair(
        "MPHK3",
        MacbookSerialNumber(MacbookModel.MACBOOK_PRO_14_M2_MAX, MacbookRam.GB_32, MacbookMemory.TB_1, AppleColor.SILVER)
    ),

    )
