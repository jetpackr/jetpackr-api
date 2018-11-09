package com.jetpackr.data.configuration.container

import com.jetpackr.data.parameter.Checkbox
import com.jetpackr.data.parameter.Select
import com.jetpackr.data.configuration.Platform
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

class Container(
        name: String,
        label: String,
        description: String,
        version: Select,
        install: Checkbox,

        val type: Type,
        val command: String? = null,
        val volumes: Map<String, String>? = null,
        val ports: List<Port>? = null,
        val environment: Environment? = null
): Platform(
        name,
        label,
        description,
        null,
        version,
        install
) {
    enum class Type {
        DataStore,
        MessageBroker,
        SearchEngine
    }

    override fun toString(): String {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE)
    }
}