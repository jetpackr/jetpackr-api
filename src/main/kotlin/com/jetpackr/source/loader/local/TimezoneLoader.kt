package com.jetpackr.source.loader.local

import net.time4j.tz.Timezone
import java.time.LocalDateTime
import java.time.ZoneId

class TimezoneLoader: LocalLoader() {
    val MATCHES = Regex("^\\p{Alpha}+/\\p{Alpha}+(_\\p{Alpha}+)*")

    override suspend fun load(): Map<String, String> {
        val versions = mutableMapOf<String, String>()

        Timezone.getAvailableIDs().forEach {
            val canonical = it.canonical()

            if (canonical.length > 1 && canonical matches MATCHES
                    && !canonical.startsWith("WINDOWS")
                    && !canonical.startsWith("MILITARY")
                    && !canonical.startsWith("Etc")
            ) {
                try {
                    val offset = LocalDateTime.now().atZone(ZoneId.of(canonical)).offset.toString()
                     versions += "$canonical (UTC${if (offset == "Z") "+00:00" else offset})" to canonical
                } catch (e: Exception) {}
            }
        }

        return versions.toSortedMap()
    }
}