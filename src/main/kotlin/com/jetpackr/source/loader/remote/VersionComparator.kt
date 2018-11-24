package com.jetpackr.source.loader.remote

import org.apache.maven.artifact.versioning.DefaultArtifactVersion

val VersionComparator = Comparator<Pair<String, String>> { pair1: Pair<String, String>, pair2: Pair<String, String> ->
        DefaultArtifactVersion(
                pair2.second.replaceFirst("v","")
                                .replace("_", ".")
        ).compareTo(DefaultArtifactVersion(
                pair1.second.replaceFirst("v","")
                                .replace("_", ".")
        ))
}