package org.crewco.template.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.crewco.template.ExampleAddon

class InfoCommand(private val addon: ExampleAddon) : CommandExecutor {

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        sender.sendMessage("§e=== ${addon.name} ===")
        sender.sendMessage("§7Version: ${addon.version}")
        sender.sendMessage("§7Authors: ${addon.authors.joinToString(", ")}")
        sender.sendMessage("§7Description: ${addon.description}")
        return true
    }
}