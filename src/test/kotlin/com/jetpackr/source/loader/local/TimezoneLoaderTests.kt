package com.jetpackr.source.loader.local

import io.kotlintest.Description
import io.kotlintest.specs.StringSpec
import io.mockk.every
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should contain all`

class TimezoneLoaderTests : StringSpec() {
    val log = KotlinLogging.logger {}

    val mock = spyk<TimezoneLoader>()

    val DEPRECATED_TIMEZONES = listOf(
            "WINDOWS~Europe/Helsinki",
            "MILITARY~UTC+12:00",
            "Etc/GMT+10",
            "MET",
            "HST"
    )

    val VALID_TIMEZONES = listOf(
            "Atlantic/Faroe",
            "Asia/Nicosia",
            "Pacific/Majuro",
            "Europe/Tallinn",
            "Indian/Chagos"
    )

    override fun beforeTest(description: Description) {
        val name = description.name

        if (name.contains("deprecated timezones")) {
            every { mock["getZoneIds"]() } returns DEPRECATED_TIMEZONES
        }

        if (name.contains("valid timezones")) {
            every { mock["getZoneIds"]() } returns VALID_TIMEZONES
        }
    }
    init {
        "return deprecated timezones" {
            runBlocking {
                val timezones = mock.load()
                log.info("timezones: {}", timezones)

                timezones.size `should be equal to` 0

                Any()
            }
        }

        "return valid timezones" {
            runBlocking {
                val timezones = mock.load()
                log.info("timezones: {}", timezones)

                timezones.size `should be equal to` 5
                timezones.map { it.second } `should contain all` listOf(
                        "Atlantic/Faroe",
                        "Pacific/Majuro",
                        "Indian/Chagos"
                )

                Any()
            }
        }
    }
}