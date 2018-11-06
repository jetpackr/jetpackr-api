package com.jetpackr.configuration.kit

import com.jetpackr.common.data.parameter.Checkbox
import com.jetpackr.common.data.parameter.Select
import com.jetpackr.configuration.Platform
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

class Kit(
        name: String,
        label: String,
        description: String,
        alias: String? = null,
        version: Select,
        install: Checkbox? = null,
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