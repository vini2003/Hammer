package dev.vini2003.hammer

import kotlinx.serialization.json.Json
import kotlinx.serialization.protobuf.ProtoBuf
import net.minecraft.util.Identifier

object H {
	@SuppressWarnings
	const val ID = "hammer"
	
	@JvmStatic
	@get:JvmName("getJson")
	val JSON = Json { ignoreUnknownKeys = true; prettyPrint = true }
	
	@JvmStatic
	@get:JvmName("getProtoBuf")
	val PROTOBUF = ProtoBuf { }
	
	@JvmStatic
	fun id(string: String): Identifier {
		return Identifier(ID, string)
	}
}