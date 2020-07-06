package dev.vini2003.hammer.permission.common.util

import dev.vini2003.hammer.permission.HP
import net.kyori.adventure.platform.fabric.impl.accessor.ComponentSerializerAccess
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.minecraft.network.MessageType
import net.minecraft.scoreboard.Team
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import java.util.*

object ChatUtils {
	@JvmStatic
	fun formatColoredValueToComponent(value: String): Component {
		return LegacyComponentSerializer.legacyAmpersand().deserialize(value).toBuilder().build()
	}
	
	@JvmStatic
	fun convertComponentToText(component: Component): MutableText {
		return Text.Serializer.fromJson(ComponentSerializerAccess.getGSON().toJsonTree(component))!!
	}
	
	@JvmStatic
	fun formatColoredValueText(string: String): MutableText {
		return convertComponentToText(formatColoredValueToComponent(string))
	}
	
	@JvmStatic
	fun formatWithLuckPerms(player: ServerPlayerEntity, old: Text, type: MessageType, sender: UUID): Text {
		if (type != MessageType.CHAT) return old
		
		val senderPlayer = player.server.playerManager.getPlayer(sender)!!
		
		old as TranslatableText
		
		try {
			val user = HP.LUCK_PERMS.userManager.getUser(sender)!!
			
			val group = HP.LUCK_PERMS.groupManager.getGroup(user.primaryGroup)!!
			
			val groupPrefix = group.cachedData.metaData.prefix
			val groupSuffix = group.cachedData.metaData.suffix
			
			val prefix = user.cachedData.metaData.prefix
			val suffix = user.cachedData.metaData.suffix
			
			var result = ""
			
			val color = ((senderPlayer.scoreboardTeam) as Team?)?.color
			
			if (prefix != null) {
				result += "$prefix "
				
				if (color != null) {
					result += "$color${senderPlayer.name.asString()}"
				} else {
					result += senderPlayer.name.asString()
				}
				if (suffix == null && groupSuffix == null) result += "&7"
			} else if (groupPrefix != null) {
				result += "$groupPrefix "
				
				if (color != null) {
					result += "$color${senderPlayer.name.asString()}"
				} else {
					result += senderPlayer.name.asString()
				}
				if (suffix == null && groupSuffix == null) result += "&7"
			} else {
				if (color != null) {
					result += "$color${senderPlayer.name.asString()}"
				} else {
					result += senderPlayer.name.asString()
				}
				
				result += "&7"
			}
			
			if (suffix != null) {
				result += " $suffix"
				result += " » "
				result += old.args[1] as String
			} else if (groupSuffix != null) {
				result += " $groupSuffix"
				result += " » "
				result += old.args[1] as String
			} else {
				result += " » "
				result += old.args[1] as String
			}
			
			return formatColoredValueText(result)
		} catch (ignored: Exception) {
		}
		
		return old
	}
}