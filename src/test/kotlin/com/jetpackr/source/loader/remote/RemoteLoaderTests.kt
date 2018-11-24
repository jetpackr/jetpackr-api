package com.jetpackr.source.loader.remote

import com.jetpackr.common.fullUrl
import io.kotlintest.Description
import io.kotlintest.matchers.collections.shouldContainExactly
import io.kotlintest.specs.StringSpec
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.mockk.coEvery
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging

class RemoteLoaderTests : StringSpec() {
    val log = KotlinLogging.logger {}

    val mockEngine = MockEngine { when (url.fullUrl) {} }
    val client = HttpClient(mockEngine)
    val loader = object: RemoteLoader(client) {
        override suspend fun doLoad(url: String): List<Pair<String, String>> = listOf()
    }
    val mock = spyk(loader)

    val RUBY_URL = "https://api.github.com/repos/ruby/ruby/tags"
    val LOAD_RUBY_VERSION = "load Ruby's versions"
    val RUBY_VERSIONS = listOf(
            Pair("v2_3_1", "v2_3_1"),
            Pair("v2_3_0", "v2_3_0"),
            Pair("v2_3_5", "v2_3_5"),
            Pair("v2_3_2", "v2_3_2"),
            Pair("v2_3_3", "v2_3_3"),
            Pair("v2_6_0_preview3", "v2_6_0_preview3"),
            Pair("v2_6_0_preview2", "v2_6_0_preview2")
    )

    val GENERIC_URL = "https://generic-repo-url.com/repos/generic-app/versions"
    val LOAD_GENERIC_VERSION = "load generic versions"
    val GENERIC_VERSIONS = listOf(
            Pair("3.4", "3.4"),
            Pair("0.9.1", "0.9.1"),
            Pair("3.2.1-preview2", "3.2.1-preview2"),
            Pair("1.9", "1.9"),
            Pair("1.1", "1.1"),
            Pair("2.11", "2.11"),
            Pair("1.4.4-beta1", "1.4.4-beta1"),
            Pair("3.5.2", "3.5.2"),
            Pair("4.3.1", "4.3.1"),
            Pair("2.13", "2.13")
    )

    override fun beforeTest(description: Description) = runBlocking {
        val name = description.name

        if (name.contains(LOAD_RUBY_VERSION)) {
            coEvery { mock.doLoad(RUBY_URL) } returns RUBY_VERSIONS
        }

        if (name.contains(LOAD_GENERIC_VERSION)) {
            coEvery { mock.doLoad(GENERIC_URL) } returns GENERIC_VERSIONS
        }
    }

    init {
        LOAD_RUBY_VERSION {
            runBlocking {
                val versions = mock.load(RUBY_URL)
                log.info("version: {}", versions)

                versions `shouldContainExactly` listOf(
                        Pair("v2_3_5", "v2_3_5"),
                        Pair("v2_3_3", "v2_3_3"),
                        Pair("v2_3_2", "v2_3_2"),
                        Pair("v2_3_1", "v2_3_1"),
                        Pair("v2_3_0", "v2_3_0")
                )

                Any()
            }
        }

        LOAD_GENERIC_VERSION {
            runBlocking {
                val versions = mock.load(GENERIC_URL)
                log.info("version: {}", versions)

                versions `shouldContainExactly` listOf(
                        Pair("4.3.1", "4.3.1"),
                        Pair("3.5.2", "3.5.2"),
                        Pair("3.4", "3.4"),
                        Pair("2.13", "2.13"),
                        Pair("2.11", "2.11"),
                        Pair("1.9", "1.9"),
                        Pair("1.1", "1.1"),
                        Pair("0.9.1", "0.9.1")
                )

                Any()
            }
        }
    }
}