package com.jetpackr.source.loader

import com.jetpackr.source.comparator.VersionComparator
import com.jetpackr.source.response.NPMRegistryResponse
import com.jetpackr.source.filter.VersionFilter
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class NPMRegistryLoader(client: HttpClient): SourceLoader(client) {
    override suspend fun load(url: String): List<String> {
        return client.get<NPMRegistryResponse>(url).time
                .mapNotNull {
                    if (setOf("modified", "created").contains(it.key))
                        null
                    else
                        it.key
                }.filter(VersionFilter)
                .sortedWith(VersionComparator)
    }
}