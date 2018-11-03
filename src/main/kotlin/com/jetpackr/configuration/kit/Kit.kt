package com.jetpackr.configuration.kit

import com.jetpackr.configuration.Platform
import com.jetpackr.configuration.parameter.Checkbox
import com.jetpackr.configuration.parameter.Select
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

class Kit(
        name: String,
        label: String,
        description: String,
        alias: String? = null,
        version: Select,
        install: Checkbox,
        val dependency: Kit? = null,
        val extensions: List<Kit>? = null
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