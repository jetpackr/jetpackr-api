package com.jetpackr

import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.JsonDeserializer
import com.jetpackr.cache.CacheService
import com.jetpackr.data.parameter.Select
import com.jetpackr.generator.GeneratorService
import com.jetpackr.mapper.YamlMapper
import com.jetpackr.mapper.deserializer.SelectDeserializer
import com.jetpackr.source.Source.Local.Timezone
import com.jetpackr.source.Source.Remote.DockerHub
import com.jetpackr.source.Source.Remote.GitHub
import com.jetpackr.source.Source.Remote.NPMRegistry
import com.jetpackr.source.Source.Remote.SDKMAN
import com.jetpackr.source.SourceService
import com.jetpackr.source.loader.local.TimezoneLoader
import com.jetpackr.source.loader.remote.DockerHubLoader
import com.jetpackr.source.loader.remote.GitHubLoader
import com.jetpackr.source.loader.remote.NPMRegistryLoader
import com.jetpackr.source.loader.remote.SDKMANLoader
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

    single("localLoaders") {
        mapOf(
                Timezone to TimezoneLoader()
        )
    }

    single("remoteLoaders") {
        mapOf(
                DockerHub to DockerHubLoader(get()),
                GitHub to GitHubLoader(get()),
                NPMRegistry to NPMRegistryLoader(get()),
                SDKMAN to SDKMANLoader(get())
        )
    }

    single {
        SourceService(get("localLoaders"), get("remoteLoaders"))
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

val CacheModule = module {
    single("data") {
        listOf(
                "/jetpackr/machine.yml",
                "/jetpackr/kits.yml",
                "/jetpackr/containers.yml"
        )
    }

    single(createOnStart=true) {
        @Suppress("UNCHECKED_CAST")
        CacheService(get(), get("data")) {
            maximumSize(100)
            refreshAfterWrite(5, DAYS)
        }
    }
}

val GeneratorModule = module {
    single {
        GeneratorService()
    }
}