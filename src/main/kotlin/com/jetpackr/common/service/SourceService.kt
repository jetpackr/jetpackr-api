package com.jetpackr.common.service

import com.jetpackr.common.data.parameter.Option
import com.jetpackr.common.data.source.Source
import com.jetpackr.common.loader.SourceLoader
import io.ktor.client.HttpClient
import kotlinx.coroutines.runBlocking

class SourceService(
        private val sourceLoaders: Map<Source.Type, SourceLoader>,
        private val client: HttpClient
) {
    init {
        if (sourceLoaders == null)
            throw RuntimeException("Source Loaders are empty")
    }

    fun load(source: Source): List<Option> = runBlocking {
        val sourceLoader: SourceLoader? = sourceLoaders[source.type]

        if (sourceLoader != null)
            sourceLoader.invoke(source.url, client)
        else
            throw RuntimeException("${source.type}'s Source Loader is empty")
    }
}
