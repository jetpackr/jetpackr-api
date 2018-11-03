package com.jetpackr.configuration.container

import com.jetpackr.configuration.Platform
import com.jetpackr.configuration.parameter.Checkbox
import com.jetpackr.configuration.parameter.Select
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
        val port: List<Port>? = null,
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