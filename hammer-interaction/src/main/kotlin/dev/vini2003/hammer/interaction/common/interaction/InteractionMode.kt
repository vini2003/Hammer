package dev.vini2003.hammer.interaction.common.interaction

import kotlinx.serialization.Serializable

@Serializable
enum class InteractionMode {
	WHITELIST,
	BLACKLIST
}