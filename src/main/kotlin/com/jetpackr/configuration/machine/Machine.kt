package com.jetpackr.configuration.machine

import com.jetpackr.configuration.Metadata
import com.jetpackr.configuration.Parameter
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

class Machine(
        name: String,
        label: String,
        description: String,
        val box: Parameter,
        val memory: Parameter,
        val synchorization: Parameter,
        val timezone: Timezone
): Metadata(
        name = name,
        label = label,
        description = description
) {
    override fun toString(): String {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE)
    }
}