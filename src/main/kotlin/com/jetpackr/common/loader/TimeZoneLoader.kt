package com.jetpackr.common.loader

import com.jetpackr.common.data.parameter.Option
import java.util.TimeZone

val TimeZoneLoader: SourceLoader = { _, _ ->
    TimeZone.getAvailableIDs().mapNotNull { it ->
        if (!it.startsWith("SystemV"))
            Option(value = it)
        else
            null
    }
}