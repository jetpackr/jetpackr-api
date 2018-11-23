package com.jetpackr.source.loader.local

abstract class LocalLoader {
    abstract suspend fun load(): List<Pair<String, String>>
}