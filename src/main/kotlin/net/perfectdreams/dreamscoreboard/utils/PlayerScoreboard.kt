package net.perfectdreams.dreamscoreboard.utils

import net.perfectdreams.dreamcore.DreamCore
import net.perfectdreams.dreamcore.utils.PhoenixScoreboard
import net.perfectdreams.dreamcore.utils.balance
import net.perfectdreams.dreamcore.utils.extensions.artigo
import net.perfectdreams.dreamcore.utils.onlinePlayers
import net.perfectdreams.dreamscoreboard.DreamScoreboard
import net.perfectdreams.dreamvote.DreamVote
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.TimeUnit

class PlayerScoreboard(val m: DreamScoreboard, val player: Player) {
	val phoenix: PhoenixScoreboard = PhoenixScoreboard()
	var lastIndex = 15
	var randomEmote = "?"

	init {
		phoenix.setTitle("§4§lSparkly§b§lPower")
		player.scoreboard = phoenix.scoreboard
	}

	fun updateScoreboard() {
		if (DreamScoreboard.CURRENT_TICK == 0)
			randomEmote = DreamScoreboard.EMOTES.random()

		phoenix.setTitle("§6✪ §r$randomEmote §4§lSparkly§b§lPower §r$randomEmote §6✪")

		if (phoenix.scoreboard.getObjective("health") == null) {
			val healthObj = phoenix.scoreboard.registerNewObjective("health", "health")
			healthObj.displaySlot = DisplaySlot.PLAYER_LIST
		}

		setupTeams()

		player.setPlayerListHeaderFooter(
				"""§4||§c|§f|§b|§3|| §6»»§e»»§f»» §8§l[ §4§lSparkly§b§lPower §8§] §f««§e««§6«« §4||§c|§f|§b|§3||
    |§3§omc.sparklypower.net
    |§3§m🟆-§b§m-§3§m-§b§m-§3§m-§b§m-§3§m-§b§m-§3§m-§b§m-§3§m-§b§m-§3§m-§b§m-§3§m-§b§m-§3§m-§b§m-§3§m-§b§m-§3§m-§b§m-§3§m-§3§m-🟆
    |§e§lSeja bem-vind${player.artigo} ${player.displayName}§e§l!
    |§6Modéstia à parte... esse servidor é incrível!
    |§8§m-§3§m-§b§m-§f§m-§b§m-§3§m-§8§m
    |§6§lPrecisa de ajuda? §e/ajuda
    |§6§lAlguma dúvida? §6§oPergunte no chat!
    |§8§m-§3§m-§b§m-§f§m-§b§m-§3§m-§8§m
""".trimMargin(),
				"""§8§m-§3§m-§b§m-§f§m-§b§m-§3§m-§8§m
    |§f锈 §bQuer ajudar o servidor? Então compre VIP! §f锈
    |§3https://sparklypower.net/loja
    |
    |§f閍 §bVote no servidor para receber recompensas incríveis! §f閍
    |§3https://sparklypower.net/votar
    |
    |§f閌 §7SparklyPower é o servidor oficial da Loritta Morenitta! • https://loritta.website/ §f閌
    |§7Lembre-se... você é incrível, continue sendo uma pessoa maravilhosa e ajude a
    |§7transformar o mundo em um lugar melhor!
    |§3§m🟆-§b§m-§3§m-§b§m-§3§m-§b§m-§3§m-§b§m-§3§m-§b§m-§3§m-§b§m-§3§m-§b§m-§3§m-§b§m-§3§m-§b§m-§3§m-§b§m-§3§m-§b§m-§3§m-§3§m-🟆
""".trimMargin()
		)

		var idx = 15

		idx = setupPlayersOnline(idx)
		phoenix.setText("§c", idx--)

		if (DreamScoreboard.CURRENT_TICK in 0..4) {
			idx = setupClock(idx)
			phoenix.setText("§c", idx--)

			idx = setupMoney(idx)

			phoenix.setText("§c", idx--)
			idx = setupActiveEvents(idx)
		}

		if (DreamScoreboard.CURRENT_TICK in 5..9) {
			idx = setupUpcomingEvents(idx)
		}

		if (DreamScoreboard.CURRENT_TICK in 10..14) {
			idx = setupStaff(idx)
		}

		if (DreamScoreboard.CURRENT_TICK in 15..19) {
			idx = setupLastVoter(idx)
			phoenix.setText("§c", idx--)
			idx = setupFacebook(idx)
			phoenix.setText("§c", idx--)
			idx = setupTwitter(idx)
			phoenix.setText("§c", idx--)
			idx = setupDiscord(idx)
		}

		var lastIndex = this.lastIndex

		for (idx in idx downTo (Math.max(1, lastIndex))) {
			phoenix.removeLine(idx)
		}

		this.lastIndex = idx
	}

	private fun setupPlayersOnline(_idx: Int): Int {
		var idx = _idx
		phoenix.setText("§3➦ §b§lPlayers Online", idx--)
		val playerCount = Bukkit.getOnlinePlayers().size
		val specialText = when (playerCount) {
			in 109..Integer.MAX_VALUE -> " ✧ﾟ･: *ヽ(◕ヮ◕ヽ)"
			in 90..99 -> " ヾ(⌐■_■)ノ♪"
			in 80..89 -> "(づ￣ ³￣)づ"
			in 70..79 -> "(｡◕‿‿◕｡)"
			in 60..69 -> "(◕‿◕✿)"
			in 50..59 -> "ʕᵔᴥᵔʔ"
			in 40..49 -> "ʕ•ᴥ•ʔ"
			in 30..39 -> "\\ (•◡•) /"
			in 20..29 -> ":D"
			in 10..19 -> ":)"
			else -> ""
		}

		phoenix.setText("§b${playerCount}§3/§b${Bukkit.getMaxPlayers()} §d${specialText}", idx--)
		return idx
	}

	private fun setupClock(_idx: Int): Int {
		var idx = _idx
		phoenix.setText("§4➦ §c§lHorário Atual", idx--)
		val calendar = Calendar.getInstance()
		phoenix.setText("§c${String.format("%02d", calendar[Calendar.HOUR_OF_DAY])}§4:§c${String.format("%02d", calendar[Calendar.MINUTE])}", idx--)
		return idx
	}

	private fun setupStaff(_idx: Int): Int {
		var idx = _idx
		val staffs = onlinePlayers().filter { it.hasPermission("pocketdreams.soustaff") }

		phoenix.setText("§6➦ §e§lStaff Online", idx--)

		if (staffs.isNotEmpty()) {
			staffs.forEach {
				phoenix.setText("§e${it.name}", idx--)
			}
		} else {
			phoenix.setText("§eNinguém... :(", idx--)
		}
		return idx
	}

	private fun setupMoney(_idx: Int): Int {
		var idx = _idx
		phoenix.setText("§2➦ §a§lSonhos", idx--)
		val df = DecimalFormat("#")
		df.maximumFractionDigits = 8
		phoenix.setText("§a${df.format(player.balance)}$", idx--)
		return idx
	}

	private fun setupActiveEvents(_idx: Int): Int {
		var idx = _idx
		phoenix.setText("§6➦ §e§lEventos Ativos", idx--)
		val events = DreamCore.INSTANCE.dreamEventManager.getRunningEvents()
		if (events.isEmpty()) {
			phoenix.setText("§eNenhum... :(", idx--)
		} else {
			for (event in events) {
				phoenix.setText("§e${event.eventName}", idx--)
			}
		}
		return idx
	}

	private fun setupUpcomingEvents(_idx: Int): Int {
		var idx = _idx
		phoenix.setText("§5➦ §d§lA seguir...", idx--)
		val events = DreamCore.INSTANCE.dreamEventManager.getUpcomingEvents()
				.sortedByDescending {
					(it.delayBetween + it.lastTime) - System.currentTimeMillis()
				}

		if (events.isEmpty()) {
			phoenix.setText("§dNenhum... :(", idx--)
		} else {
			val hasPlayers = events.filter { Bukkit.getOnlinePlayers().size >= it.requiredPlayers }
			val notEnoughPlayers = events.filter { it.requiredPlayers > Bukkit.getOnlinePlayers().size }
			for (ev in hasPlayers) {
				val diff = (ev.delayBetween + ev.lastTime) - System.currentTimeMillis()
				var fancy = ""
				if (diff >= (60000 * 60)) {
					val minutes = ((diff / (1000 * 60)) % 60)
					val hours = ((diff / (1000 * 60 * 60)) % 24)
					fancy = String.format("%dh%dm",
							hours,
							minutes
					)
				} else if (diff >= 60000) {
					fancy = String.format("%dm",
							TimeUnit.MILLISECONDS.toMinutes(diff),
							TimeUnit.MILLISECONDS.toSeconds(diff) -
									TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diff))
					)
				} else {
					fancy = String.format("%ds",
							TimeUnit.MILLISECONDS.toSeconds(diff) -
									TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diff))
					)
				}
				phoenix.setText("§d" + ev.eventName + " (" + fancy + ")", idx--)
			}
			for (ev in notEnoughPlayers) {
				val requiredCount = ev.requiredPlayers - Bukkit.getOnlinePlayers().size
				val str = if (requiredCount == 1) "player" else "players"
				phoenix.setText("§d" + ev.eventName + " (+$requiredCount $str)", idx--)
			}
		}

		return idx
	}

	private fun setupLastVoter(_idx: Int): Int {
		var idx = _idx
		phoenix.setText("§5➦ §d§lÚltimo Votador", idx--)
		if (DreamVote.INSTANCE.lastVoter == null) {
			phoenix.setText("§dNinguém... §6/votar", idx--)
		} else {
			phoenix.setText("§d${DreamVote.INSTANCE.lastVoter}", idx--)
		}
		return idx
	}

	private fun setupFacebook(_idx: Int): Int {
		var idx = _idx
		phoenix.setText("§1➦ §9§lFacebook", idx--)
		phoenix.setText("§6/facebook", idx--)
		return idx
	}

	private fun setupTwitter(_idx: Int): Int {
		var idx = _idx
		phoenix.setText("§1➦ §9§lTwitter", idx--)
		phoenix.setText("§9@SparklyPower", idx--)
		return idx
	}

	private fun setupDiscord(_idx: Int): Int {
		var idx = _idx
		phoenix.setText("§1➦ §9§lDiscord", idx--)
		phoenix.setText("§6/discord", idx--)
		return idx
	}

	private fun setupTeams() {
		/* val mainScoreboard = Bukkit.getScoreboardManager().mainScoreboard

		for (team in mainScoreboard.teams) {
			val t = phoenix.scoreboard.getTeam(team.name) ?: phoenix.scoreboard.registerNewTeam(team.name)
			t.prefix = team.prefix
			t.suffix = team.suffix
			t.displayName = team.displayName
			t.color = team.color
			for (player in team.players) {
				t.addPlayer(player)
			}
		} */

		for (player in Bukkit.getOnlinePlayers()) {
			val prefix = when {
				player.hasPermission("group.dono") -> "§a§l[Dono] "
				player.hasPermission("group.admin") -> "§4§l[Admin] "
				player.hasPermission("group.moderador") -> "§9§l[Moderador] "
				player.hasPermission("group.suporte") -> "§6§l[Suporte] "
				player.hasPermission("group.vip++") -> "§b[VIP§6++§b] "
				player.hasPermission("group.vip+") -> "§b[VIP§6+§b] "
				player.hasPermission("group.vip") -> "§b[VIP§b]"
				else -> "§f"
			}
			val teamPrefix = when {
				player.hasPermission("group.dono") -> "0"
				player.hasPermission("group.admin") -> "1"
				player.hasPermission("group.moderador") -> "2"
				player.hasPermission("group.suporte") -> "3"
				player.hasPermission("group.vip++") -> "4"
				player.hasPermission("group.vip+") -> "5"
				player.hasPermission("group.vip") -> "6"
				else -> "9"
			}
			val suffix = when {
				player.hasPermission("group.dono") -> " §f閌"
				player.hasPermission("group.admin") -> " §f閌"
				player.hasPermission("group.moderador") -> " §f閌"
				player.hasPermission("group.suporte") -> " §f閌"
				player.hasPermission("group.vip++") -> " §f娀"
				player.hasPermission("group.vip+") -> " §f閍"
				player.hasPermission("group.vip") -> " §f锈"
				else -> "§f"
			}

			var teamName = player.name
			if (teamName.length > 15) {
				teamName = teamName.substring(0, 14)
			}

			teamName = teamPrefix + teamName
			val t = phoenix.scoreboard.getTeam(teamName) ?: phoenix.scoreboard.registerNewTeam(teamName)
			t.prefix = prefix
			t.suffix = suffix

			t.color = when {
				m.coloredGlow.contains(player.uniqueId) -> m.coloredGlow[player.uniqueId]
				player.hasPermission("group.dono") -> ChatColor.GREEN
				player.hasPermission("group.admin") -> ChatColor.RED
				player.hasPermission("group.moderador") -> ChatColor.DARK_AQUA
				player.hasPermission("group.suporte") -> ChatColor.GOLD
				player.hasPermission("group.vip++") -> ChatColor.AQUA
				player.hasPermission("group.vip+") -> ChatColor.AQUA
				player.hasPermission("group.vip") -> ChatColor.AQUA
				else -> ChatColor.WHITE
			}

			if (!t.hasPlayer(player))
				t.addPlayer(player)
		}

		player.scoreboard = phoenix.scoreboard
	}
}
