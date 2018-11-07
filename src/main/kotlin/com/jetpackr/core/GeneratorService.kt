package com.jetpackr.core

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import com.jetpackr.configuration.Jetpackr
import com.jetpackr.log
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.io.SequenceInputStream
import java.util.Collections

class GeneratorService constructor(
        private val mapper: ObjectMapper,
        builder: Caffeine<String, Jetpackr>,
        private val files: List<String>) {
    private val cache: LoadingCache<String, Jetpackr>

    init {
        cache = builder.build { initialize() }
        cache.get("jetpackr") { initialize() }
    }

    private fun initialize(): Jetpackr {
        log.info("initializing....")
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