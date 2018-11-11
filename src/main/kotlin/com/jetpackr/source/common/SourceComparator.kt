package com.jetpackr.source.common

import org.apache.maven.artifact.versioning.DefaultArtifactVersion

val SourceComparator = Comparator<String> { version1: String, version2: String ->
        DefaultArtifactVersion(
                version2.replaceFirst("v","")
                        .replace("_", ".")
        ).compareTo(DefaultArtifactVersion(
                version1.replaceFirst("v","")
                        .replace("_", ".")
        ))
}