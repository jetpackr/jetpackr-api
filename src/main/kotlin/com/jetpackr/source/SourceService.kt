package com.jetpackr.source

import com.jetpackr.data.parameter.Option
import com.jetpackr.source.loader.remote.RemoteLoader
import com.jetpackr.source.loader.local.LocalLoader

class SourceService(
        private val localLoaders: Map<Source.Local, LocalLoader>,
        private val remoteLoaders: Map<Source.Remote, RemoteLoader>
) {
    suspend fun load(source: Source): List<Option> {
        val localLoader = localLoaders[source.local]
        val remoteLoader = remoteLoaders[source.remote]

        if (localLoader != null)
            return localLoader.load().map {
                if (it.key == it.value)
                    Option(value = it.value)
                else
                    Option(it.key, it.value)
            }
        else if (remoteLoader != null)
            return remoteLoader.load(source.url).map {
                if (it.key == it.value)
                    Option(value = it.value)
                else
                    Option(it.key, it.value)
            }
        else
            throw RuntimeException("${source.remote}'s Source Loader is empty")
    }
}