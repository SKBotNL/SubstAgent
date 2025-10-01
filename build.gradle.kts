import java.io.BufferedReader

plugins {
    id("com.gradleup.shadow") version "8.3.6"
    id("com.diffplug.spotless") version "7.0.4"
    id("java")
}

val commitHash =
    Runtime.getRuntime().exec(arrayOf("git", "rev-parse", "--short=10", "HEAD")).let { process ->
        process.waitFor()
        val output = process.inputStream.use { it.bufferedReader().use(BufferedReader::readText) }
        process.destroy()
        output.trim()
    }

group = "nl.skbotnl"

version = commitHash

repositories { mavenCentral() }

dependencies {
    implementation("org.javassist:javassist:3.30.2-GA")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test { useJUnitPlatform() }

tasks.build {
    dependsOn(tasks.spotlessApply)
    dependsOn(tasks.shadowJar)
}

tasks.shadowJar {
    archiveClassifier.set("")
    minimize()
}

tasks.jar {
    manifest { attributes("Premain-Class" to "nl.skbotnl.substagent.SubstAgent", "Implementation-Version" to version) }
    archiveClassifier.set("part")
}

spotless {
    java {
        removeUnusedImports()
        palantirJavaFormat()
    }
    kotlinGradle {
        ktfmt().kotlinlangStyle().configure { it.setMaxWidth(120) }
        target("build.gradle.kts", "settings.gradle.kts")
    }
}
