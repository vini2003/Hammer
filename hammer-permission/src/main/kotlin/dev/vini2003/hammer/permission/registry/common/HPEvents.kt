package dev.vini2003.hammer.permission.registry.common

import dev.vini2003.hammer.permission.common.manager.RoleManager
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents

object HPEvents {
	fun init() {
		ServerPlayConnectionEvents.JOIN.register(RoleManager.ServerPlayConnectionEventsJoinListener)
	}
}