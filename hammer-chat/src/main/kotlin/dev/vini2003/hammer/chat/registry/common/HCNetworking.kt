package dev.vini2003.hammer.chat.registry.common

import dev.vini2003.hammer.H

object HCNetworking {
	fun init() = Unit
	
	@JvmField
	val TOGGLE_CHAT = H.id("toggle_chat")
	@JvmField
	val TOGGLE_GLOBAL_CHAT = H.id("toggle_global_chat")
	@JvmField
	val TOGGLE_FEEDBACK = H.id("toggle_feedback")
	@JvmField
	val TOGGLE_WARNINGS = H.id("toggle_warnings")
}