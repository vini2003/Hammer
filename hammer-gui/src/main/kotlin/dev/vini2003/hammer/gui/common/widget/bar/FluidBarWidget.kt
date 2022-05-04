@file:Suppress("DEPRECATION", "UnstableApiUsage")

package dev.vini2003.hammer.gui.common.widget.bar

import dev.vini2003.hammer.client.scissor.Scissors
import dev.vini2003.hammer.client.texture.Texture
import dev.vini2003.hammer.client.util.InstanceUtils
import dev.vini2003.hammer.common.util.FluidTextUtils
import dev.vini2003.hammer.common.util.TextUtils
import dev.vini2003.hammer.gui.registry.client.HGUIKeyBinds
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text


open class FluidBarWidget(
	maximum: () -> Float = { 1.0F },
	current: () -> Float = { 0.0F }
) : BarWidget(maximum, current) {
	var storage: SingleSlotStorage<FluidVariant>? = null
		set(value) {
			current = { value!!.amount.toFloat() }
			maximum = { value!!.capacity.toFloat() }
			
			field = value
		}
	
	override var foregroundTexture: Texture = STANDARD_FOREGROUND_TEXTURE
	
	override var backgroundTexture: Texture = STANDARD_BACKGROUND_TEXTURE

	override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider, delta: Float) {
		foregroundTexture = Texture.of(storage!!.resource)
		
		val windowHeight = InstanceUtils.CLIENT.window.height.toFloat()
		val windowScale = InstanceUtils.CLIENT.window.scaleFactor.toFloat()
		
		val foregroundWidth = width / maximum() * current()
		val foregroundHeight = height / maximum() * current()
		
		var area: Scissors?
		
		if (vertical) {
			backgroundTexture.draw(matrices, provider, x, y, width, height)
			
			area = Scissors(provider, (x * windowScale).toInt(), (windowHeight - (y + height) * windowScale).toInt(), (width * windowScale).toInt(), (foregroundHeight * windowScale).toInt())
			
			foregroundTexture.draw(matrices, provider, x + 1, y + 1, width - 2, height - 2)
			
			area.destroy(provider)
		}
		
		if (horizontal) {
			backgroundTexture.draw(matrices, provider, x, y, width, height)
			
			area = Scissors(provider, x * windowScale, windowHeight - (y + height) * windowScale, foregroundWidth * windowScale, height * windowScale)
			
			foregroundTexture.draw(matrices, provider, x + 1, y + 1, width - 2, height - 2)
			
			area.destroy(provider)
		}
	}
	
	override fun getTooltip(): List<Text> {
		return if (current() == 0.0F) {
			listOf(TextUtils.EMPTY)
		} else {
			if (HGUIKeyBinds.SHOW_DETAILED_VALUES.isPressed) {
				FluidTextUtils.variantTooltip(storage!!.resource) + FluidTextUtils.detailedTooltip(storage!!)
			} else {
				FluidTextUtils.variantTooltip(storage!!.resource) + FluidTextUtils.shortenedTooltip(storage!!)
			}
		}
	}
}