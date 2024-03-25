plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    runtimeOnly ("org.postgresql:postgresql")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.testcontainers:junit-jupiter:1.19.7")
    testImplementation ("org.testcontainers:postgresql:1.16.0")
    implementation ("org.postgresql:postgresql:42.2.24")
    testImplementation ("org.mockito:mockito-core:3.12.4")
    implementation ("javax.servlet:javax.servlet-api:4.0.1")
    implementation ("com.google.code.gson:gson:2.8.8")


}

tasks.test {
    useJUnitPlatform()
}