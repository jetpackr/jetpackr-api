package com.jetpackr.data.parameter

import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

class Select(
        name: String,
        label: String,
        full: Boolean = false,
        val options: List<Option>
): Parameter(
        name,
        label,
        full
) {
    override fun toString(): String {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE)
    }
}