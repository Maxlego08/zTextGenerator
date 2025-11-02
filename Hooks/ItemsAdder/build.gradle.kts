group = "Hooks:ItemsAdder"

repositories {
    maven(url = "https://maven.devs.beer/")
}

dependencies {

    compileOnly(projects.api)
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    compileOnly("dev.lone:api-itemsadder:4.0.2-beta-release-11")
    compileOnly(files("libs/LoneLibs_1.0.65.jar"))
}