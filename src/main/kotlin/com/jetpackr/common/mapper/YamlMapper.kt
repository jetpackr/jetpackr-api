package com.jetpackr.common.mapper

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

val YamlMapper: (block: SimpleModule.() -> Unit) -> ObjectMapper = {
    val objectMapper = ObjectMapper(YAMLFactory())
    val deserializerModule: SimpleModule = SimpleModule().apply(it)

    objectMapper.registerKotlinModule()
    objectMapper.registerModule(deserializerModule)

    objectMapper
}