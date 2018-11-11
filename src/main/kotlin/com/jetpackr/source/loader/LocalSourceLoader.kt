package com.jetpackr.source.loader

abstract class LocalSourceLoader {
    abstract suspend fun load(): List<String>
}