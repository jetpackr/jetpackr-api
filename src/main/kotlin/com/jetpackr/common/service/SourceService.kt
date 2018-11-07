package com.jetpackr.common.service

import com.jetpackr.common.data.parameter.Option
import com.jetpackr.common.data.source.Source
import com.jetpackr.common.loader.SourceLoader
import io.ktor.client.HttpClient

class SourceService(
        private val sourceLoaders: Map<Source.Type, SourceLoader>,
        private val client: HttpClient
) {
    suspend fun load(source: Source): List<Option> {
        val sourceLoader = sourceLoaders[source.type]

        if (sourceLoader != null)
            return sourceLoader.invoke(source.url, client)
        else
            throw RuntimeException("${source.type}'s Source Loader is empty")
    }
}
