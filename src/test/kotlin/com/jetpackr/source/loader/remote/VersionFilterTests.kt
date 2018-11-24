package com.jetpackr.source.loader.remote

import io.kotlintest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.specs.StringSpec
import mu.KotlinLogging

class VersionFilterTests : StringSpec() {
    val log = KotlinLogging.logger {}

    init {
        "filter versions" {
            val versions = listOf(
                    Pair("1.23-preview4", "1.23-preview4"),
                    Pair("1.1.1-windowsservercore", "1.1.1-windowsservercore"),
                    Pair("4.2-alpha2", "4.2.alpha2"),
                    Pair("6.55-rc2", "6.55-rc2"),
                    Pair("5.0-beta3", "5.0-beta3"),
                    Pair("0.1.5", "0.1.5"),
                    Pair("1.3.1-nanoserver", "1.3.1-nanoserver"),
                    Pair("2.57-next755", "2.57-next755"),
                    Pair("1.2.7", "1.2.7"),
                    Pair("7.6", "7.6"),
                    Pair("4.3.3-windowsservercore", "4.3.3-windowsservercore"),
                    Pair("4.3.2", "4.3.2"),
                    Pair("2.2.1-deprecated", "2.2.1-deprecated"),
                    Pair("3.2.5", "3.2.5"),
                    Pair("4.3.3-windowsservercore", "4.3.3-windowsservercore"),
                    Pair("7.0.5-nanoserver", "7.0.5-nanoserver"),
                    Pair("0.9", "0.9"),
                    Pair("2.6.7-rc6", "2.6.7-rc6")
            ).filter(VersionFilter)
            log.info("versions: {}", versions)

            versions `shouldHaveSize` 8
            versions `shouldContainExactlyInAnyOrder` listOf(
                    Pair("1.2.7", "1.2.7"),
                    Pair("2.6.7-rc6", "2.6.7-rc6"),
                    Pair("7.6", "7.6"),
                    Pair("4.3.2", "4.3.2"),
                    Pair("6.55-rc2", "6.55-rc2"),
                    Pair("0.1.5", "0.1.5"),
                    Pair("3.2.5", "3.2.5"),
                    Pair("0.9", "0.9")
            )
        }
    }
}