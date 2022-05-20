package dev.vini2003.hammer.gui.api.common.util

import dev.vini2003.hammer.gui.api.client.util.extension.*
import net.minecraft.client.gui.hud.InGameHud
import net.minecraft.entity.player.PlayerEntity

object InGameHudUtils {
	val INSTANCE: InGameHud?
		get() {
			val client = InstanceUtils.CLIENT ?: return null
			
			return client.inGameHud
		}

	@JvmStatic
	fun getLeftBarPos(hud: InGameHud, playerEntity: PlayerEntity) = hud.getLeftBarPos(playerEntity)
	
	@JvmStatic
	fun getRightBarPos(hud: InGameHud, playerEntity: PlayerEntity) = hud.getRightBarPos(playerEntity)
	
	@JvmStatic
	fun getHeartBarPos(hud: InGameHud, playerEntity: PlayerEntity) = hud.getHeartBarPos(playerEntity)
	
	@JvmStatic
	fun getArmorBarPos(hud: InGameHud, playerEntity: PlayerEntity) = hud.getArmorBarPos(playerEntity)
	
	@JvmStatic
	fun getAirBarPos(hud: InGameHud, playerEntity: PlayerEntity) = hud.getAirBarPos(playerEntity)
	
	@JvmStatic
	fun getHungerBarPos(hud: InGameHud, playerEntity: PlayerEntity) = hud.getHungerBarPos(playerEntity)
}