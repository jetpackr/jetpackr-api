package com.jetpackr.source.loader.local

abstract class LocalSourceLoader {
    abstract suspend fun load(): List<String>
}