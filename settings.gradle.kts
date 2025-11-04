rootProject.name = "zTextGenerator"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        maven {
            name = "groupezReleases"
            url = uri("https://repo.groupez.dev/releases")
        }
        gradlePluginPortal()
    }
}

include("API")
include("Hooks:ItemsAdder")
include("Hooks:Nexo")
include("Hooks:Oraxen")
include("Hooks:ZMenu")