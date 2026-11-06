plugins {
	id("net.fabricmc.fabric-loom-remap") version "1.18-SNAPSHOT"
}

version = project.property("mod_version") as String
group = project.property("maven_group") as String

base {
	archivesName = project.property("archives_base_name") as String
}

loom {
	splitEnvironmentSourceSets()
	mods {
		create("flposn") {
			sourceSet(sourceSets.main.get())
			sourceSet(sourceSets.named("client").get())
		}
	}
}

dependencies {
	minecraft("com.mojang:minecraft:${project.property("mc_version")}")
	mappings("net.fabricmc:yarn:${project.property("yarn_mappings")}:v2")
	modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")
	modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_api_version")}")
}

tasks.withType<JavaCompile>().configureEach {
	options.release = 17
}

java {
	withSourcesJar()
	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
}

tasks.jar {
	from(rootProject.file("LICENSE")) {
		into("META-INF")
	}
}

tasks.named<Jar>("sourcesJar") {
	from(rootProject.file("LICENSE")) {
		into("META-INF")
	}
}
