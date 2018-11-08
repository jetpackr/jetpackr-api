package com.jetpackr.core

import org.koin.dsl.module.module
import java.util.concurrent.TimeUnit

val CoreModule = module {
    single(createOnStart=true) {
        @Suppress("UNCHECKED_CAST")
        GeneratorService(get(), listOf(
                "/jetpackr/machine.yml",
                "/jetpackr/kits.yml",
                "/jetpackr/containers.yml"
        )) {
            maximumSize(100)
            refreshAfterWrite(5, TimeUnit.DAYS)
        }
    }
}