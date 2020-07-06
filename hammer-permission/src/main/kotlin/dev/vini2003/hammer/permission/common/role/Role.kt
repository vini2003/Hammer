package dev.vini2003.hammer.permission.common.role

import dev.vini2003.hammer.common.util.BufUtils
import dev.vini2003.hammer.permission.common.packet.add.AddRolePacket
import dev.vini2003.hammer.permission.common.packet.remove.RemoveRolePacket
import dev.vini2003.hammer.permission.common.util.PermissionUtils
import dev.vini2003.hammer.permission.registry.common.HPNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.luckperms.api.model.group.Group
import net.luckperms.api.node.types.InheritanceNode
import net.luckperms.api.node.types.PermissionNode
import net.luckperms.api.node.types.PrefixNode
import net.minecraft.entity.player.PlayerEntity
import java.util.*

data class Role(
	val name: String,
	val prefix: String? = null,
	val prefixWeight: Int? = null,
	val prefixColor: Int? = null
) {
	val inheritanceNode: InheritanceNode by lazy { InheritanceNode.builder(group).build() }
	val permissionNode: PermissionNode by lazy { PermissionNode.builder("hammer.${name}").value(true).build() }
	val prefixNode: PrefixNode by lazy { PrefixNode.builder(prefix!!, prefixWeight!!).build() }
	
	val group: Group by lazy { PermissionUtils.getOrCreateGroup(name) }
	
	val holders: MutableSet<UUID> = mutableSetOf()
	
	private var initialized: Boolean = false
	
	private fun <T> lazy(block: () -> T): Lazy<T> {
		return kotlin.lazy {
			if (!initialized) {
				initialized = true
				
				val data = group.data()
				
				if (prefix != null) {
					data.add(prefixNode)
				}
				
				data.add(permissionNode)
				data.add(inheritanceNode)
				
				PermissionUtils.saveGroup(group)
			}
			
			block()
		}
	}
	
	fun addToClient(uuid: UUID) = holders.add(uuid)
	
	fun addTo(player: PlayerEntity) {
		if (!player.world.isClient) {
			PermissionUtils.addNode(player.uuid, inheritanceNode)
			
			val packet = AddRolePacket(name, player.uuid)
			val buf = BufUtils.toPacketByteBuf(packet)
			
			player.server?.playerManager?.playerList?.forEach { otherPlayer ->
				ServerPlayNetworking.send(otherPlayer, HPNetworking.ADD_ROLE, PacketByteBufs.duplicate(buf))
			}
		} else {
			addToClient(player.uuid)
		}
	}
	
	fun removeFromClient(uuid: UUID) = holders.remove(uuid)
	
	fun removeFrom(player: PlayerEntity) {
		if (!player.world.isClient) {
			PermissionUtils.removeNode(player.uuid, inheritanceNode)
			
			val packet = RemoveRolePacket(name, player.uuid)
			val buf = BufUtils.toPacketByteBuf(packet)
			
			player.server?.playerManager?.playerList?.forEach { otherPlayer ->
				ServerPlayNetworking.send(otherPlayer, HPNetworking.REMOVE_ROLE, PacketByteBufs.duplicate(buf))
			}
		} else {
			removeFromClient(player.uuid)
		}
	}
}