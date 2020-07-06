package dev.vini2003.hammer.adventure.common.util.extension

import dev.vini2003.hammer.adventure.HA
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.TitlePart
import net.minecraft.server.network.ServerPlayerEntity

fun ServerPlayerEntity.audience() = HA.ADVENTURE.audience(this)

fun ServerPlayerEntity.sendMessage(component: Component, actionBar: Boolean) {
	if (actionBar) {
		audience().sendActionBar(component)
	} else {
		audience().sendMessage(component)
	}
}

fun ServerPlayerEntity.sendTitle(component: Component) = audience().sendTitlePart(TitlePart.TITLE, component)

fun ServerPlayerEntity.sendSubtitle(component: Component) = audience().sendTitlePart(TitlePart.SUBTITLE, component)
