package com.jetpackr.source.loader

abstract class SourceLoader {
    abstract suspend fun load(): List<String>
    abstract suspend fun load(url: String): List<String>
}