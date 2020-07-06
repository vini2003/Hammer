package dev.vini2003.hammer.permission.common.manager

import dev.vini2003.hammer.common.util.BufUtils
import dev.vini2003.hammer.permission.common.packet.sync.SyncRolesPacket
import dev.vini2003.hammer.permission.common.role.Role
import dev.vini2003.hammer.permission.common.util.extension.hasRole
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
	
	fun register(role: Role) {
		ROLES.add(role)
		
		ROLES_BY_NAME[role.name] = role
	}
	
	fun getRoleByName(name: String): Role? {
		return ROLES_BY_NAME[name]
	}
	
	@JvmStatic
	fun getRolePrefix(player: PlayerEntity): String? {
		return ROLES.filter { role ->
			player.hasRole(role)
		}.maxByOrNull {
			role -> role.prefixWeight ?: 0
		}?.prefix
	}
	
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



































