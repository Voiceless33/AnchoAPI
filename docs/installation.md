# Installation

AnchoAPI is distributed through a Maven/Nexus repository.

---

## Maven

Add the repository:
```xml
<repositories>
    <repository>
        <id>ancho-releases</id>
        <url>https://maven.codewithancho.com/repository/maven-releases/</url>
    </repository>
</repositories>
```

## Gradle
```
repositories {
    mavenCentral()
    maven { url "https://maven.codewithancho.com/repository/maven-releases/" }
}

dependencies {
    implementation "com.codewithancho.api:ancho-api:1.0.0"
}
```

