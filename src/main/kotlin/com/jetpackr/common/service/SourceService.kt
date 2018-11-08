package com.jetpackr.common.service

import com.jetpackr.common.data.parameter.Option
import com.jetpackr.common.data.source.Source
import com.jetpackr.common.loader.SourceLoader
import io.ktor.client.HttpClient

class SourceService(
        private val client: HttpClient,
        private val loaders: Map<Source.Type, SourceLoader>
) {
    suspend fun load(source: Source): List<Option> {
        val loader = loaders[source.type]

        if (loader != null)
            return loader.load(source.url).map {
                Option(value = it)
            }
        else
            throw RuntimeException("${source.type}'s Source Loader is empty")
    }
}
