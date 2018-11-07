package com.jetpackr.core

import com.github.benmanes.caffeine.cache.Caffeine
import com.jetpackr.configuration.Jetpackr
import org.koin.dsl.module.module
import java.util.concurrent.TimeUnit

val CoreModule = module {
    single(createOnStart=true) {
        @Suppress("UNCHECKED_CAST")
        GeneratorService(
                get(),
                Caffeine.newBuilder()
                        .maximumSize(100)
                        .expireAfterWrite(5, TimeUnit.DAYS)
                        .refreshAfterWrite(5, TimeUnit.DAYS) as Caffeine<String, Jetpackr>,
                listOf(
                        "/jetpackr/machine.yml",
                        "/jetpackr/kits.yml",
                        "/jetpackr/containers.yml"
                )
        )
    }
}