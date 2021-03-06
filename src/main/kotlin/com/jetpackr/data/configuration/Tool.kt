package com.jetpackr.data.configuration

import com.jetpackr.data.parameter.Checkbox
import com.jetpackr.data.parameter.Select
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

class Tool(
        name: String,
        label: String,
        description: String,
        alias: String? = null,
        version: Select,
        install: Checkbox? = null,
        val dependency: Tool? = null,
        val extensions: List<Tool>? = null
): Platform(
        name,
        label,
        description,
        alias,
        version,
        install
) {
    override fun toString(): String {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE)
    }
}