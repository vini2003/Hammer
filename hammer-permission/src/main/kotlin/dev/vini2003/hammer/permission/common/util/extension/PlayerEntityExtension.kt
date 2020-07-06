package dev.vini2003.hammer.permission.common.util.extension

import dev.vini2003.hammer.permission.common.role.Role
import dev.vini2003.hammer.permission.common.util.PermissionUtils
import net.minecraft.entity.player.PlayerEntity

fun PlayerEntity.hasRole(role: Role) = (world.isClient && role.holders.contains(uuid)) || PermissionUtils.hasPermission(this, role.inheritanceNode.key)

fun PlayerEntity.addRole(role: Role) = role.addTo(this)
fun PlayerEntity.removeRole(role: Role) = role.removeFrom(this)

fun PlayerEntity.addRoleInClient(role: Role) = role.addToClient(uuid)
fun PlayerEntity.removeRoleInClient(role: Role) = role.removeFromClient(uuid)