package dev.vini2003.hammer.example.registry.client

import dev.vini2003.hammer.core.HC
import dev.vini2003.hammer.core.api.client.texture.TiledImageTexture
import dev.vini2003.hammer.core.api.client.util.DrawingUtils.drawTexturedQuad
import dev.vini2003.hammer.core.api.client.util.InstanceUtils
import dev.vini2003.hammer.core.api.common.math.position.Position
import dev.vini2003.hammer.example.impl.client.screen.handled.DebugHandledScreen
import dev.vini2003.hammer.example.registry.common.HEScreenHandlers
import dev.vini2003.hammer.gui.api.client.event.InGameHudEvents
import dev.vini2003.hammer.gui.api.client.util.extension.getHungerBarPos
import dev.vini2003.hammer.gui.api.common.widget.bar.HudBarWidget
import net.fabricmc.fabric.api.renderer.v1.model.SpriteFinder
import net.minecraft.client.gui.screen.ingame.HandledScreens
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.util.Identifier
import kotlin.math.ceil

object HEScreens {
	fun init() {
		HandledScreens.register(HEScreenHandlers.DEBUG, ::DebugHandledScreen)
		
		InGameHudEvents.INIT.register { hud, collection ->
			val client = InstanceUtils.CLIENT ?: return@register
			
			var leftBar = HudBarWidget(
				maximum = { 100.0F },
				current = { client.player?.age?.toFloat()!! % 100.0F },
				side = HudBarWidget.Side.LEFT,
				show = { client.player?.isSneaking == false }
			)
			
			leftBar.horizontal = true
			
			leftBar.foregroundTexture = TiledImageTexture(HC.id("textures/widget/oxygen_foreground.png"), 9.0F, 9.0F, maxTilesX = { 10 - (ceil(((leftBar.current() / leftBar.maximum())) * 10.0F).toInt()) }, shiftOnTileX = -1.0F)
			leftBar.backgroundTexture = TiledImageTexture(HC.id("textures/widget/oxygen_background.png"), 9.0F, 9.0F, maxTilesX = { 10 }, shiftOnTileX = -1.0F)
			
			collection.add(leftBar)
			
			var rightBar = HudBarWidget(
				maximum = { 100.0F },
				current = { client.player?.age?.toFloat()!! % 100.0F },
				side = HudBarWidget.Side.RIGHT,
				show = { client.player?.isSneaking == false }
			)
			
			rightBar.horizontal = true
			
			rightBar.foregroundTexture = TiledImageTexture(HC.id("textures/widget/oxygen_foreground.png"), 9.0F, 9.0F, maxTilesX = { 10 - (ceil(((rightBar.current() / rightBar.maximum())) * 10.0F).toInt()) }, shiftOnTileX = -1.0F)
			rightBar.backgroundTexture = TiledImageTexture(HC.id("textures/widget/oxygen_background.png"), 9.0F, 9.0F, maxTilesX = { 10 }, shiftOnTileX = -1.0F)
			
			collection.add(rightBar)
		}
	}
}