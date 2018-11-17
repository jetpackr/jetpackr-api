package com.jetpackr.source.loader.local

abstract class LocalLoader {
    abstract suspend fun load(): Map<String, String>
}