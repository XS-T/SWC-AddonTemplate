package org.crewco.template


import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.Plugin
import org.crewco.swccore.api.addon.AbstractAddon
import org.crewco.template.commands.MainCommand
import org.crewco.template.listeners.JoinListener
import org.crewco.template.listeners.QuitListener

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
            name = "example",
            executor = MainCommand(this),
            description = "Main example command",
            usage = "/example <subcommand>",
            aliases = listOf("ex", "exaddon"),
            tabCompleter = MainCommand(this)
        )


        // Register all listeners in one call
        logInfo("Registering event listeners...")
        registerEvents(
            JoinListener(this),
            QuitListener(this),
        )
        logInfo("Event listeners registered!")

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