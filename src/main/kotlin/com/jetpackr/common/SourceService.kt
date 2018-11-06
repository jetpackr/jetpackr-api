package com.jetpackr.common

import com.jetpackr.common.data.parameter.Option
import com.jetpackr.common.data.source.Source
import kotlinx.coroutines.runBlocking
import java.util.TimeZone

typealias SourceLoader = (Source) -> List<Option>

class SourceService(private val sourceLoaders: Map<Source.Type, SourceLoader>) {
    init {
        if (sourceLoaders == null)
            throw RuntimeException("Source Loaders are empty")
    }

    fun loadFromSource(source: Source): List<Option> = runBlocking {
        val sourceLoader: SourceLoader? = sourceLoaders[source.type]

        if (sourceLoader != null)
            sourceLoader.invoke(source)
        else
            throw RuntimeException("${source.type}'s Source Loader is empty")
    }
}

val loadFromTimeZone: SourceLoader = {
    TimeZone.getAvailableIDs().mapNotNull { it ->
        if (!it.startsWith("SystemV"))
            Option(value = it)
        else
            null
    }
}

val loadFromDockerHub: SourceLoader = {
    listOf()
}

val loadFromGitHub: SourceLoader = {
    listOf()
}

val loadFromNPMRegistry: SourceLoader = {
    listOf()
}

val loadFromSDKMAN: SourceLoader = {
    listOf()
}

val sourceService = SourceService(
        mapOf(
                Source.Type.DockerHub to loadFromDockerHub,
                Source.Type.GitHub to loadFromGitHub,
                Source.Type.NPMRegistry to loadFromNPMRegistry,
                Source.Type.SDKMAN to loadFromSDKMAN,
                Source.Type.TimeZone to loadFromTimeZone
        )
)
