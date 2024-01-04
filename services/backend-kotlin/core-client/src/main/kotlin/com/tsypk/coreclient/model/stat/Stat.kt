package com.tsypk.coreclient.model.stat

import com.tsypk.coreclient.util.localDateNowMoscow
import java.time.LocalDate

data class Stat(
    val statRegistry: StatRegistry,
    val atDate: LocalDate = localDateNowMoscow(),
)
