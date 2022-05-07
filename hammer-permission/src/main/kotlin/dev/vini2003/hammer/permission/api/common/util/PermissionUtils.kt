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

import dev.vini2003.hammer.permission.HP
import net.luckperms.api.model.group.Group
import net.luckperms.api.model.user.User
import net.luckperms.api.node.Node
import net.luckperms.api.node.types.PermissionNode
import net.minecraft.entity.player.PlayerEntity
import java.util.*

object PermissionUtils {
	@JvmStatic
	@get:JvmName("getUserManager")
	val USER_MANAGER
		get() = HP.LUCK_PERMS.userManager
	
	@JvmStatic
	@get:JvmName("getGroupManager")
	val GROUP_MANAGER
		get() = HP.LUCK_PERMS.groupManager
	
	@JvmStatic
	@get:JvmName("getContextManager")
	val CONTEXT_MANAGER
		get() = HP.LUCK_PERMS.contextManager
	
	@JvmStatic
	@get:JvmName("getStaticQueryOptions")
	val STATIC_QUERY_OPTIONS
		get() = HP.LUCK_PERMS.contextManager.staticQueryOptions
	
	@JvmStatic
	fun createGroup(groupName: String) = GROUP_MANAGER.createAndLoadGroup(groupName)
	
	@JvmStatic
	fun getOrCreateGroup(groupName: String) = GROUP_MANAGER.getGroup(groupName) ?: GROUP_MANAGER.createAndLoadGroup(groupName).get()!!
	
	@JvmStatic
	fun getUser(player: PlayerEntity) = USER_MANAGER.getUser(player.uuid)
	
	@JvmStatic
	fun getUser(playerUuid: UUID) = USER_MANAGER.getUser(playerUuid)
	
	@JvmStatic
	fun saveUser(user: User) = USER_MANAGER.saveUser(user)
	
	@JvmStatic
	fun saveGroup(group: Group) = GROUP_MANAGER.saveGroup(group)
	
	@JvmStatic
	fun saveUser(uuid: UUID) {
		val user = getUser(uuid)
		
		if (user != null) {
			saveUser(user)
		}
	}
	
	@JvmStatic
	fun addNode(player: PlayerEntity, node: Node) = addNode(player.uuid, node)
	
	@JvmStatic
	fun addNode(playerUuid: UUID, node: Node) {
		val user = getUser(playerUuid)
		
		if (user != null) {
			val userData = user.data()
			
			userData.add(node)
			
			saveUser(user)
		}
	}
	
	@JvmStatic
	fun removeNode(player: PlayerEntity, node: Node) = removeNode(player.uuid, node)
	
	@JvmStatic
	fun removeNode(playerUuid: UUID, node: Node) {
		val user = getUser(playerUuid)
		
		if (user != null) {
			val userData = user.data()
			
			userData.remove(node)
			
			saveUser(user)
		}
	}
	
	@JvmStatic
	fun hasPermission(player: PlayerEntity, permission: String) = hasPermission(player.uuid, permission)
	
	@JvmStatic
	fun hasPermission(playerUuid: UUID, permission: String): Boolean {
		val user = getUser(playerUuid)
		
		if (user != null) {
			return user.cachedData.getPermissionData(STATIC_QUERY_OPTIONS).checkPermission(permission).asBoolean()
		} else {
			return false
		}
	}
	
	@JvmStatic
	fun setPermission(playerUuid: UUID, permission: String, value: Boolean) {
		val user = getUser(playerUuid)
		
		if (user != null) {
			user.data().add(PermissionNode.builder(permission).value(value).build())
			
			USER_MANAGER.saveUser(user)
		}
	}
}