package com.jetpackr.source.loader.local

import java.util.TimeZone

class TimeZoneLoader: LocalSourceLoader() {
    override suspend fun load(): List<String> {
        return TimeZone.getAvailableIDs().mapNotNull { it ->
            if (!it.startsWith("SystemV"))
                it
            else
                null
        }
    }
}