pluginManagement {
	repositories {
		maven("https://repository.hanbings.io/proxy") {
			name = "Fabric"
		}
		mavenCentral()
		gradlePluginPortal()
	}
}

include("1_20_4f")
include("26xf")
//include("1_20_4m")
//include("26xm")
