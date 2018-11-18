package com.jetpackr.source.loader.remote

private val MATCHES = Regex("^v?\\d+([._]\\d+)*([._-]\\p{Alnum}+)*")
private val CONTAINS = Regex("(windowsservercore|nanoserver|deprecated)(\\p{Alnum})*")

val VersionFilter: (String) -> Boolean = {
   it matches MATCHES && !it.contains(regex = CONTAINS)
}