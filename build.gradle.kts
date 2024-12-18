plugins {
    id("java")
    id("application")
}

group = "art.ameliah.hsr"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:24.0.0")
    implementation("com.google.code.gson:gson:2.11.0")

    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

application {
    applicationDefaultJvmArgs = listOf("-Dfile.encoding=UTF-8")
    mainClass.set("art.ameliah.hsr.Main")
}

tasks.test {
    useJUnitPlatform()
}