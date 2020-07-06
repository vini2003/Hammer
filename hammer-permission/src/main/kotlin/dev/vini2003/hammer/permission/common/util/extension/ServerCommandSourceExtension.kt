package dev.vini2003.hammer.permission.common.util.extension

import dev.vini2003.hammer.permission.common.role.Role
import dev.vini2003.hammer.permission.common.util.PermissionUtils
import net.minecraft.server.command.ServerCommandSource

fun ServerCommandSource.hasRole(role: Role) = PermissionUtils.hasPermission(player, role.inheritanceNode.key)
