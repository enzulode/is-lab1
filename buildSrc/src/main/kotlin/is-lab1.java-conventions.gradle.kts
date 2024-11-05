plugins {
    java
    id("com.diffplug.spotless")
    pmd
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

spotless {
    java {
        importOrder()
        removeUnusedImports()
        cleanthat()
        googleJavaFormat()
            .reflowLongStrings(false)
            .reorderImports(true)
            .skipJavadocFormatting()
        formatAnnotations()
        toggleOffOn("formatter:off", "formatter:on")
    }
}

pmd {
    toolVersion = "7.6.0"
    isConsoleOutput = true
}

tasks.withType<Pmd>().configureEach {
    reports {
        xml.required = false
        html.required = true
    }
}
