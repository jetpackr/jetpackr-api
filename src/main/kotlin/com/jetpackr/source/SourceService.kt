package com.jetpackr.source

import com.jetpackr.data.parameter.Option
import com.jetpackr.source.loader.LocalSourceLoader
import com.jetpackr.source.loader.RemoteSourceLoader

class SourceService(
        private val localSourceLoaders: Map<Source.Type, LocalSourceLoader>,
        private val remoteSourceLoaders: Map<Source.Type, RemoteSourceLoader>
) {
    suspend fun load(source: Source): List<Option> {
        return when(source.url.isNullOrBlank()) {
            true -> localSourceLoaders[source.type]!!.load()
            false -> remoteSourceLoaders[source.type]!!.load(source.url)
        }.map {
            Option(value = it)
        }
    }
}