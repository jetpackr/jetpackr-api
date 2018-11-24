package com.jetpackr.source.loader.remote

private val MATCHES = Regex("^v?\\d+([._]\\d+)*([._-]\\p{Alnum}+)*")
private val CONTAINS = Regex("(windowsservercore|nanoserver|deprecated|beta|alpha|preview|next)(\\p{Alnum})*")

val VersionFilter: (Pair<String, String>) -> Boolean = {
   it.second matches MATCHES && !it.second.contains(regex = CONTAINS)
}