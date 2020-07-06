package dev.vini2003.hammer.util.registry.common

import dev.vini2003.hammer.H

object HUNetworking {
	fun init() = Unit
	
	@JvmField
	val FLY_SPEED_UPDATE = H.id("fly_speed_update")
	@JvmField
	val WALK_SPEED_UPDATE = H.id("walk_speed_update")
}