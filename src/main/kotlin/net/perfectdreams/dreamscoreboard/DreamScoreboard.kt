package net.perfectdreams.dreamscoreboard

import com.okkero.skedule.SynchronizationContext
import com.okkero.skedule.schedule
import net.perfectdreams.dreamcore.utils.KotlinPlugin
import net.perfectdreams.dreamcore.utils.registerEvents
import net.perfectdreams.dreamcore.utils.scheduler
import net.perfectdreams.dreamscoreboard.commands.GlowingCommand
import net.perfectdreams.dreamscoreboard.utils.PlayerScoreboard
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.concurrent.ConcurrentHashMap

class DreamScoreboard : KotlinPlugin(), Listener {
	companion object {
		var CURRENT_TICK = 0
	}

	val scoreboards = ConcurrentHashMap<Player, PlayerScoreboard>()

	override fun softEnable() {
		super.softEnable()
		registerEvents(this)

		registerCommand(GlowingCommand())
		
		scheduler().schedule(this, SynchronizationContext.ASYNC) {
			while (true) {
				CURRENT_TICK++
				if (CURRENT_TICK > 19) {
					CURRENT_TICK = 0
				}
				scoreboards.values.forEach {
					try {
						it.updateScoreboard()
					} catch (e: Exception) {
						e.printStackTrace()
					}
				}
				waitFor(20)
			}
		}
	}

	override fun softDisable() {
		super.softDisable()
	}

	@EventHandler
	fun onJoin(e: PlayerJoinEvent) {
		val playerScoreboard = PlayerScoreboard(e.player)
		playerScoreboard.updateScoreboard()
		scoreboards[e.player] = playerScoreboard
	}

	@EventHandler
	fun onQuit(e: PlayerQuitEvent) {
		scoreboards.remove(e.player)
	}
}