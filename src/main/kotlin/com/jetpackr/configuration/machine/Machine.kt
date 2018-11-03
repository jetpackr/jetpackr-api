package com.jetpackr.configuration.machine

import com.jetpackr.common.data.Metadata
import com.jetpackr.common.data.parameter.Select
import com.jetpackr.common.data.parameter.Text
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

class Machine(
        name: String,
        label: String,
        description: String,
        val box: Select,
        val memory: Text,
        val synchronization: Select,
        val timezone: Select
): Metadata(
        name,
        label,
        description
) {
    override fun toString(): String {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE)
    }
}