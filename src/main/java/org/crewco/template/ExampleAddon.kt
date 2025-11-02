package org.crewco.template


import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.Plugin
import org.crewco.swccore.api.addon.AbstractAddon

/**
 * Example addon implementation.
 * This shows how to create an addon for your plugin.
 */
class ExampleAddon(plugin: Plugin) : AbstractAddon(plugin), Listener {

    override val id: String = "example-addon"
    override val name: String = "Example Addon"
    override val version: String = "1.0.0"
    override val authors: List<String> = listOf("YourName")
    override val description: String = "An example addon that demonstrates the API"

    // Optional: specify dependencies on other addons
    override val dependencies: List<String> = emptyList()

    override fun onLoad() {
        super.onLoad()
        logInfo("Example addon is loading...")

        // Initialize your addon's data here
        // Load configuration, setup data structures, etc.
    }

    override fun onEnable() {
        super.onEnable()

        // Register event listeners
        plugin.server.pluginManager.registerEvents(this, plugin)

        // Register commands using the CommandManager (no plugin.yml needed!)
        registerCommand(
            name = "examplecommand",
            description = "An example command from the addon",
            usage = "/examplecommand [arg]"
        ) { sender, _, _, args ->
            sender.sendMessage("§aHello from Example Addon!")
            sender.sendMessage("§7You passed ${args.size} arguments")
            if (args.isNotEmpty()) {
                sender.sendMessage("§7First argument: ${args[0]}")
            }
            true
        }

        // Another example with tab completion
        registerCommand(
            name = "exampleinfo",
            description = "Shows addon information",
            usage = "/exampleinfo",
            aliases = listOf("einfo", "exinfo"),
            tabCompleter = org.bukkit.command.TabCompleter { _, _, _, args ->
                if (args.size == 1) {
                    listOf("version", "author", "status").filter {
                        it.startsWith(args[0].lowercase())
                    }
                } else {
                    emptyList()
                }
            }
        ) { sender, _, _, args ->
            when (args.firstOrNull()?.lowercase()) {
                "version" -> sender.sendMessage("§aVersion: $version")
                "author" -> sender.sendMessage("§aAuthors: ${authors.joinToString(", ")}")
                "status" -> sender.sendMessage("§aStatus: ${if (isEnabled) "§2Enabled" else "§cDisabled"}")
                else -> {
                    sender.sendMessage("§e=== $name ===")
                    sender.sendMessage("§7Version: §f$version")
                    sender.sendMessage("§7Authors: §f${authors.joinToString(", ")}")
                    sender.sendMessage("§7Description: §f$description")
                }
            }
            true
        }

        logInfo("Example addon has been enabled!")

        logInfo("Example addon has been enabled!")
    }

    override fun onDisable() {
        super.onDisable()

        // Clean up resources
        // Save data, close connections, etc.

        logInfo("Example addon has been disabled!")
    }

    override fun onReload() {
        super.onReload()

        // Reload configuration or data
        logInfo("Example addon has been reloaded!")
    }

    // Example event handler
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        event.player.sendMessage("§aWelcome! This message is from Example Addon.")
    }
}