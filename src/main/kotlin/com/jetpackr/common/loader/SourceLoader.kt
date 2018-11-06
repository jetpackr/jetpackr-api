package com.jetpackr.common.loader

import com.jetpackr.common.data.parameter.Option
import io.ktor.client.HttpClient

typealias SourceLoader = suspend (String?, HttpClient) -> List<Option>