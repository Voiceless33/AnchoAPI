# 🚀 AnchoAPI

**AnchoAPI** is a lightweight and flexible developer API built to make Minecraft plugin creation faster, cleaner, and more modular.  
It provides a powerful command system, advanced item tools, an item-leveling framework, storage utilities, and a set of helpful abstractions you can drop into any project.

---

## 📦 Installation

### Maven
```xml
<repositories>
    <repository>
        <id>ancho-releases</id>
        <url>https://maven.codewithancho.com/repository/maven-releases/</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.codewithancho.api</groupId>
    <artifactId>ancho-api</artifactId>
    <version>1.0.0</version>
</dependency>
```
### Gradle
```
repositories {
    mavenCentral()
    maven { url "https://maven.codewithancho.com/repository/maven-releases/" }
}

dependencies {
    implementation "com.codewithancho.api:ancho-api:1.0.0"
}
```