package dev.vini2003.blade.common.widget.bar

import dev.vini2003.blade.BL
import dev.vini2003.blade.client.texture.PartitionedTexture
import dev.vini2003.blade.client.util.Instances
import dev.vini2003.blade.client.scissor.Scissors
import dev.vini2003.blade.client.texture.FluidTexture
import dev.vini2003.blade.client.texture.Texture
import dev.vini2003.blade.common.util.extension.toTranslatableText
import dev.vini2003.blade.common.widget.AbstractWidget
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.fluid.Fluids
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import kotlin.properties.Delegates

open class HorizontalFluidBarWidget(
	var maximum: () -> Float = {100F},
	var current: () -> Float = {0F},
	backgroundId: Identifier = BL.id("textures/widget/bar_background.png")
) : AbstractWidget() {
	var fluidVariant: FluidVariant by Delegates.observable(FluidVariant.blank()) { property, oldValue, newValue ->
		foregroundTexture = Texture.of(newValue)
	}
	
	var foregroundTexture: Texture = Texture.of(
		fluidVariant
	)
	
	var backgroundTexture: Texture = Texture.of(
		backgroundId,
		18F,
		18F,
		0.05555555555555555556F,
		0.05555555555555555556F,
		0.05555555555555555556F,
		0.05555555555555555556F
	)

	override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider) {
		if (hidden) return
		
		val rawHeight = Instances.client.window.height.toFloat()
		val scale = Instances.client.window.scaleFactor.toFloat()

		val sBGX = width / maximum() * current()

		var area: Scissors?

		area = Scissors(provider, (x * scale).toInt(), (rawHeight - (y + height) * scale).toInt(), (width * scale).toInt(), (height * scale).toInt())

		backgroundTexture.draw(matrices, provider, x, y, width, height)

		area.destroy(provider)

		area = Scissors(provider, (x * scale).toInt(), (rawHeight - (y + height) * scale).toInt(), (sBGX * scale).toInt(), (height * scale).toInt())
		
		foregroundTexture.draw(matrices, provider, x, y, width, height)

		area.destroy(provider)
	}
	
	override fun getTooltip(): List<Text> {
		return if (current() == 0.0F) {
			listOf("text.blade.empty".toTranslatableText())
		} else {
			FluidVariantRendering.getTooltip(fluidVariant)
		}
	}
}