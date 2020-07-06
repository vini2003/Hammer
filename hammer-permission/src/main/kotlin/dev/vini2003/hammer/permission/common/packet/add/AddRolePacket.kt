package dev.vini2003.hammer.permission.common.packet.add

import dev.vini2003.hammer.common.packet.Packet
import dev.vini2003.hammer.common.util.serializer.UUIDSerializer
import dev.vini2003.hammer.permission.common.manager.RoleManager
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class AddRolePacket(val name: String, val uuid: @Serializable(with = UUIDSerializer::class) UUID) : Packet<AddRolePacket>() {
	override fun receive(context: ClientContext) {
		RoleManager.getRoleByName(name)?.addToClient(uuid)
	}
}