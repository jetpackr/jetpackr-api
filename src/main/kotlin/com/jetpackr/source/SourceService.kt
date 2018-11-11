package com.jetpackr.source

import com.jetpackr.data.parameter.Option
import com.jetpackr.source.loader.SourceLoader

class SourceService(
        private val loaders: Map<Source.Type, SourceLoader>
) {
    suspend fun load(source: Source): List<Option> {
        return when(source.url.isNullOrBlank()) {
            true -> loaders[source.type]!!.load()
            false -> loaders[source.type]!!.load(source.url)
        }.map {
            Option(value = it)
        }
    }
}