package recognitioncommons.models.sony

import recognitioncommons.models.country.Country

data class PlayStationFullModel(
    val model: PlaystationModel,
    val edition: PlaystationEdition,
    val revision: PlaystationRevision,
    val year: Int,
    val country: Country? = null
) {
    val idNumber: String = getModel(revision, edition, country = country)
}