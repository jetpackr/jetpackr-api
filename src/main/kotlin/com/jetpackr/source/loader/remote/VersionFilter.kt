package com.jetpackr.source.loader.remote

private val MATCHES = Regex("^v?\\d+([._]\\d+)*([._-]\\p{Alnum}+)*")
private val CONTAINS = Regex("(windowsservercore|nanoserver|deprecated)(\\p{Alnum})*")

val VersionFilter: (Pair<String, String>) -> Boolean = {
   it.first matches MATCHES && !it.first.contains(regex = CONTAINS)
}