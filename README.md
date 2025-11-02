# SWCCore Addon System

A powerful and flexible addon/plugin system for Spigot servers built with Kotlin. This system allows developers to create modular extensions for your main plugin without modifying the core code.

## 🌟 Features

- ✅ **Dynamic Addon Loading** - Load addons from JAR files at runtime
- ✅ **YAML-based Configuration** - Use familiar `manifest.yml` instead of complex MANIFEST.MF
- ✅ **Lifecycle Management** - Full control with onLoad, onEnable, onDisable, and onReload hooks
- ✅ **Dependency System** - Addons can depend on other addons
- ✅ **Dynamic Command Registration** - Register commands without plugin.yml entries
- ✅ **Event System** - Full Bukkit event listener support
- ✅ **Per-Addon Data Folders** - Automatic configuration and data management
- ✅ **Hot Reload Support** - Reload addons without restarting the server
- ✅ **Built-in Logging** - Convenient logging utilities for addon developers

## 📦 Installation

### For Server Administrators

1. Download the latest release of SWCCore
2. Place `SWCCore.jar` in your server's `plugins/` folder
3. Start the server to generate the addon directory
4. Place addon JARs in `plugins/SWCCore/addons/`
5. Restart the server or use `/addon reload`

### For Plugin Developers (Main Plugin)

1. Include the addon API in your main plugin:
```kotlin
class YourMainPlugin : JavaPlugin(), YourMainPluginInterface {
    lateinit var addonManager: AddonManager
    override lateinit var commandManager: CommandManager
    
    override fun onLoad() {
        commandManager = CommandManager(this)
        addonManager = AddonManager(this)
        
        val addonsFolder = File(dataFolder, "addons")
        addonManager.loadAddonsFromDirectory(addonsFolder)
    }
    
    override fun onEnable() {
        addonManager.enableAddons()
    }
    
    override fun onDisable() {
        addonManager.disableAddons()
        commandManager.unregisterAll()
    }
}
```

2. Implement the required interface:
```kotlin
interface YourMainPluginInterface {
    val commandManager: CommandManager
}
```

## 🚀 Creating Your First Addon

### Project Setup

#### Using Maven

Create a new Maven project with this `pom.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>my-addon</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <name>MyAddon</name>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <kotlin.version>1.9.22</kotlin.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <repositories>
        <repository>
            <id>spigot-repo</id>
            <url>https://hub.spigotmc.org/nexus/content/repositories/snapshots/</url>
        </repository>
    </repositories>

    <dependencies>
        <!-- Spigot API -->
        <dependency>
            <groupId>org.spigotmc</groupId>
            <artifactId>spigot-api</artifactId>
            <version>1.20.4-R0.1-SNAPSHOT</version>
            <scope>provided</scope>
        </dependency>

        <!-- SWCCore (place JAR in libs/ folder) -->
        <dependency>
            <groupId>org.crewco</groupId>
            <artifactId>swccore</artifactId>
            <version>1.0.0</version>
            <scope>system</scope>
            <systemPath>${project.basedir}/libs/SWCCore.jar</systemPath>
        </dependency>

        <!-- Kotlin -->
        <dependency>
            <groupId>org.jetbrains.kotlin</groupId>
            <artifactId>kotlin-stdlib</artifactId>
            <version>${kotlin.version}</version>
        </dependency>
    </dependencies>

    <build>
        <sourceDirectory>src/main/kotlin</sourceDirectory>
        
        <resources>
            <resource>
                <directory>src/main/resources</directory>
                <filtering>true</filtering>
            </resource>
        </resources>
        
        <plugins>
            <!-- Kotlin Maven Plugin -->
            <plugin>
                <groupId>org.jetbrains.kotlin</groupId>
                <artifactId>kotlin-maven-plugin</artifactId>
                <version>${kotlin.version}</version>
                <executions>
                    <execution>
                        <id>compile</id>
                        <phase>compile</phase>
                        <goals>
                            <goal>compile</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>

            <!-- Shade Plugin -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>3.5.0</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                        <configuration>
                            <relocations>
                                <relocation>
                                    <pattern>kotlin</pattern>
                                    <shadedPattern>com.example.myaddon.lib.kotlin</shadedPattern>
                                </relocation>
                            </relocations>
                            <filters>
                                <filter>
                                    <artifact>*:*</artifact>
                                    <excludes>
                                        <exclude>META-INF/*.SF</exclude>
                                        <exclude>META-INF/*.DSA</exclude>
                                        <exclude>META-INF/*.RSA</exclude>
                                    </excludes>
                                </filter>
                            </filters>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```

#### Using Gradle

```kotlin
plugins {
    kotlin("jvm") version "1.9.22"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.example"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT")
    compileOnly(files("libs/SWCCore.jar"))
    implementation(kotlin("stdlib"))
}

tasks {
    shadowJar {
        archiveClassifier.set("")
        relocate("kotlin", "com.example.myaddon.lib.kotlin")
        minimize()
    }
    
    build {
        dependsOn(shadowJar)
    }
}

kotlin {
    jvmToolchain(17)
}
```

### Create manifest.yml

Create `src/main/resources/manifest.yml`:

```yaml
# Required fields
name: MyAddon
version: 1.0.0
main: com.example.myaddon.MyAddon
author: YourName
description: A cool addon for SWCCore

# Optional fields
authors:
  - YourName
  - Contributor

# List other addons this addon depends on
dependencies: []

website: https://example.com
```

### Create Your Addon Class

Create `src/main/kotlin/com/example/myaddon/MyAddon.kt`:

```kotlin
package com.example.myaddon

import org.crewco.swccore.api.addon.AbstractAddon
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.Plugin

class MyAddon(plugin: Plugin) : AbstractAddon(plugin), Listener {
    
    override val id = "my-addon"
    override val name = "My Addon"
    override val version = "1.0.0"
    override val authors = listOf("YourName")
    override val description = "A cool addon"
    
    override fun onLoad() {
        super.onLoad()
        logInfo("Loading addon...")
    }
    
    override fun onEnable() {
        super.onEnable()
        
        // Register event listeners
        plugin.server.pluginManager.registerEvents(this, plugin)
        
        // Register commands (no plugin.yml needed!)
        registerCommand(
            name = "myaddon",
            description = "Main addon command",
            usage = "/myaddon"
        ) { sender, _, _, args ->
            sender.sendMessage("§aHello from MyAddon!")
            true
        }
        
        logInfo("Addon enabled!")
    }
    
    override fun onDisable() {
        super.onDisable()
        logInfo("Addon disabled!")
    }
    
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        event.player.sendMessage("§aWelcome! MyAddon is active.")
    }
}
```

### Build Your Addon

**Maven:**
```bash
mvn clean package
```

**Gradle:**
```bash
./gradlew build
```

Your addon JAR will be in `target/` (Maven) or `build/libs/` (Gradle).

## 📖 API Reference

### Addon Lifecycle

```kotlin
class MyAddon(plugin: Plugin) : AbstractAddon(plugin) {
    
    override fun onLoad() {
        // Called when addon is discovered and loaded
        // Initialize data structures, load config
    }
    
    override fun onEnable() {
        // Called when addon is enabled
        // Register listeners, commands, start tasks
    }
    
    override fun onDisable() {
        // Called when addon is disabled
        // Save data, cleanup resources
        // Commands are automatically unregistered
    }
    
    override fun onReload() {
        // Called when addon is reloaded
        // Reload configuration, refresh data
    }
}
```

### Command Registration

```kotlin
// Simple command
registerCommand("mycommand") { sender, _, _, args ->
    sender.sendMessage("Command executed!")
    true
}

// With tab completion
registerCommand(
    name = "setmode",
    description = "Set game mode",
    usage = "/setmode <mode>",
    aliases = listOf("gm", "mode"),
    tabCompleter = SimpleTabCompleter { _, _, _, args ->
        if (args.size == 1) {
            listOf("creative", "survival", "adventure")
                .filter { it.startsWith(args[0].lowercase()) }
        } else null
    }
) { sender, _, _, args ->
    // Handle command
    true
}
```

### Event Listeners

```kotlin
class MyAddon(plugin: Plugin) : AbstractAddon(plugin), Listener {
    
    override fun onEnable() {
        super.onEnable()
        plugin.server.pluginManager.registerEvents(this, plugin)
    }
    
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        event.player.sendMessage("Welcome!")
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    fun onBlockBreak(event: BlockBreakEvent) {
        // Handle event
    }
}
```

### Configuration

```kotlin
class MyAddon(plugin: Plugin) : AbstractAddon(plugin) {
    
    private val config = AddonConfig(this)
    
    override fun onLoad() {
        super.onLoad()
        config.load()
    }
    
    override fun onEnable() {
        super.onEnable()
        
        val enabled = config.getBoolean("feature.enabled", true)
        val message = config.getString("messages.welcome", "Welcome!")
        
        config.set("last-loaded", System.currentTimeMillis())
        config.save()
    }
}
```

### Logging

```kotlin
logInfo("Info message")
logWarning("Warning message")
logError("Error message", exception)
```

### Data Folder

```kotlin
override fun onEnable() {
    super.onEnable()
    
    // dataFolder is automatically created
    val configFile = File(dataFolder, "config.yml")
    val dataFile = File(dataFolder, "data.json")
}
```

### Accessing Main Plugin

```kotlin
override fun onEnable() {
    super.onEnable()
    
    val mainPlugin = plugin as Startup
    // Access main plugin API
}
```

### Dependencies

```kotlin
class MyAddon(plugin: Plugin) : AbstractAddon(plugin) {
    
    override val dependencies = listOf("other-addon-id")
    
    override fun onEnable() {
        super.onEnable()
        
        val mainPlugin = plugin as Startup
        val otherAddon = mainPlugin.addonManager.getAddon("other-addon-id")
        // Use other addon's API
    }
}
```

## 🎮 Commands

The main plugin provides these commands:

| Command | Description | Permission |
|---------|-------------|------------|
| `/addon` | List all loaded addons | `swccore.addon` |
| `/addon list` | Show detailed addon list | `swccore.addon` |
| `/addon info <id>` | Show addon information | `swccore.addon` |
| `/addon reload [id]` | Reload addon(s) | `swccore.addon` |

## 📝 manifest.yml Reference

```yaml
# Required Fields
name: AddonName              # Display name of your addon
version: 1.0.0              # Version number (semver recommended)
main: com.example.MyAddon   # Full path to main addon class
author: YourName            # Primary author
description: Description    # Brief description

# Optional Fields
authors:                    # List of all authors
  - Author1
  - Author2

dependencies:               # List of addon IDs this addon depends on
  - dependency-addon-id

website: https://example.com  # Homepage or documentation URL
```

## 🔧 Advanced Examples

### Custom Command Executor

```kotlin
class MyCommandExecutor(private val addon: MyAddon) : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        when (args.firstOrNull()?.lowercase()) {
            "help" -> showHelp(sender)
            "info" -> showInfo(sender)
            "reload" -> reload(sender)
            else -> sender.sendMessage("§cUnknown subcommand!")
        }
        return true
    }
    
    private fun showHelp(sender: CommandSender) {
        sender.sendMessage("§e=== MyAddon Help ===")
        sender.sendMessage("§7/myaddon help - Show this help")
        sender.sendMessage("§7/myaddon info - Show addon info")
    }
}

// Register it
override fun onEnable() {
    super.onEnable()
    registerCommand(
        name = "myaddon",
        executor = MyCommandExecutor(this),
        tabCompleter = MyTabCompleter()
    )
}
```

### Async Tasks

```kotlin
override fun onEnable() {
    super.onEnable()
    
    plugin.server.scheduler.runTaskAsynchronously(plugin, Runnable {
        // Long-running operation
        val data = database.loadData()
        
        // Return to main thread
        plugin.server.scheduler.runTask(plugin, Runnable {
            applyData(data)
        })
    })
}
```

### Repeating Tasks

```kotlin
private val tasks = mutableListOf<BukkitTask>()

override fun onEnable() {
    super.onEnable()
    
    val task = plugin.server.scheduler.runTaskTimer(plugin, Runnable {
        // Runs every second
        logInfo("Tick!")
    }, 0L, 20L)
    
    tasks.add(task)
}

override fun onDisable() {
    super.onDisable()
    tasks.forEach { it.cancel() }
    tasks.clear()
}
```

## 🐛 Troubleshooting

### Addon Not Loading

**Problem:** Addon doesn't appear in `/addon list`

**Solutions:**
- Check that `manifest.yml` exists in the JAR at the root level
- Verify the `main` field points to the correct class
- Check server console for error messages
- Ensure your addon class implements `Addon` interface
- Verify the constructor takes `Plugin` as parameter

**Check manifest:**
```
make sure you have a manifest.yml in your resources folder
```

### Commands Not Working

**Problem:** Commands registered but not responding

**Solutions:**
- Ensure `super.onEnable()` is called first
- Check that `commandManager` is properly initialized
- Verify command names don't conflict with existing commands
- Check console for registration errors

### Class Not Found

**Problem:** `ClassNotFoundException` when loading addon

**Solutions:**
- Check that the package path in `main` matches your class location
- Verify all dependencies are shaded into the JAR

### Events Not Firing

**Problem:** Event handlers not being called

**Solutions:**
- Ensure your class implements `Listener`
- Register the listener: `plugin.server.pluginManager.registerEvents(this, plugin)`
- Check event priority and cancellation status

## 📚 Additional Resources

- [Spigot API Documentation](https://hub.spigotmc.org/javadocs/spigot/)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Bukkit Event Tutorial](https://bukkit.fandom.com/wiki/Event_API_Reference)

## 🤝 Contributing

Contributions are welcome! Please follow these guidelines:

1. Fork the repository
2. Create a feature branch
3. Write tests for new features
4. Ensure code follows Kotlin conventions
5. Submit a pull request

## 📄 License

[Your License Here]

## 💬 Support

- **Issues:** [GitHub Issues](https://github.com/yourrepo/issues)
- **Discord:** [Your Discord](https://discord.gg/yourserver)
- **Email:** business@crewco.org

## ✨ Credits

Created by [CrewCo]

Special thanks to all contributors!

---

**Made with ❤️ for the Spigot community**