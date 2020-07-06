package dev.vini2003.hammer.adventure.common.util.extension

import dev.vini2003.hammer.adventure.HA
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.TitlePart
import net.minecraft.server.world.ServerWorld

fun ServerWorld.audience() = HA.ADVENTURE.audience(players)

fun ServerWorld.sendMessage(message: Component, actionBar: Boolean) {
	if (actionBar) {
		audience().sendActionBar(message)
	} else {
		audience().sendMessage(message)
	}
}

fun ServerWorld.sendTitle(component: Component) = audience().sendTitlePart(TitlePart.TITLE, component)

fun ServerWorld.sendSubtitle(component: Component) = audience().sendTitlePart(TitlePart.SUBTITLE, component)