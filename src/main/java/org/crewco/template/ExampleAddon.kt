package org.crewco.template


import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.Plugin
import org.crewco.swccore.api.addon.AbstractAddon
import org.crewco.swccore.api.addon.AddonConfig
import org.crewco.template.commands.MainCommand
import org.crewco.template.listeners.JoinListener
import org.crewco.template.listeners.QuitListener

/**
 * Example addon implementation.
 * This shows how to create an addon for your plugin.
 */

class ExampleAddon(plugin: Plugin) : AbstractAddon(plugin) {
    companion object{
        lateinit var addon:ExampleAddon
        lateinit var config: AddonConfig
    }
    override fun onLoad() {
        super.onLoad()
        logInfo("Example addon is loading...")

        // Initialize your addon's data here
        // Load configuration, setup data structures, etc.
        addon = this
        config = AddonConfig(this)
        config.load()
    }

    override fun onEnable() {
        super.onEnable()

        // Can check for Plugin deps manually, but its handled at core level via the manifes
        /**
         *         if (!hasPluginDependency(this.plugin,"Vault")) {
         *             logWarning("Vault not found - some features disabled")
         *         }
         */

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
        config.reload()
        config.save()
    }
}