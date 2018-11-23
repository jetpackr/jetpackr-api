package com.jetpackr.source.loader.remote

import org.apache.maven.artifact.versioning.DefaultArtifactVersion

val VersionComparator = Comparator<Pair<String, String>> { pair1: Pair<String, String>, pair2: Pair<String, String> ->
        DefaultArtifactVersion(
                pair2.first.replaceFirst("v","")
                                .replace("_", ".")
        ).compareTo(DefaultArtifactVersion(
                pair1.first.replaceFirst("v","")
                                .replace("_", ".")
        ))
}