package dev.vini2003.hammer.chat.registry.client

import dev.vini2003.hammer.chat.client.listener.RenderHudWarningsCallback
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback

object HCEvents {
	fun init() {
		HudRenderCallback.EVENT.register(RenderHudWarningsCallback)
	}
}