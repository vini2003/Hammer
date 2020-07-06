package dev.vini2003.hammer.adventure.common.util.extension

import dev.vini2003.hammer.adventure.HA
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.TitlePart
import net.minecraft.server.command.ServerCommandSource

fun ServerCommandSource.audience(): Audience = HA.ADVENTURE.audience(player)

fun ServerCommandSource.sendMessage(component: Component, actionBar: Boolean) {
	if (actionBar) {
		audience().sendActionBar(component)
	} else {
		audience().sendMessage(component)
	}
}

fun ServerCommandSource.sendTitle(component: Component) = audience().sendTitlePart(TitlePart.TITLE, component)

fun ServerCommandSource.sendSubtitle(component: Component) = audience().sendTitlePart(TitlePart.SUBTITLE, component)

fun ServerCommandSource.sendFeedback(component: Component, broadcastToOps: Boolean) = sendFeedback(component.toText(), broadcastToOps)
