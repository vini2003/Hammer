package dev.vini2003.hammer.interaction

import dev.vini2003.hammer.interaction.registry.client.HINetworking

object HIClient {
	fun init() {
		HINetworking.init()
	}
}