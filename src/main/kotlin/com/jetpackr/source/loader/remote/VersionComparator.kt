package com.jetpackr.source.loader.remote

import org.apache.maven.artifact.versioning.DefaultArtifactVersion

val VersionComparator = Comparator<String> { version1: String, version2: String ->
        DefaultArtifactVersion(
                version2.replaceFirst("v","")
                        .replace("_", ".")
        ).compareTo(DefaultArtifactVersion(
                version1.replaceFirst("v","")
                        .replace("_", ".")
        ))
}