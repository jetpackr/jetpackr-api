package com.jetpackr.source.loader.local

import com.jetpackr.source.loader.SourceLoader

abstract class LocalSourceLoader: SourceLoader() {
    final override suspend fun load(url: String): List<String> = TODO("Not implemented")
}