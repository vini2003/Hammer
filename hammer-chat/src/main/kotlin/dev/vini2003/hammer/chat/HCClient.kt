package dev.vini2003.hammer.chat

import dev.vini2003.hammer.chat.registry.client.HCCommands
import dev.vini2003.hammer.chat.registry.client.HCNetworking

object HCClient {
	fun init() {
		HCCommands.init()
		HCNetworking.init()
	}
}