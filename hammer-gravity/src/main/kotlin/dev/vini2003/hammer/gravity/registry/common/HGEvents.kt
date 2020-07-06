package dev.vini2003.hammer.gravity.registry.common

import dev.vini2003.hammer.gravity.common.manager.GravityManager
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents

object HGEvents {
	fun init() {
		ServerPlayConnectionEvents.JOIN.register(GravityManager.ServerPlayConnectionEventsJoinListener)
	}
}