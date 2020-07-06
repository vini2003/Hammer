package dev.vini2003.hammer.permission.common.util

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
	val userManager
		get() = HP.LUCK_PERMS.userManager
	
	@JvmStatic
	@get:JvmName("getGroupManager")
	val groupManager
		get() = HP.LUCK_PERMS.groupManager
	
	@JvmStatic
	@get:JvmName("getContextManager")
	val contextManager
		get() = HP.LUCK_PERMS.contextManager
	
	@JvmStatic
	@get:JvmName("getStaticQueryOptions")
	val staticQueryOptions
		get() = HP.LUCK_PERMS.contextManager.staticQueryOptions
	
	@JvmStatic
	fun createGroup(groupName: String) = groupManager.createAndLoadGroup(groupName)
	
	@JvmStatic
	fun getOrCreateGroup(groupName: String) = groupManager.getGroup(groupName) ?: groupManager.createAndLoadGroup(groupName).get()!!
	
	@JvmStatic
	fun getUser(player: PlayerEntity) = userManager.getUser(player.uuid)
	
	@JvmStatic
	fun getUser(playerUuid: UUID) = userManager.getUser(playerUuid)
	
	@JvmStatic
	fun saveUser(user: User) = userManager.saveUser(user)
	
	@JvmStatic
	fun saveGroup(group: Group) = groupManager.saveGroup(group)
	
	@JvmStatic
	fun saveUser(uuid: UUID) {
		val user = getUser(uuid)
		
		user?.apply(::saveUser)
	}
	
	@JvmStatic
	fun addNode(player: PlayerEntity, node: Node) = addNode(player.uuid, node)
	
	@JvmStatic
	fun addNode(playerUuid: UUID, node: Node) {
		val user = getUser(playerUuid)
		
		user?.data()?.apply {
			add(node)
		}
		
		user?.apply(::saveUser)
	}
	
	@JvmStatic
	fun removeNode(player: PlayerEntity, node: Node) = removeNode(player.uuid, node)
	
	@JvmStatic
	fun removeNode(playerUuid: UUID, node: Node) {
		val user = getUser(playerUuid)
		
		user?.data()?.apply {
			remove(node)
		}
		
		user?.apply(::saveUser)
	}
	
	@JvmStatic
	fun hasPermission(player: PlayerEntity, permission: String) = hasPermission(player.uuid, permission)
	
	@JvmStatic
	fun hasPermission(playerUuid: UUID, permission: String): Boolean {
		val user = getUser(playerUuid)
		
		if (user != null) {
			return user.cachedData.getPermissionData(staticQueryOptions).checkPermission(permission).asBoolean()
		} else {
			return false
		}
	}
	
	@JvmStatic
	fun setPermission(playerUuid: UUID, permission: String, value: Boolean) {
		val user = getUser(playerUuid)
		
		if (user != null) {
			
			user.data().add(PermissionNode.builder(permission).value(value).build())
			
			userManager.saveUser(user)
		}
	}
}