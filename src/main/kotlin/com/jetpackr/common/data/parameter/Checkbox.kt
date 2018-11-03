package com.jetpackr.common.data.parameter

import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

class Checkbox(
        name: String,
        label: String,
        val flag: Boolean? = false
): Parameter(
        name,
        label
) {
    override fun toString(): String {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE)
    }
}