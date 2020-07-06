package dev.vini2003.hammer.permission.common.packet.sync

import dev.vini2003.hammer.common.packet.Packet
import dev.vini2003.hammer.common.util.serializer.UUIDSerializer
import dev.vini2003.hammer.permission.common.manager.RoleManager
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class SyncRolesPacket(val roles: Map<String, List<@Serializable(with = UUIDSerializer::class) UUID>>) : Packet<SyncRolesPacket>() {
	override fun receive(context: ClientContext) {
		roles.forEach { (name, holders) ->
			val role = RoleManager.getRoleByName(name)
			
			if (role != null) {
				role.holders.clear()
				
				holders.forEach { holder -> role.holders.add(holder) }
			}
		}
	}
}