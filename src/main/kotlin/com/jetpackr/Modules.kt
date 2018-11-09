package com.jetpackr

import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.JsonDeserializer
import com.jetpackr.data.parameter.Select
import com.jetpackr.generator.GeneratorService
import com.jetpackr.mapper.YamlMapper
import com.jetpackr.mapper.deserializer.SelectDeserializer
import com.jetpackr.source.Source.Type.DockerHub
import com.jetpackr.source.Source.Type.GitHub
import com.jetpackr.source.Source.Type.NPMRegistry
import com.jetpackr.source.Source.Type.SDKMAN
import com.jetpackr.source.Source.Type.TimeZone
import com.jetpackr.source.SourceService
import com.jetpackr.source.loader.DockerHubLoader
import com.jetpackr.source.loader.GitHubLoader
import com.jetpackr.source.loader.NPMRegistryLoader
import com.jetpackr.source.loader.SDKMANLoader
import com.jetpackr.source.loader.TimeZoneLoader
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import org.koin.dsl.module.module
import java.util.concurrent.TimeUnit.DAYS

val SourceModule = module {
    single {
        HttpClient(Apache) {
            install(JsonFeature) {
                serializer = JacksonSerializer {
                    configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
                }
            }
        }
    }

    single("sourceLoaders") {
        mapOf(
                DockerHub to DockerHubLoader(get()),
                GitHub to GitHubLoader(get()),
                NPMRegistry to NPMRegistryLoader(get()),
                SDKMAN to SDKMANLoader(get()),
                TimeZone to TimeZoneLoader(get())
        )
    }

    single {
        SourceService(get("sourceLoaders"))
    }
}

val MapperModule = module {
    single("selectDeserializer") {
        SelectDeserializer(get()) as JsonDeserializer<Select>
    }

    single {
        YamlMapper {
            addDeserializer(Select::class.java, get("selectDeserializer"))
        }
    }
}

val GeneratorModule = module {
    single("data") {
        listOf(
                "/jetpackr/machine.yml",
                "/jetpackr/kits.yml",
                "/jetpackr/containers.yml"
        )
    }

    single(createOnStart=true) {
        @Suppress("UNCHECKED_CAST")
        (GeneratorService(get(), get("data")) {
            maximumSize(100)
            refreshAfterWrite(5, DAYS)
        })
    }
}