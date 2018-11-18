package com.jetpackr.source.loader.local

import net.time4j.tz.Timezone
import java.time.LocalDateTime
import java.time.ZoneId

class TimezoneLoader: LocalLoader() {
    private val MATCHES = Regex("^\\p{Alpha}+/\\p{Alpha}+(_\\p{Alpha}+)*")

    private fun getZoneIds(): List<String> {
        return Timezone.getAvailableIDs().map {
            it.canonical()
        }
    }

    override suspend fun load(): Map<String, String> {
        val versions = mutableMapOf<String, String>()

        getZoneIds().forEach {
            if (it.length > 1 && it matches MATCHES
                    && !it.startsWith("WINDOWS")
                    && !it.startsWith("MILITARY")
                    && !it.startsWith("Etc")
            ) {
                try {
                    val offset = LocalDateTime.now().atZone(ZoneId.of(it)).offset.toString()
                    versions += "(UTC${if (offset == "Z") "+00:00" else offset}) $it" to it
                } catch (e: Exception) {
                }
            }
        }

        return versions.toList().sortedBy { (_, value)  -> value }.toMap()
    }
}