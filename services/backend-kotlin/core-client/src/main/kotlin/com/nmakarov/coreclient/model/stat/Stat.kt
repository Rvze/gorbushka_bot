package com.nmakarov.coreclient.model.stat

import com.nmakarov.coreclient.util.localDateNowMoscow
import java.time.LocalDate

data class Stat(
    val statRegistry: StatRegistry,
    val atDate: LocalDate = localDateNowMoscow(),
)
