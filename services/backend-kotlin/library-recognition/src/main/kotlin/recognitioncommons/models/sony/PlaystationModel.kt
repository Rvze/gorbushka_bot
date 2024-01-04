package recognitioncommons.models.sony

enum class PlaystationModel(
    val revision: PlaystationRevision,
    val edition: PlaystationEdition
) {
    PS5_DIGITAL_EDITION_2022(
        revision = PlaystationRevision.SECOND_REVISION,
        edition = PlaystationEdition.DIGITAL_EDITION
    ),
    PS5_DIGITAL_EDITION_2023(
        revision = PlaystationRevision.THIRD_REVISION,
        edition = PlaystationEdition.DIGITAL_EDITION
    ),
    PS5_DISK_EDITION_2022(
        revision = PlaystationRevision.SECOND_REVISION,
        edition = PlaystationEdition.DISK_EDITION
    ),
    PS5_DISK_EDITION_2023(
        revision = PlaystationRevision.THIRD_REVISION,
        edition = PlaystationEdition.DISK_EDITION
    )
}