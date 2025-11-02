group = "Hooks:Nexo"

repositories {
    maven (url = "https://repo.nexomc.com/releases")
}

dependencies {
    compileOnly(projects.api)
    compileOnly("com.nexomc:nexo:1.1.0")
}