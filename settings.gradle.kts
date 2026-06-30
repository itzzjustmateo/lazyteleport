plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

rootProject.name = "LazyTeleport"

include("common", "paper", "folia")
