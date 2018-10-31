package com.jetpackr.configuration

import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

open class Parameter(
        name: String,
        label: String,
        val value: String? = null,
        val options: List<Option>? = null
): Metadata(
        name = name,
        label = label
) {
    override fun toString(): String {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE)
    }
}