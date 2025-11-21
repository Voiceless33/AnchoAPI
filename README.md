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
  * [AdvancedItem](#advanceditem)

---

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

> This is how you register your command manager and let it load all your command from a given package.
##### CommandManager Usage
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

> This is how your command needs to look like in order for the CommandManager to load it.
##### ExampleCommand.java
```java
@CommandName("example")
@CommandDescription("A simple example command")
@CommandUsage("/example")
@CommandPermission("anchoapi.example")
@CommandAliases({"ex", "example-command"})
public class ExampleCommand implements CommandExecutor {

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

## AdvancedItem

> Creating custom **AdvancedItems**

```java
ItemStack diamondSword = new ItemStack(Material.DIAMOND_SWORD);

// Wrapping the item
AdvancedItem advanced = new AdvancedItem(diamondSword);

// Add your API identifier
advanced.addNamespacedData(
        advanced.getAPI_ITEM_IDENTIFIER(),
        PersistentDataType.INTEGER,
        1
        );

// Make it soulbound to the player by passing his UUID
advanced.addNamespacedData(
            advanced.getSOULBOUND_ITEM_IDENTIFIER(),
            PersistentDataType.STRING,
            uuid.toString()
);

// If you want the item to glow or not, this can be toggled on and off with true/false
advanced.setGlow(true);

// Use the leveling system
AdvancedItemLeveling leveling = advanced.getLeveling();

leveling.initLeveling(1,0,100,10);
// level = 1, xp = 0, xpPerLevel = 100, maxLevel = 10

leveling.addExperience(120);
// adds XP → auto levels up inside AdvancedItemLeveling
```

F