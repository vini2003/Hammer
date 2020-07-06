package dev.vini2003.hammer.chat.registry.common

import dev.vini2003.hammer.chat.listener.SyncGlobalChatListener
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents

object HCEvents {
	fun init() {
		ServerPlayConnectionEvents.JOIN.register(SyncGlobalChatListener)
	}
}