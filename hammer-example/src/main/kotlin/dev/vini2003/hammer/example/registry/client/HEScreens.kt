package dev.vini2003.hammer.example.registry.client

import dev.vini2003.hammer.core.HC
import dev.vini2003.hammer.core.api.client.texture.ImageTexture
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
				type = HudBarWidget.Type.CONTINUOS,
				side = HudBarWidget.Side.LEFT,
				maximum = { 100.0F },
				current = { client.player?.age?.toFloat()!! % 100.0F },
			)
			
			leftBar.horizontal = true
			
			leftBar.show = { client.player?.isCreative == false && client.player?.isSneaking == false }
			leftBar.smooth = false
			leftBar.invert = false
			
			leftBar.foregroundTexture = ImageTexture(HC.id("textures/widget/oxygen_foreground.png"))
			leftBar.backgroundTexture = ImageTexture(HC.id("textures/widget/oxygen_background.png"))
			
			collection.add(leftBar)
			
			var rightBar = HudBarWidget(
				type = HudBarWidget.Type.CONTINUOS,
				side = HudBarWidget.Side.RIGHT,
				maximum = { 100.0F },
				current = { client.player?.age?.toFloat()!! % 100.0F },
			)
			
			rightBar.show = { client.player?.isCreative == false && client.player?.isSneaking == false }
			
			rightBar.horizontal = true
			rightBar.smooth = false
			rightBar.invert = true
			
			rightBar.foregroundTexture = ImageTexture(HC.id("textures/widget/oxygen_foreground.png"))
			rightBar.backgroundTexture = ImageTexture(HC.id("textures/widget/oxygen_background.png"))
			
			collection.add(rightBar)
		}
	}
}