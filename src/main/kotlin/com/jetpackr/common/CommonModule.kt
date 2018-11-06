package com.jetpackr.common

import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.jetpackr.common.data.parameter.Select
import com.jetpackr.common.data.source.Source.Type.DockerHub
import com.jetpackr.common.data.source.Source.Type.GitHub
import com.jetpackr.common.data.source.Source.Type.NPMRegistry
import com.jetpackr.common.data.source.Source.Type.SDKMAN
import com.jetpackr.common.data.source.Source.Type.TimeZone
import com.jetpackr.common.deserializer.SelectDeserializer
import com.jetpackr.common.loader.DockerHubLoader
import com.jetpackr.common.loader.GitHubLoader
import com.jetpackr.common.loader.NPMRegistryLoader
import com.jetpackr.common.loader.SDKMANLoader
import com.jetpackr.common.loader.TimeZoneLoader
import com.jetpackr.common.mapper.YamlMapper
import com.jetpackr.common.service.SourceService
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import org.koin.dsl.module.module

val CommonModule = module {
    single {
        mapOf(
                DockerHub to DockerHubLoader,
                GitHub to GitHubLoader,
                NPMRegistry to NPMRegistryLoader,
                SDKMAN to SDKMANLoader,
                TimeZone to TimeZoneLoader
        )
    }

    single {
        HttpClient (Apache) {
            install(JsonFeature) {
                serializer = JacksonSerializer {
                    configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
                }
            }
        }
    }

    single {
        SourceService(get(), get())
    }

    single {
        YamlMapper {
            addDeserializer(Select::class.java, SelectDeserializer(get()))
        }
    }
}