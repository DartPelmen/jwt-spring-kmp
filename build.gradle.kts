import org.gradle.api.internal.artifacts.dependencies.DefaultExternalModuleDependency
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

val kotlinVersion = "1.9.0"
val serializationVersion = "1.5.1"
val kotlinWrappersVersion = "1.0.0-pre.561"
val ktorVersion = "2.3.0"

plugins {
    kotlin("multiplatform") version "1.9.0"
    application //to run JVM part
    kotlin("plugin.serialization") version "1.9.0"
    id("org.springframework.boot") version "3.1.4"
    id("io.spring.dependency-management") version "1.1.3"
    kotlin("plugin.spring") version "1.8.22"
    kotlin("plugin.jpa") version "1.8.22"
    kotlin("kapt") version "1.9.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
    maven { url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers") }
}

kotlin {
    jvm {
        jvmToolchain(20)
        withJava()
        apply(plugin = "org.springframework.boot")
        apply(plugin = "io.spring.dependency-management")
        apply(plugin = "org.jetbrains.kotlin.plugin.spring")
        apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
        tasks.withType<Test> {
            useJUnitPlatform()
        }
    }
    js(IR) {
        binaries.executable()
        browser {
            commonWebpackConfig{
                cssSupport{
                    enabled = true
                }
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-html:0.7.3")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.2.1")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmMain by getting {
            dependencies {
// https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-api
                implementation("io.jsonwebtoken:jjwt-api:0.12.3")
// https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-impl
                runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.3")
// https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-jackson
                runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.3")
// https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-root
                implementation("io.jsonwebtoken:jjwt-root:0.12.3")

                implementation("org.springframework.boot:spring-boot-starter-data-jpa")
                implementation("org.springframework.boot:spring-boot-starter-data-rest")
                implementation("org.springframework.boot:spring-boot-starter-security")
                implementation("org.springframework.boot:spring-boot-starter-web")
                implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
                implementation("org.jetbrains.kotlin:kotlin-reflect")
                implementation("org.springframework.boot:spring-boot-devtools")
                runtimeOnly("org.postgresql:postgresql")
                configurations["kapt"].dependencies.add(DefaultExternalModuleDependency("org.springframework.boot","spring-boot-configuration-processor","3.1.4"))

            }
        }

        val jsMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-js:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation(project.dependencies.enforcedPlatform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:$kotlinWrappersVersion"))
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom")
            }
        }
    }
}

application {
    mainClass.set("org.example.ServerKt")
}

// include JS artifacts in any JAR we generate
tasks.named<Jar>("jvmJar").configure {
    val taskName = if (project.hasProperty("isProduction")
        || project.gradle.startParameter.taskNames.contains("installDist")
    ) {
        "jsBrowserProductionWebpack"
    } else {
        "jsBrowserDevelopmentWebpack"
    }
    val webpackTask = tasks.named<KotlinWebpack>(taskName)
    from(webpackTask.map {
        File(it.destinationDirectory, it.outputFileName)
    }) // bring output file along into the JAR
    into("static")
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "20"
        }
    }
}

distributions {
    main {
        contents {
            from("$buildDir/libs") {
                rename("${rootProject.name}-jvm", rootProject.name)
                into("lib")
            }
        }
    }
}

// Alias "installDist" as "stage" (for cloud providers)
tasks.register("stage") {
    dependsOn(tasks.named("installDist"))
}

tasks.named<JavaExec>("run").configure {
    classpath(tasks.named<Jar>("jvmJar")) // so that the JS artifacts generated by `jvmJar` can be found and served
}
