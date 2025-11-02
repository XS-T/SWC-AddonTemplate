package org.crewco.template.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.crewco.template.ExampleAddon

class JoinListener(private val addon: ExampleAddon) : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        player.sendMessage("§aWelcome ${player.name}!")
        addon.logInfo("${player.name} joined")
    }
}