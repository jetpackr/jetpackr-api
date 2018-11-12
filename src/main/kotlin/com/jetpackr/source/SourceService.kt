package com.jetpackr.source

import com.jetpackr.data.parameter.Option
import com.jetpackr.source.loader.SourceLoader

class SourceService(
        private val loaders: Map<Source.Type, SourceLoader>
) {
    suspend fun load(source: Source): List<Option> {

        val loader = loaders[source.type]

        if (loader == null)
            throw RuntimeException("${source.type}'s Source Loader is empty")
        else
            return loader.load(source.url).map {
                Option(value = it)
            }
    }
}