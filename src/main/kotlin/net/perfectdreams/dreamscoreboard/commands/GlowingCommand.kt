package net.perfectdreams.dreamscoreboard.commands

import net.perfectdreams.commands.annotation.Subcommand
import net.perfectdreams.commands.bukkit.SparklyCommand
import org.bukkit.entity.Player

class GlowingCommand : SparklyCommand(arrayOf("glowing", "glow"), permission = "dreamscoreboard.glowing") {
    @Subcommand
    fun glow(player: Player) {
        if (player.isGlowing) {
            player.isGlowing = false
            player.sendMessage("§aAgora você parou de brilhar... que triste, né?")
        } else {
            player.isGlowing = true
            player.sendMessage("§aAgora você está brilhando amigx! Tá divaaaaaa :3")
        }
    }
}