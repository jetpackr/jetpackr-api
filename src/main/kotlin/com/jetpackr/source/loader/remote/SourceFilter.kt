package com.jetpackr.source.loader.remote

val MATCHES = Regex("^v?\\d+([._]\\d+)*([._-]\\p{Alnum}+)*")
val CONTAINS = Regex("(windowsservercore|nanoserver|deprecated)(\\p{Alnum})*")

val SourceFilter: (String) -> Boolean = {
   it matches MATCHES && !it.contains(regex = CONTAINS)
}