package com.jetpackr.core

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import com.jetpackr.configuration.Jetpackr
import mu.KotlinLogging
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.io.SequenceInputStream
import java.util.Collections

class GeneratorService constructor(
        private val mapper: ObjectMapper,
        private val files: List<String>,
        block: Caffeine<String, Jetpackr>.() -> Unit) {
    private val log = KotlinLogging.logger {}
    private val cache: LoadingCache<String, Jetpackr>

    init {
        @Suppress("UNCHECKED_CAST")
        val builder = (Caffeine.newBuilder() as Caffeine<String, Jetpackr>).apply(block)

        cache = builder.build {
            log.info("Reloading cache...")
            val jetpackr = build()
            log.info("Cache has been reloaded successfully")
            jetpackr
        }

        cache.put("jetpackr", build())
    }

    private fun build(): Jetpackr {
        val inputStreams = mutableListOf<InputStream>()

        for ((index, file) in files.withIndex()) {
            inputStreams += this::class.java.getResourceAsStream(file)

            if (index != files.lastIndex)
                inputStreams += ByteArrayInputStream("\n".toByteArray())
        }

        val inputStream = SequenceInputStream(
                Collections.enumeration(inputStreams.toSet())
        )

        return mapper.readValue(inputStream)
    }

    fun load(): Jetpackr {
        return cache.get("jetpackr")!!
    }
}