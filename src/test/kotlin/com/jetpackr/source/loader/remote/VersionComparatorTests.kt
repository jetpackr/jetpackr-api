package com.jetpackr.source.loader.remote

import io.kotlintest.matchers.collections.shouldContainExactly
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.specs.StringSpec
import mu.KotlinLogging

class VersionComparatorTests : StringSpec() {
    val log = KotlinLogging.logger {}

    init {
        "compare Ruby's versions" {
            val versions= listOf(
                    Pair("v2_3_1", "v2_3_1"),
                    Pair("v2_3_0", "v2_3_0"),
                    Pair("v2_3_5", "v2_3_5"),
                    Pair("v2_3_2", "v2_3_2"),
                    Pair("v2_3_3", "v2_3_3"),
                    Pair("v2_6_0_preview3", "v2_6_0_preview3"),
                    Pair("v2_6_0_preview2", "v2_6_0_preview2")
            ).sortedWith(VersionComparator)
            log.info("versions: {}", versions)

            versions shouldHaveSize 7
            versions shouldContainExactly listOf(
                    Pair("v2_6_0_preview3", "v2_6_0_preview3"),
                    Pair("v2_6_0_preview2", "v2_6_0_preview2"),
                    Pair("v2_3_5", "v2_3_5"),
                    Pair("v2_3_3", "v2_3_3"),
                    Pair("v2_3_2", "v2_3_2"),
                    Pair("v2_3_1", "v2_3_1"),
                    Pair("v2_3_0", "v2_3_0")
            )
        }

        "compare generic versions" {
            val versions= listOf(
                    Pair("3.4", "3.4"),
                    Pair("0.9.1", "0.9.1"),
                    Pair("1.9", "1.9"),
                    Pair("1.1", "1.1"),
                    Pair("2.11", "2.11"),
                    Pair("3.5.2", "3.5.2"),
                    Pair("4.3.1", "4.3.1"),
                    Pair("2.13", "2.13")
            ).sortedWith(VersionComparator)
            log.info("versions: {}", versions)

            versions shouldHaveSize 8
            versions shouldContainExactly listOf(
                    Pair("4.3.1", "4.3.1"),
                    Pair("3.5.2", "3.5.2"),
                    Pair("3.4", "3.4"),
                    Pair("2.13", "2.13"),
                    Pair("2.11", "2.11"),
                    Pair("1.9", "1.9"),
                    Pair("1.1", "1.1"),
                    Pair("0.9.1", "0.9.1")
            )
        }
    }
}