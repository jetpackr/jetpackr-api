package com.jetpackr.source.loader.remote

import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.jetpackr.common.fullUrl
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.specs.StringSpec
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockHttpResponse
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.http.ContentType.Application
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.io.ByteReadChannel
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.amshove.kluent.`should contain all`

class GitHubLoaderTests : StringSpec() {
    val log = KotlinLogging.logger {}

    val NODE = Pair(
            "https://api.github.com/repos/nodejs/node/tags",
            """
                [
                    {
                        "name": "v11.2.0",
                        "zipball_url": "https://api.github.com/repos/nodejs/node/zipball/v11.2.0",
                        "tarball_url": "https://api.github.com/repos/nodejs/node/tarball/v11.2.0",
                        "commit": {
                            "sha": "a19e1aba38d4bd761b892b7384c457c6377e942c",
                            "url": "https://api.github.com/repos/nodejs/node/commits/a19e1aba38d4bd761b892b7384c457c6377e942c"
                        },
                        "node_id": "MDM6UmVmMjcxOTM3Nzk6djExLjIuMA=="
                    },
                    {
                        "name": "v11.1.0",
                        "zipball_url": "https://api.github.com/repos/nodejs/node/zipball/v11.1.0",
                        "tarball_url": "https://api.github.com/repos/nodejs/node/tarball/v11.1.0",
                        "commit": {
                            "sha": "af6d26281f734dbbf1bf497103d1cb4b85a52b2e",
                            "url": "https://api.github.com/repos/nodejs/node/commits/af6d26281f734dbbf1bf497103d1cb4b85a52b2e"
                        },
                        "node_id": "MDM6UmVmMjcxOTM3Nzk6djExLjEuMA=="
                    },
                    {
                        "name": "v9.7.1",
                        "zipball_url": "https://api.github.com/repos/nodejs/node/zipball/v9.7.1",
                        "tarball_url": "https://api.github.com/repos/nodejs/node/tarball/v9.7.1",
                        "commit": {
                            "sha": "316926f66d666d669e174b38892e2e2488686cd3",
                            "url": "https://api.github.com/repos/nodejs/node/commits/316926f66d666d669e174b38892e2e2488686cd3"
                        },
                        "node_id": "MDM6UmVmMjcxOTM3Nzk6djkuNy4x"
                    },
                    {
                        "name": "v9.7.0",
                        "zipball_url": "https://api.github.com/repos/nodejs/node/zipball/v9.7.0",
                        "tarball_url": "https://api.github.com/repos/nodejs/node/tarball/v9.7.0",
                        "commit": {
                            "sha": "85bbd43fb4930ecaa0c5b0aa248472a781d0eb1e",
                            "url": "https://api.github.com/repos/nodejs/node/commits/85bbd43fb4930ecaa0c5b0aa248472a781d0eb1e"
                        },
                        "node_id": "MDM6UmVmMjcxOTM3Nzk6djkuNy4w"
                    },
                    {
                        "name": "v9.6.0",
                        "zipball_url": "https://api.github.com/repos/nodejs/node/zipball/v9.6.0",
                        "tarball_url": "https://api.github.com/repos/nodejs/node/tarball/v9.6.0",
                        "commit": {
                            "sha": "cb18511275a4138c9db631b3e48a33c7143c5560",
                            "url": "https://api.github.com/repos/nodejs/node/commits/cb18511275a4138c9db631b3e48a33c7143c5560"
                        },
                        "node_id": "MDM6UmVmMjcxOTM3Nzk6djkuNi4w"
                    }
                ]
            """.trimIndent()
    )

    val RUBY = Pair(
            "https://api.github.com/repos/ruby/ruby/tags",
            """
                [
                    {
                        "name": "v2_6_0_preview3",
                        "zipball_url": "https://api.github.com/repos/ruby/ruby/zipball/v2_6_0_preview3",
                        "tarball_url": "https://api.github.com/repos/ruby/ruby/tarball/v2_6_0_preview3",
                        "commit": {
                          "sha": "7ccad5680db6fcef10f7f6349681a2377c50a2c9",
                          "url": "https://api.github.com/repos/ruby/ruby/commits/7ccad5680db6fcef10f7f6349681a2377c50a2c9"
                        },
                        "node_id": "MDM6UmVmNTM4NzQ2OnYyXzZfMF9wcmV2aWV3Mw=="
                    },
                    {
                        "name": "v2_6_0_preview2",
                        "zipball_url": "https://api.github.com/repos/ruby/ruby/zipball/v2_6_0_preview2",
                        "tarball_url": "https://api.github.com/repos/ruby/ruby/tarball/v2_6_0_preview2",
                        "commit": {
                          "sha": "6f59db30c18cd793cbf6169301158493df7e78e4",
                          "url": "https://api.github.com/repos/ruby/ruby/commits/6f59db30c18cd793cbf6169301158493df7e78e4"
                        },
                        "node_id": "MDM6UmVmNTM4NzQ2OnYyXzZfMF9wcmV2aWV3Mg=="
                    },
                    {
                        "name": "v2_3_4",
                        "zipball_url": "https://api.github.com/repos/ruby/ruby/zipball/v2_3_4",
                        "tarball_url": "https://api.github.com/repos/ruby/ruby/tarball/v2_3_4",
                        "commit": {
                            "sha": "4bd69735af901266ec21486243fc206030caa6b9",
                            "url": "https://api.github.com/repos/ruby/ruby/commits/4bd69735af901266ec21486243fc206030caa6b9"
                        },
                        "node_id": "MDM6UmVmNTM4NzQ2OnYyXzNfNA=="
                    },
                    {
                        "name": "v2_3_3",
                        "zipball_url": "https://api.github.com/repos/ruby/ruby/zipball/v2_3_3",
                        "tarball_url": "https://api.github.com/repos/ruby/ruby/tarball/v2_3_3",
                        "commit": {
                            "sha": "c91cb76f8d84b2963f6ede2ef445ad46a6104216",
                            "url": "https://api.github.com/repos/ruby/ruby/commits/c91cb76f8d84b2963f6ede2ef445ad46a6104216"
                        },
                        "node_id": "MDM6UmVmNTM4NzQ2OnYyXzNfMw=="
                    },
                    {
                        "name": "v2_3_1",
                        "zipball_url": "https://api.github.com/repos/ruby/ruby/zipball/v2_3_1",
                        "tarball_url": "https://api.github.com/repos/ruby/ruby/tarball/v2_3_1",
                        "commit": {
                            "sha": "5827d8e887d881eb3a6e6ea7410590261c90545f",
                            "url": "https://api.github.com/repos/ruby/ruby/commits/5827d8e887d881eb3a6e6ea7410590261c90545f"
                        },
                        "node_id": "MDM6UmVmNTM4NzQ2OnYyXzNfMQ=="
                    },
                    {
                        "name": "v2_3_0",
                        "zipball_url": "https://api.github.com/repos/ruby/ruby/zipball/v2_3_0",
                        "tarball_url": "https://api.github.com/repos/ruby/ruby/tarball/v2_3_0",
                        "commit": {
                            "sha": "d40ea2afa6ff5a6e5befcf342fb7b6dc58796b20",
                            "url": "https://api.github.com/repos/ruby/ruby/commits/d40ea2afa6ff5a6e5befcf342fb7b6dc58796b20"
                        },
                        "node_id": "MDM6UmVmNTM4NzQ2OnYyXzNfMA=="
                    },
                    {
                        "name": "v2_3_0_preview2",
                        "zipball_url": "https://api.github.com/repos/ruby/ruby/zipball/v2_3_0_preview2",
                        "tarball_url": "https://api.github.com/repos/ruby/ruby/tarball/v2_3_0_preview2",
                        "commit": {
                            "sha": "e3434401aca2e331132652d4458366267e8cf378",
                            "url": "https://api.github.com/repos/ruby/ruby/commits/e3434401aca2e331132652d4458366267e8cf378"
                        },
                        "node_id": "MDM6UmVmNTM4NzQ2OnYyXzNfMF9wcmV2aWV3Mg=="
                    }
                ]
            """.trimIndent()
    )

    val mockEngine = MockEngine {
        log.info("url: {}", url.fullUrl)
        when (url.fullUrl) {
            NODE.first -> {
                MockHttpResponse(
                        call,
                        HttpStatusCode.OK,
                        ByteReadChannel(NODE.second.toByteArray(Charsets.UTF_8)),
                        headersOf("Content-Type" to listOf(Application.Json.toString()))
                )
            }
            RUBY.first -> {
                MockHttpResponse(
                        call,
                        HttpStatusCode.OK,
                        ByteReadChannel(RUBY.second.toByteArray(Charsets.UTF_8)),
                        headersOf("Content-Type" to listOf(Application.Json.toString()))
                )
            }
            else -> {
                error("Unhandled ${url.fullUrl}")
            }
        }
    }

    val client = HttpClient(mockEngine) {
        install(JsonFeature) {
            serializer = JacksonSerializer {
                configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
            }
        }
    }

    val loader = GitHubLoader(client)

    init {
        "load Node.js' versions" {
            runBlocking {
                val versions = loader.load(NODE.first)
                log.info("versions: {}", versions)

                versions `shouldHaveSize` 5
                versions.map { it.second } `should contain all` listOf("v9.7.0", "v11.2.0", "v9.7.1")

                Any()
            }
        }

        "load Ruby's versions" {
            runBlocking {
                val versions = loader.load(RUBY.first)
                log.info("versions: {}", versions)

                versions `shouldHaveSize` 4
                versions.map { it.second } `should contain all` listOf("v2_3_0", "v2_3_3")

                Any()
            }
        }
    }
}