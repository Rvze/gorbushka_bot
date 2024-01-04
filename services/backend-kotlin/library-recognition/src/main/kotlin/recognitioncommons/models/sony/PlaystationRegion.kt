package recognitioncommons.models.sony

import recognitioncommons.models.country.Country

fun findRegionByCountry(country: Country?) =
    when (country) {
        Country.JAPAN -> "00"
        Country.AUSTRALIA, Country.NEW_ZEALAND -> "02"
        Country.INDIA, Country.RUSSIA, Country.UKRAINE, Country.KAZAKHSTAN,
        Country.CHINA -> "09"

        Country.KYRGYZ_REPUBLIC, Country.TAJIKISTAN, Country.TURKMENISTAN, Country.UZBEKISTAN -> "08"
        Country.MEXICO, Country.BRAZIL -> "14"
        Country.CANADA, Country.USA -> "15"
        Country.EUROPE -> "16"
        Country.SOUTH_KOREA -> "18"
        else -> {
            ""
        }
    }