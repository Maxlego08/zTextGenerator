group = "Hooks:ZMenu"

repositories {
    maven {
        name = "groupezReleases"
        url = uri("https://repo.groupez.dev/releases")
    }
}

dependencies {

    compileOnly(projects.api)
    compileOnly("fr.maxlego08.menu:zmenu-api:1.1.1.4")
}