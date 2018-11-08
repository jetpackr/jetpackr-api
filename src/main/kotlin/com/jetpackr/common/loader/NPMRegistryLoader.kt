package com.jetpackr.common.loader

import com.jetpackr.common.comparator.VersionComparator
import com.jetpackr.common.data.response.NPMRegistryResponse
import com.jetpackr.common.filter.VersionFilter
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