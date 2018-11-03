package com.jetpackr.configuration

import com.jetpackr.configuration.parameter.Checkbox
import com.jetpackr.configuration.parameter.Select
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

open class Platform(
  name: String,
  label: String,
  description: String,
  alias: String? = null,
  val version: Select,
  val install: Checkbox
): Metadata(
        name,
        label,
        description,
        alias
) {
    override fun toString(): String {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE)
    }
}