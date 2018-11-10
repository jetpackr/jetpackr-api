package com.jetpackr.source.filter

val VersionFilter: (String) -> Boolean = {
    Regex("^v?[0-9]+([._][0-9]+)*(-(alpine))?") matches it
}