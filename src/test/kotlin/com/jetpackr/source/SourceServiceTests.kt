package com.jetpackr.source

import com.jetpackr.data.parameter.Option
import com.jetpackr.source.Source.Local.Timezone
import com.jetpackr.source.Source.Remote.DockerHub
import com.jetpackr.source.Source.Remote.GitHub
import com.jetpackr.source.Source.Remote.NPMRegistry
import com.jetpackr.source.loader.local.TimezoneLoader
import com.jetpackr.source.loader.remote.DockerHubLoader
import com.jetpackr.source.loader.remote.NPMRegistryLoader
import io.kotlintest.Description
import io.kotlintest.matchers.collections.shouldContainExactly
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking

class SourceServiceTests : StringSpec() {

    val timezoneLoader = mockk<TimezoneLoader>()
    val localLoaders = mapOf(
            Timezone to timezoneLoader
    )

    val dockerHubUrl = "https://registry.hub.docker.com/v2/repositories/library/rabbitmq/tags"
    val dockerHubLoader = mockk<DockerHubLoader>()

    val npmRegistryUrl = "https://registry.npmjs.com/@vue/cli"
    val npmRegistryLoader = mockk<NPMRegistryLoader>()
    val remoteLoaders = mapOf(
            DockerHub to dockerHubLoader,
            NPMRegistry to npmRegistryLoader
    )

    val sourceService = SourceService(
            localLoaders,
            remoteLoaders
    )

    override fun beforeTest(description: Description) {
        every {
            runBlocking {
                dockerHubLoader.load(dockerHubUrl)
            }
        } returns listOf(
                Pair("3.7.8-management-alpine", "3.7.8-management-alpine"),
                Pair("3.7.7-alpine", "3.7.7-alpine"),
                Pair("3.7.6", "3.7.6"),
                Pair("2.1", "2.1.0")
        )

        every {
            runBlocking {
                npmRegistryLoader.load(npmRegistryUrl)
            }
        } returns listOf(
                Pair("3.1.2", "3.1.2"),
                Pair("3.1.0", "3.1.0"),
                Pair("3.0", "3.0.0"),
                Pair("1.1", "1.1.0")
        )

        every {
            runBlocking {
                timezoneLoader.load()
            }
        } returns listOf(
                Pair("(UTC+02:00) Asia/Nicosia", "Asia/Nicosia"),
                Pair("(UTC+00:00) Atlantic/Faroe", "Atlantic/Faroe"),
                Pair("(UTC+02:00) Europe/Tallinn", "Europe/Tallinn"),
                Pair("(UTC+06:00) Indian/Chagos", "Indian/Chagos"),
                Pair("Pacific/Auckland", "Pacific/Auckland"),
                Pair("(UTC+12:00) Pacific/Majuro", "Pacific/Majuro")
        )
    }

    init {
        "load from DockerHub" {
            runBlocking {
                val options = sourceService.load(
                        Source(
                                remote = DockerHub,
                                url = dockerHubUrl
                        )
                )

                options shouldContainExactly listOf(
                        Option(value = "3.7.8-management-alpine"),
                        Option(value = "3.7.7-alpine"),
                        Option(value = "3.7.6"),
                        Option("2.1", "2.1.0")
                )

                Any()
            }
        }

        "load from NPMRegistry" {
            runBlocking {
                val options = sourceService.load(
                        Source(
                                remote = NPMRegistry,
                                url = npmRegistryUrl
                        )
                )

                options shouldContainExactly listOf(
                        Option(value = "3.1.2"),
                        Option(value = "3.1.0"),
                        Option("3.0", "3.0.0"),
                        Option("1.1", "1.1.0")
                )

                Any()
            }
        }

        "load from Timezone" {
            runBlocking {
                val options = sourceService.load(
                        Source(
                                local = Timezone
                        )
                )

                options shouldContainExactly listOf(
                        Option("(UTC+02:00) Asia/Nicosia", "Asia/Nicosia"),
                        Option("(UTC+00:00) Atlantic/Faroe", "Atlantic/Faroe"),
                        Option("(UTC+02:00) Europe/Tallinn", "Europe/Tallinn"),
                        Option("(UTC+06:00) Indian/Chagos", "Indian/Chagos"),
                        Option(value = "Pacific/Auckland"),
                        Option("(UTC+12:00) Pacific/Majuro", "Pacific/Majuro")
                )

                Any()
            }
        }

        shouldThrow<RuntimeException>  {
            runBlocking {
                sourceService.load(
                        Source(
                                remote = GitHub,
                                url = "https://api.github.com/repos/ruby/ruby/tags"
                        )
                )
            }
        }
    }
}