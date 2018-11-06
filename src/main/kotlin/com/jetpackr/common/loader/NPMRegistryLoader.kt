package com.jetpackr.common.loader

import com.jetpackr.common.comparator.VersionComparator
import com.jetpackr.common.data.parameter.Option
import com.jetpackr.common.data.response.NPMRegistryResponse
import io.ktor.client.request.get

val NPMRegistryLoader: SourceLoader = { url, client ->
    if (url == null)
        throw Exception("Url cannot be empty")

    client.get<NPMRegistryResponse>(url).time.mapNotNull{
        if (setOf("modified", "created").contains(it.key))
            null
        else
            it.key
    }.sortedWith(VersionComparator).map {
        Option(value = it)
    }
}