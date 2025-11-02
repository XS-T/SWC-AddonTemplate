package org.crewco.template.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.crewco.template.ExampleAddon

class MainCommand(private val addon: ExampleAddon) : CommandExecutor, TabCompleter {

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (args.isEmpty()) {
            showHelp(sender)
            return true
        }

        when (args[0].lowercase()) {
            "help" -> showHelp(sender)
            "info" -> showInfo(sender)
            "version" -> showVersion(sender)
            "reload" -> reload(sender)
            else -> {
                sender.sendMessage("§cUnknown subcommand: ${args[0]}")
                sender.sendMessage("§7Use §e/example help")
            }
        }

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): List<String>? {
        if (args.size == 1) {
            return listOf("help", "info", "version", "reload")
                .filter { it.startsWith(args[0].lowercase()) }
        }
        return null
    }

    private fun showHelp(sender: CommandSender) {
        sender.sendMessage("§e=== Example Addon Help ===")
        sender.sendMessage("§7/example help §f- Show this help")
        sender.sendMessage("§7/example info §f- Show addon info")
        sender.sendMessage("§7/example version §f- Show version")
        sender.sendMessage("§7/example reload §f- Reload addon")
    }

    private fun showInfo(sender: CommandSender) {
        sender.sendMessage("§e=== ${addon.name} ===")
        sender.sendMessage("§7${addon.description}")
        sender.sendMessage("§7By: ${addon.authors.joinToString(", ")}")
    }

    private fun showVersion(sender: CommandSender) {
        sender.sendMessage("§e${addon.name} §7v${addon.version}")
    }

    private fun reload(sender: CommandSender) {
        if (!sender.hasPermission("exampleaddon.reload")) {
            sender.sendMessage("§cYou don't have permission!")
            return
        }

        addon.onReload()
        sender.sendMessage("§aAddon reloaded!")
    }
}