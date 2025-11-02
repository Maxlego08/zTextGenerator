group = "Hooks:ItemsAdder"

repositories {
    maven(url = "https://maven.devs.beer/")
}

dependencies {

    compileOnly(projects.api)

    compileOnly("dev.lone:api-itemsadder:4.0.2-beta-release-11")
    compileOnly(files("libs/LoneLibs_1.0.65.jar"))
}