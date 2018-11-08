package com.jetpackr.common.loader

import io.ktor.client.HttpClient
import java.util.TimeZone

class TimeZoneLoader(client: HttpClient): SourceLoader(client) {
    override suspend fun load(url: String): List<String> {
        return TimeZone.getAvailableIDs().mapNotNull { it ->
            if (!it.startsWith("SystemV"))
                it
            else
                null
        }
    }
}