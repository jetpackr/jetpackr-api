package com.jetpackr.common.loader

import com.jetpackr.common.comparator.VersionComparator
import com.jetpackr.common.data.parameter.Option
import com.jetpackr.common.data.response.NPMRegistryResponse
import com.jetpackr.common.filter.VersionFilter
import io.ktor.client.request.get

val NPMRegistryLoader: SourceLoader = { client, url ->
    client.get<NPMRegistryResponse>(url).time.mapNotNull{
        if (setOf("modified", "created").contains(it.key))
            null
        else
            it.key
    }.filter(VersionFilter).sortedWith(VersionComparator).map {
        Option(value = it)
    }
}