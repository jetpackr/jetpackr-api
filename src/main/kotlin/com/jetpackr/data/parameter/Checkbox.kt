package com.jetpackr.data.parameter

import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

class Checkbox(
        name: String,
        label: String,
        span: Boolean = false,
        val checked: Boolean = false
): Parameter(
        name,
        label,
        span
) {
    override fun toString(): String {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE)
    }
}