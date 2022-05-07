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

package dev.vini2003.hammer.permission.api.common.manager

import dev.vini2003.hammer.core.api.common.util.BufUtils
import dev.vini2003.hammer.permission.impl.packet.sync.SyncRolesPacket
import dev.vini2003.hammer.permission.api.common.role.Role
import dev.vini2003.hammer.permission.api.common.util.extension.hasRole
import dev.vini2003.hammer.permission.registry.common.HPNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayNetworkHandler
import net.minecraft.text.TextColor

object RoleManager {
	private val ROLES: MutableList<Role> = mutableListOf()
	private val ROLES_BY_NAME: MutableMap<String, Role> = mutableMapOf()
	
	/**
	 * Registers a role.
	 *
	 * @param role the role.
	 */
	fun register(role: Role) {
		ROLES.add(role)
		
		ROLES_BY_NAME[role.name] = role
	}
	
	/**
	 * Returns a role by its name.
	 *
	 * @param name the role's name.
	 * @return the role.
	 */
	fun getRoleByName(name: String): Role? {
		return ROLES_BY_NAME[name]
	}
	
	/**
	 * Returns a player's role prefix.
	 *
	 * @param player the player.
	 * @return the prefix.
	 */
	@JvmStatic
	fun getRolePrefix(player: PlayerEntity): String? {
		return ROLES.filter { role ->
			player.hasRole(role)
		}.maxByOrNull {
			role -> role.prefixWeight ?: 0
		}?.prefix
	}
	
	/**
	 * Returns a player's role prefix color.
	 *
	 * @param player the player.
	 * @return the color.
	 */
	@JvmStatic
	fun getRolePrefixColor(player: PlayerEntity): TextColor? {
		return ROLES.filter { role ->
			player.hasRole(role)
		}.maxByOrNull { role ->
			role.prefixWeight ?: 0
		}?.prefixColor?.let { color ->
			TextColor.fromRgb(color)
		}
	}
	
	/**
	 * Returns all registered roles.
	 *
	 * @return the roles.
	 */
	fun roles(): Iterator<Role> {
		return ROLES.iterator()
	}
	
	object ServerPlayConnectionEventsJoinListener : ServerPlayConnectionEvents.Join {
		override fun onPlayReady(handler: ServerPlayNetworkHandler, sender: PacketSender, server: MinecraftServer) {
			val players = server.playerManager.playerList.toMutableList()
			players.add(handler.player)
			
			val packet = SyncRolesPacket(ROLES_BY_NAME.mapValues { entry -> entry.value.holders.toList() })
			val buf = BufUtils.toPacketByteBuf(packet)
			
			players.forEach { player ->
				ServerPlayNetworking.send(player, HPNetworking.SYNC_ROLES, PacketByteBufs.duplicate(buf))
			}
		}
	}
}



































