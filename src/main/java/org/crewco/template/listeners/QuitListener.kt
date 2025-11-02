package org.crewco.template.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import org.crewco.template.ExampleAddon

class QuitListener(private val addon: ExampleAddon) : Listener {

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        addon.logInfo("${event.player.name} left")
    }
}