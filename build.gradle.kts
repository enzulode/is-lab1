plugins {
    id("is-lab1.java-conventions")
    alias(libs.plugins.springBootGradlePlugin)
    alias(libs.plugins.springBootDependencyManagementGradlePlugin)
}

group = "com.enzulode"
version = "1.0.0-SNAPSHOT"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    testLogging.events("PASSED", "SKIPPED", "FAILED")
}
