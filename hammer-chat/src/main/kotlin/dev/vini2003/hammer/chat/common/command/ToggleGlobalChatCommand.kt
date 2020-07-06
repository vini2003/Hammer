package dev.vini2003.hammer.chat.common.command

import dev.vini2003.hammer.chat.common.packet.ToggleGlobalChatPacket
import dev.vini2003.hammer.chat.registry.common.HCNetworking
import dev.vini2003.hammer.chat.registry.common.HCValues
import dev.vini2003.hammer.command.common.command.ServerRootCommand
import dev.vini2003.hammer.command.common.util.extension.command
import dev.vini2003.hammer.command.common.util.extension.execute
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.text.TranslatableText

val TOGGLE_GLOBAL_CHAT_COMMAND = ServerRootCommand {
	command("toggle_global_chat") {
		execute {
			HCValues.SHOW_GLOBAL_CHAT = !HCValues.SHOW_GLOBAL_CHAT
			
			source.sendFeedback(TranslatableText("command.hammer.toggle_global_chat", if (HCValues.SHOW_GLOBAL_CHAT) "enabled" else "disabled"), true)
			
			val packet = ToggleGlobalChatPacket(HCValues.SHOW_GLOBAL_CHAT)
			val buf = packet.toPacketByteBuf()
			
			source.server.playerManager.playerList.forEach { player ->
				ServerPlayNetworking.send(player, HCNetworking.TOGGLE_GLOBAL_CHAT, PacketByteBufs.duplicate(buf))
			}
		}
	}
}