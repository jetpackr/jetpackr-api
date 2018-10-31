package com.jetpackr.configuration.machine

import com.jetpackr.configuration.Option
import com.jetpackr.configuration.Parameter
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import java.util.TimeZone.getAvailableIDs

class Timezone(
        name: String,
        label: String
): Parameter(
        name,
        label,
        options = getAvailableIDs().mapNotNull { it ->
            if (!it.startsWith("SystemV"))
                Option(value = it)
            else
                null
        }
) {
    override fun toString(): String {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE)
    }
}