package com.jetpackr.data.parameter

import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

class Text(
        name: String,
        label: String,
        span: Boolean = false,
        val value: String
): Parameter(
        name,
        label,
        span
) {
    override fun toString(): String {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE)
    }
}