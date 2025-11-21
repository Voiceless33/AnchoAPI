![AnchoAPI Logo](images/logo.png)

---
## 📖 Table of Contents

* [Why](#why)
* [Installation and Setup](#installation-and-setup)
  * [pom.xml](#pomxml) 
  * [build.gradle](#buildgradle)
* [Usage](#usage)
  * [CommandManager](#commandmanager-usage)
  * [ExampleCommand](#examplecommandjava)

## Why
**AnchoAPI** is a lightweight, flexible developer toolkit designed to help you build cleaner and more modular applications.
Whether you're working on Minecraft plugins or general Java projects, **AnchoAPI** gives you a set of powerful utilities that streamline your workflow.

It includes an annotation-driven command system, advanced item and data wrappers, a customizable leveling framework, storage helpers, and a collection of abstractions that make your codebase easier to structure and maintain.

**AnchoAPI** focuses on simplicity, speed, and scalability, so you can focus on creating great features instead of rewriting the same boilerplate again and again.

---

## Installation and Setup

### pom.xml
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

### build.gradle
```groovy
repositories {
    // Other repos
    maven {
        name = 'ancho-releases'
        url = uri('https://maven.codewithancho.com/repository/maven-releases/')
    }
}

dependencies {
    // Other dependencies
    implementation 'com.codewithancho.api:ancho-api:1.0.0'
}
```

---

## Usage

### CommandManager Usage
```java
import com.codewithancho.api.spigot.managers.CommandManager;
import com.codewithancho.api.spigot.managers.CommandMapExposer;
import org.bukkit.Bukkit;

// Pass the main plugin class from your plugin instead of 'this' if you're running it somewhere else.
CommandManager commandManager = new CommandManager(this, CommandMapExposer.getCommandMap(this));

@Override
public void onEnable() {
    commandManager.loadCommands("path.to.commands.package");
}
```

##### ExampleCommand.java
```java
package com.codewithancho.example.commands;

import com.codewithancho.api.spigot.annotations.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@CommandName("example")
@CommandDescription("A simple example command")
@CommandUsage("/example")
@CommandPermission("anchoapi.example")
@CommandAliases({"ex", "example-command"})
public class TestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {

        sender.sendMessage("This is just an example command");
        return true;
    }
}

```
