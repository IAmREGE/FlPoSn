plugins {
	id("net.fabricmc.fabric-loom-remap") version "1.18-SNAPSHOT"
	id("xyz.wagyourtail.jvmdowngrader") version "1.3.6"
}

version = project.property("mod_version") as String
group = project.property("maven_group") as String

base {
	archivesName = project.property("archives_base_name") as String
}

repositories {
	maven("https://repo.codemc.io/repository/relativitymc/")
}

jvmdg {
	downgradeTo = JavaVersion.VERSION_16
}

loom {
	useIntermediateMappings = true
	intermediaryUrl = $$"https://repo.codemc.io/repository/relativitymc/org/relativitymc/intermediary/%1$s/intermediary-%1$s-v2.jar"
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
	mappings("org.relativitymc:modern-yarn:${project.property("yarn_mappings")}:v2")
	modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")
	modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_api_version")}")
	compileOnly(files("fakeneo.jar"))
}

tasks.withType<JavaCompile>().configureEach {
	options.release = 25
}

java {
	withSourcesJar()
	sourceCompatibility = JavaVersion.VERSION_25
	targetCompatibility = JavaVersion.VERSION_25
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

tasks.downgradeJar {
	filesMatching("flposn.mixins.json") {
		filter {
			it.replace("JAVA_25", "JAVA_16")
		}
	}
	filesMatching("flposn.client.mixins.json") {
		filter {
			it.replace("JAVA_25", "JAVA_16")
		}
	}
}
