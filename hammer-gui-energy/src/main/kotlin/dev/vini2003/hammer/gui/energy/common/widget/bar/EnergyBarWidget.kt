@file:Suppress("DEPRECATION", "UnstableApiUsage")

package dev.vini2003.hammer.gui.energy.common.widget.bar

import dev.vini2003.hammer.gui.energy.common.util.EnergyTextUtils
import dev.vini2003.hammer.H
import dev.vini2003.hammer.gui.common.widget.bar.BarWidget
import dev.vini2003.hammer.client.scissor.Scissors
import dev.vini2003.hammer.client.texture.SpriteTexture
import dev.vini2003.hammer.client.texture.Texture
import dev.vini2003.hammer.client.util.InstanceUtils
import dev.vini2003.hammer.common.util.TextUtils
import dev.vini2003.hammer.gui.registry.client.HGUIKeyBinds
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import team.reborn.energy.api.EnergyStorage

open class EnergyBarWidget(
	maximum: () -> Long = { 1L},
	current: () -> Long = { 0L}
) : BarWidget({ maximum().toFloat() }, { current().toFloat() }) {
	companion object {
		@JvmField
		val STANDARD_FILLED_TEXTURE = SpriteTexture(H.id("textures/widget/energy_bar_filled.png"))
		
		@JvmField
		val STANDARD_EMPTY_TEXTURE = SpriteTexture(H.id("textures/widget/energy_bar_empty.png"))
	}
	
	constructor(storage: EnergyStorage) : this(
		{ storage.amount },
		{ storage.capacity }
	)
	
	override var foregroundTexture: Texture = STANDARD_FILLED_TEXTURE
	
	override var backgroundTexture: Texture = STANDARD_EMPTY_TEXTURE

	override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider, delta: Float) {
		val windowHeight = InstanceUtils.CLIENT.window.height.toFloat()
		val windowScale = InstanceUtils.CLIENT.window.scaleFactor.toFloat()
		
		val foregroundHeight = height / maximum() * current()
		
		val area: Scissors?
		
		backgroundTexture.draw(matrices, provider, x, y, width, height)
		
		area = Scissors(provider, (x * windowScale).toInt(), (windowHeight - (y + height) * windowScale).toInt(), (width * windowScale).toInt(), (foregroundHeight * windowScale).toInt())
		
		foregroundTexture.draw(matrices, provider, x + 1, y + 1, width - 2, height - 2)
		
		area.destroy(provider)
	}
	
	override fun getTooltip(): List<Text> {
		return if (current() == 0.0F) {
			listOf(TextUtils.EMPTY)
		} else {
			if (HGUIKeyBinds.SHOW_DETAILED_VALUES.isPressed) {
				return EnergyTextUtils.detailedTooltip(current(), maximum())
			} else {
				return EnergyTextUtils.shortenedTooltip(current(), maximum())
			}
		}
	}
}