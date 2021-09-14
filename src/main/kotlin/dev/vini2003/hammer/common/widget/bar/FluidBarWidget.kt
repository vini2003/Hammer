@file:Suppress("DEPRECATION", "UnstableApiUsage")

package dev.vini2003.hammer.common.widget.bar

import dev.vini2003.hammer.client.scissor.Scissors
import dev.vini2003.hammer.client.texture.Texture
import dev.vini2003.hammer.client.util.Instances
import dev.vini2003.hammer.common.util.Texts
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import kotlin.math.max
import kotlin.math.min
import kotlin.properties.Delegates


open class FluidBarWidget(
	maximum: () -> Float = { 1.0F },
	current: () -> Float = { 0.0F }
) : BarWidget(maximum, current) {
	constructor(storage: StorageView<FluidVariant>) : this(
		{ storage.amount.toFloat() },
		{ storage.capacity.toFloat() }
	) {
		this.fluidVariant = storage.resource
	}
	
	constructor(maximum: () -> Float, current: () -> Float, fluidVariant: FluidVariant) : this(
		maximum,
		current
	) {
		this.fluidVariant = fluidVariant
	}
	
	var fluidVariant: FluidVariant by Delegates.observable(FluidVariant.blank()) { _, _, newValue ->
		this.foregroundTexture = Texture.of(newValue)
	}
	
	var fluidView: StorageView<FluidVariant>? by Delegates.observable(null) {_, _, newValue ->
		if (newValue == null) {
			this.maximum = { 1.0F }
			this.current = { 0.0F }
		} else {
			this.maximum = { newValue.capacity.toFloat() }
			this.current = { newValue.amount.toFloat() }
		}
	}
	
	var interpolatedCurrent = current()
	
	override var foregroundTexture: Texture = Texture.of(fluidVariant)
	
	override var backgroundTexture: Texture = StandardBackgroundTexture

	override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider, delta: Float) {
		val windowHeight = Instances.client.window.height.toFloat()
		val windowScale = Instances.client.window.scaleFactor.toFloat()

		if (interpolatedCurrent < current()) {
			interpolatedCurrent += 0.05F * delta
			interpolatedCurrent = min(interpolatedCurrent, current())
		} else {
			interpolatedCurrent -= 0.05F * delta
			interpolatedCurrent = max(interpolatedCurrent, current())
		}
		
		val foregroundWidth = width / maximum() * interpolatedCurrent
		val foregroundHeight = height / maximum() * interpolatedCurrent
		
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
			listOf(Texts.Empty)
		} else {
			if (fluidView == null) {
				Texts.tooltip(fluidVariant) + Texts.percentage(current(), maximum())
			} else {
				Texts.tooltip(fluidVariant) + Texts.fluidView(fluidView!!)
			}
		}
	}
}