/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 vini2003
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.vini2003.hammer.permission.api.common.util

import dev.vini2003.hammer.permission.api.common.role.Role
import dev.vini2003.hammer.permission.api.common.util.extension.*
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