# Plugin Addon System

This is a template for creating a Spigot plugin with an addon/extension system in Kotlin.

## Overview

This system allows other developers to create addons (extensions) for your plugin without modifying the core plugin code. Addons are loaded dynamically from JAR files and have access to your plugin's API.

## Project Structure

```
addon-api/
├── src/main/kotlin/com/yourplugin/
│   ├── YourMainPlugin.kt          # Main plugin class
│   └── api/
│       ├── Addon.kt                # Interface that all addons implement
│       ├── AbstractAddon.kt        # Base class with helper methods
│       └── AddonManager.kt         # Manages addon lifecycle
├── examples/
│   └── ExampleAddon.kt             # Example addon implementation
├── addon-template/
│   └── build.gradle.kts            # Template build file for addon developers
└── build.gradle.kts                # Main plugin build file
```

## Features

- ✅ Dynamic addon loading from JAR files
- ✅ Lifecycle management (onLoad, onEnable, onDisable, onReload)
- ✅ Dependency checking between addons
- ✅ Per-addon data folders
- ✅ Logging utilities
- ✅ Hot reload support
- ✅ Command interface for managing addons

## For Plugin Developers (You)

### 1. Setting Up Your Main Plugin

1. **Customize the package names** in all files from `com.yourplugin` to your actual package.

2. **Initialize the AddonManager** in your main plugin class:

```kotlin
class YourMainPlugin : JavaPlugin() {
    lateinit var addonManager: AddonManager
        private set
    
    override fun onLoad() {
        addonManager = AddonManager(this)
        val addonsFolder = File(dataFolder, "addons")
        addonManager.loadAddonsFromDirectory(addonsFolder)
    }
    
    override fun onEnable() {
        addonManager.enableAddons()
    }
    
    override fun onDisable() {
        addonManager.disableAddons()
    }
}
```

3. **Build your plugin**:
```bash
./gradlew shadowJar
```

The JAR will be in `build/libs/`.

### 2. Creating Additional API Classes

You can add more classes to your API that addons can use:

```kotlin
// src/main/kotlin/com/yourplugin/api/YourAPI.kt
package com.yourplugin.api

class YourAPI {
    fun doSomething() {
        // Your API methods that addons can call
    }
}
```

Make these accessible through your main plugin:

```kotlin
class YourMainPlugin : JavaPlugin() {
    val api = YourAPI()
    // ...
}
```

### 3. Publishing Your Plugin API

For addon developers to use your API, you have two options:

**Option A: Local Development**
- Share your plugin JAR file
- Addon developers place it in their `libs/` folder

**Option B: Maven Repository** (Recommended)
- Publish your plugin to Maven Central, Jitpack, or a private repository
- Addon developers add it as a Gradle dependency

## For Addon Developers

### 1. Setting Up a New Addon

1. **Create a new Kotlin project** with the provided `addon-template/build.gradle.kts`

2. **Add the main plugin JAR** to your `libs/` folder or add it as a Gradle dependency

3. **Create your addon class**:

```kotlin
package com.example.myaddon

import com.yourplugin.api.AbstractAddon
import org.bukkit.plugin.Plugin

class MyAddon(plugin: Plugin) : AbstractAddon(plugin) {
    override val id = "my-addon"
    override val name = "My Addon"
    override val version = "1.0.0"
    override val authors = listOf("YourName")
    override val description = "My cool addon"
    
    override fun onEnable() {
        super.onEnable()
        logInfo("My addon is enabled!")
    }
}
```

### 2. Building Your Addon

Update the manifest in `build.gradle.kts` to point to your main class:

```kotlin
manifest {
    attributes(
        "Addon-Main" to "com.example.myaddon.MyAddon"
    )
}
```

Build the addon:
```bash
./gradlew addonJar
```

### 3. Installing Your Addon

1. Copy the JAR from `build/libs/` to the server's `plugins/YourMainPlugin/addons/` folder
2. Restart the server or use `/addon reload`

### 4. Addon Features

**Event Listeners:**
```kotlin
class MyAddon(plugin: Plugin) : AbstractAddon(plugin), Listener {
    override fun onEnable() {
        super.onEnable()
        plugin.server.pluginManager.registerEvents(this, plugin)
    }
    
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        event.player.sendMessage("Hello from my addon!")
    }
}
```

**Commands:**
```kotlin
override fun onEnable() {
    super.onEnable()
    plugin.getCommand("mycommand")?.setExecutor { sender, _, _, _ ->
        sender.sendMessage("Command from addon!")
        true
    }
}
```

**Data Storage:**
```kotlin
override fun onEnable() {
    super.onEnable()
    val configFile = File(dataFolder, "config.yml")
    // dataFolder is automatically created
}
```

**Dependencies:**
```kotlin
override val dependencies = listOf("other-addon-id")
```

### 5. Accessing Main Plugin API

```kotlin
class MyAddon(plugin: Plugin) : AbstractAddon(plugin) {
    override fun onEnable() {
        super.onEnable()
        
        // Cast to your main plugin type
        val mainPlugin = plugin as YourMainPlugin
        mainPlugin.api.doSomething()
    }
}
```

## Commands

The main plugin provides these commands:

- `/addon list` - List all loaded addons with details
- `/addon reload [id]` - Reload all addons or a specific addon
- `/addon info <id>` - Show detailed information about an addon

## Advanced Features

### Custom Events

You can create custom events that addons can listen to:

```kotlin
// In your main plugin
class CustomEvent : Event() {
    companion object {
        private val HANDLERS = HandlerList()
        
        @JvmStatic
        fun getHandlerList() = HANDLERS
    }
    
    override fun getHandlers() = HANDLERS
}

// Addons can listen to it
@EventHandler
fun onCustomEvent(event: CustomEvent) {
    // Handle event
}
```

### Service Providers

Create services that addons can use:

```kotlin
// Main plugin
interface MyService {
    fun doSomething()
}

class MyServiceImpl : MyService {
    override fun doSomething() { }
}

// Make available through your plugin instance
val mainPlugin = plugin as YourMainPlugin
val service = mainPlugin.getService(MyService::class.java)
```

## Best Practices

1. **Version Your API**: Use semantic versioning for your main plugin
2. **Document Your API**: Provide clear documentation for addon developers
3. **Backwards Compatibility**: Try to maintain API compatibility between versions
4. **Error Handling**: Wrap addon calls in try-catch to prevent one addon from crashing others
5. **Permissions**: Consider adding permission nodes for addons
6. **Configuration**: Provide configuration options for your addon system

## Troubleshooting

**Addon not loading?**
- Check that MANIFEST.MF has the `Addon-Main` attribute
- Verify the main class path is correct
- Check server logs for errors

**ClassNotFoundException?**
- Ensure the main plugin API is available
- Check that dependencies are properly shaded

**Addon not enabling?**
- Check for missing dependencies
- Review the addon's onEnable() method for errors

## License

Open Source

## Support

business@crewco.org