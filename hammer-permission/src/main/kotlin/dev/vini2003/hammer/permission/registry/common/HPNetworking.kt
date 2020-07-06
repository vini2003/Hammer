package dev.vini2003.hammer.permission.registry.common

import dev.vini2003.hammer.H

object HPNetworking {
	fun init() = Unit
	
	@JvmField
	val ADD_ROLE = H.id("add_role")
	@JvmField
	val REMOVE_ROLE = H.id("remove_role")
	@JvmField
	val SYNC_ROLES = H.id("sync_roles")
}