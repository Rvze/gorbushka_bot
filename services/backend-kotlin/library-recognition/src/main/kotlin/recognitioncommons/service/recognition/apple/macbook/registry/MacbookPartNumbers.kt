package recognitioncommons.service.recognition.apple.macbook.registry

import recognitioncommons.models.apple.AppleColor
import recognitioncommons.models.apple.macbook.MacbookFullModel
import recognitioncommons.models.apple.macbook.MacbookMemory
import recognitioncommons.models.apple.macbook.MacbookModel
import recognitioncommons.models.apple.macbook.MacbookRam

data class MacbookPartNumInfo(
    val macbookFullModel: MacbookFullModel,
)
object MacbookPartNumbers {
    val numberToMacbook = mapOf(
        // 2020 air m1
        *buildForModelRamAndMemory(
            model = MacbookModel.MACBOOK_AIR_13_M1,
            ram = MacbookRam.GB_8,
            memory = MacbookMemory.GB_256,
            "MGN63" to AppleColor.GOLD,
            "MYDA2" to AppleColor.SILVER,
            "MGND3" to AppleColor.GOLD,
        ),

        "MGND3" to MacbookFullModel(
            model = MacbookModel.MACBOOK_AIR_13_M1,
            ram = MacbookRam.GB_8,
            memory = MacbookMemory.GB_512,
            color = AppleColor.SILVER,
        ),
        "MGNE3" to MacbookFullModel(
            model = MacbookModel.MACBOOK_AIR_13_M1,
            ram = MacbookRam.GB_8,
            memory = MacbookMemory.GB_512,
            color = AppleColor.GOLD,
        ),
        "MGNA3" to MacbookFullModel(
            model = MacbookModel.MACBOOK_AIR_13_M1,
            ram = MacbookRam.GB_8,
            memory = MacbookMemory.GB_512,
            color = AppleColor.SPACE_GRAY,
        ),
        // 2020 pro m1
        "MYDA2" to MacbookFullModel(
            model = MacbookModel.MACBOOK_AIR_13_M1,
            ram = MacbookRam.GB_8,
            memory = MacbookMemory.GB_256,
            color = AppleColor.SILVER,
        ),
        "MYD92" to MacbookFullModel(
            model = MacbookModel.MACBOOK_AIR_13_M1,
            ram = MacbookRam.GB_8,
            memory = MacbookMemory.GB_512,
            color = AppleColor.SILVER,
        ),
        "MYDC2" to MacbookFullModel(
            model = MacbookModel.MACBOOK_AIR_13_M1,
            ram = MacbookRam.GB_16,
            memory = MacbookMemory.GB_512,
            color = AppleColor.SPACE_GRAY,
        ),
        "MYD82" to MacbookFullModel(
            model = MacbookModel.MACBOOK_AIR_13_M1,
            ram = MacbookRam.GB_16,
            memory = MacbookMemory.GB_512,
            color = AppleColor.SILVER,
        ),
    )
}

private fun buildForModelRamAndMemory(
    model: MacbookModel,
    ram: MacbookRam,
    memory: MacbookMemory,
    vararg partNumToColor: (Pair<String, AppleColor>),
): Array<Pair<String, MacbookFullModel>> {
    return partNumToColor.map {
        it.first to MacbookFullModel(
            model = model,
            ram = ram,
            memory = memory,
            color = it.second,
        )
    }.toTypedArray()
}