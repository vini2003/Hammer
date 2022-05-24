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

package dev.vini2003.hammer.permission.api.common.role

import dev.vini2003.hammer.permission.impl.packet.add.AddRolePacket
import dev.vini2003.hammer.permission.impl.packet.remove.RemoveRolePacket
import dev.vini2003.hammer.permission.api.common.util.PermissionUtils
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
	
	fun isIn(player: PlayerEntity): Boolean {
		return PermissionUtils.hasPermission(player, inheritanceNode.key)
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