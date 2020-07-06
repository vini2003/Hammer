package dev.vini2003.hammer.adventure.common.util

import net.kyori.adventure.platform.fabric.FabricServerAudiences
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.minecraft.server.MinecraftServer
import net.minecraft.text.MutableText
import net.minecraft.text.Text

object AdventureUtils {
	@JvmStatic
	fun createAudience(server: MinecraftServer): FabricServerAudiences {
		return FabricServerAudiences.of(server)
	}
	
	@JvmStatic
	fun formatWithColorValuesToComponent(value: String): Component {
		return LegacyComponentSerializer.legacyAmpersand().deserialize(value).toBuilder().build()
	}
	
	@JvmStatic
	fun formatWithColorValuesToText(string: String): MutableText {
		return convertComponentToText(formatWithColorValuesToComponent(string))
	}
	
	@JvmStatic
	fun convertComponentToText(component: Component): MutableText {
		return Text.Serializer.fromJson(GsonComponentSerializer.gson().serializeToTree(component))!!
	}
	
	@JvmStatic
	fun convertTextToComponent(text: Text): Component {
		return GsonComponentSerializer.gson().deserializeFromTree(Text.Serializer.toJsonTree(text))
	}
}