dependencies {
    implementation(project(":common"))
    compileOnly("io.papermc.paper:paper-api:${property("paperApiVersion")}")
}

tasks.jar {
    from(project(":common").sourceSets.main.get().output)
    archiveFileName.set("LazyTeleport-${rootProject.version}-Folia.jar")
    destinationDirectory.set(rootProject.layout.buildDirectory)
}
