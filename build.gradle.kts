plugins {
    application

    // Fabric Loom
    id("fabric-loom") version "0.9-SNAPSHOT"

    // Java Virtual Machine
    kotlin("jvm") version "1.5.21"

    // KotlinX Serialization
    id("org.jetbrains.kotlin.plugin.serialization") version "1.5.21"

    // Maven Publish
    id("maven-publish")
}

group = "dev.vini2003"
version = "3.3.0"

minecraft {
    accessWidener = file("src/main/resources/blade.accesswidener")
}

repositories {
    // Maven Central
    mavenCentral()

    // vini2003.dev
    maven {
        name = "vini2003.dev"

        setUrl("https://maven.pkg.github.com/vini2003/Maven")

        credentials {
			username = System.getenv("GPR_USERNAME")
			password = System.getenv("GPR_TOKEN")
        }
    }

    // Fabric
    maven("https://maven.fabricmc.net/")
	
	// TerraformersMC
	maven("https://maven.terraformersmc.com/releases")
}

dependencies {
    // Minecraft
    minecraft(
        group = "com.mojang",
        name = "minecraft",
        version = "1.17.1"
    )

    // Yarn
    mappings(
        group = "net.fabricmc",
        name = "yarn",
        version = "1.17.1+build.32",
        classifier = "v2"
    )

    // Fabric Loader
    modImplementation(
        group = "net.fabricmc",
        name = "fabric-loader",
        version = "0.11.6"
    )

    // Fabric API
    modImplementation(
		group = "net.fabricmc.fabric-api",
		name = "fabric-api",
		version = "0.39.2+1.17"
    )

    // Fabric Language Kotlin
    modImplementation(
		group = "net.fabricmc",
		name = "fabric-language-kotlin",
		version = "1.6.3+kotlin.1.5.21"
    )
	
	// ModMenu
	modImplementation(
		group = "com.terraformersmc",
		name = "modmenu",
		version = "2.0.10"
	)

    // KotlinX Serialization Json
    implementation(
		group = "org.jetbrains.kotlinx",
		name = "kotlinx-serialization-json",
		version = "1.2.2"
    )

    // KotlinX Coroutines
    implementation(
		group = "org.jetbrains.kotlinx",
		name = "kotlinx-coroutines-jdk8",
		version = "1.5.1"
    )

    // KotlinX DateTime
    implementation(
		group = "org.jetbrains.kotlinx",
		name = "kotlinx-datetime",
		version = "0.2.1"
    )
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

tasks.getByName<ProcessResources>("processResources") {
    filesMatching("fabric.mod.json") {
        expand(
		    mutableMapOf(
		        "id" to "blade",
		        "name" to "Blade",
		        "version" to "3.3.0",
		        "description" to "A modern, flexible GUI library.",
		        "minecraftVersion" to "1.17.1",
		        "kotlinVersion" to "1.15.21",
		        "fabricLoaderVersion" to "0.11.6",
		        "fabricApiVersion" to "0.39.2+1.17",
			    "fabricLanguageKotlinVersion" to "1.6.3+kotlin.1.5.21"
		    )
        )
    }
}

publishing {
    publications {
        create<MavenPublication>(name) {
		    this.groupId = group as String
		    this.artifactId = name
		    this.version = version
    
		    from(components["java"])
    
		    val sourcesJar by tasks.creating(Jar::class) {
		        val sourceSets: SourceSetContainer by project
    
		        from(sourceSets["main"].allJava)
		        classifier = "sources"
		    }
		    val javadocJar by tasks.creating(Jar::class) {
		        from(tasks.get("javadoc"))
		        classifier = "javadoc"
		    }
    
		    artifact(sourcesJar)
		    artifact(javadocJar)
        }
    }

    repositories {
        repositories {
		    maven {
		        name = "GitHubPackages"
    
		        setUrl("https://maven.pkg.github.com/vini2003/Maven")
    
		        credentials {
		            username = System.getenv("GPR_USERNAME")
		            password = System.getenv("GPR_TOKEN")
		        }
		    }
        }
    }
}