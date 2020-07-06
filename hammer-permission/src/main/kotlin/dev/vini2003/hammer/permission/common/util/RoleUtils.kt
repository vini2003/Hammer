package dev.vini2003.hammer.permission.common.util

import dev.vini2003.hammer.permission.common.role.Role
import dev.vini2003.hammer.permission.common.util.extension.*
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.command.ServerCommandSource

object RoleUtils {
	@JvmStatic
	fun hasRole(player: PlayerEntity, role: Role) = player.hasRole(role)
	
	@JvmStatic
	fun addRole(player: PlayerEntity, role: Role) = player.addRole(role)
	
	@JvmStatic
	fun removeRole(player: PlayerEntity, role: Role) = player.removeRole(role)
	
	@JvmStatic
	fun addRoleInClient(player: PlayerEntity, role: Role) = player.addRoleInClient(role)
	
	@JvmStatic
	fun removeRoleInClient(player: PlayerEntity, role: Role) = player.removeRoleInClient(role)
	
	@JvmStatic
	fun hasRole(source: ServerCommandSource, role: Role) = source.hasRole(role)
}