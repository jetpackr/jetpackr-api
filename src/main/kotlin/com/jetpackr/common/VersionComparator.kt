package com.jetpackr.common

import org.apache.maven.artifact.versioning.DefaultArtifactVersion

val VersionComparator = { version1: String, version2: String ->
        DefaultArtifactVersion(version2).compareTo(DefaultArtifactVersion(version1))
}