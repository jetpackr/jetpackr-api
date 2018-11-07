package com.jetpackr.common.loader

import com.jetpackr.common.data.parameter.Option
import io.ktor.client.HttpClient

typealias SourceLoader = suspend (HttpClient, String) -> List<Option>