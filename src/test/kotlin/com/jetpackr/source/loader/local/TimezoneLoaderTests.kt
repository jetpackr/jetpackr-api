package com.jetpackr.source.loader.local

import io.kotlintest.Description
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.specs.StringSpec
import io.mockk.every
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.amshove.kluent.`should contain all`

class TimezoneLoaderTests : StringSpec() {
    private val log = KotlinLogging.logger {}

    private val mock = spyk<TimezoneLoader>()

    private val loadDeprecatedTimezones = "load deprecated timezones"
    private val deprecatedTimezones = listOf(
            "WINDOWS~Europe/Helsinki",
            "MILITARY~UTC+12:00",
            "Etc/GMT+10",
            "MET",
            "HST"
    )

    private val loadValidTimezones = "load valid timezones"
    private val validTimezones = listOf(
            "Atlantic/Faroe",
            "Asia/Nicosia",
            "Pacific/Majuro",
            "Europe/Tallinn",
            "Indian/Chagos"
    )

    override fun beforeTest(description: Description) {
        val name = description.name

        if (name.contains(loadDeprecatedTimezones)) {
            every { mock["getZoneIds"]() } returns deprecatedTimezones
        }

        if (name.contains(loadValidTimezones)) {
            every { mock["getZoneIds"]() } returns validTimezones
        }
    }

    init {
        loadDeprecatedTimezones {
            runBlocking {
                val timezones = mock.load()
                log.info("timezones: {}", timezones)

                timezones shouldHaveSize 0

                Any()
            }
        }

        loadValidTimezones {
            runBlocking {
                val timezones = mock.load()
                log.info("timezones: {}", timezones)

                timezones shouldHaveSize 5
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