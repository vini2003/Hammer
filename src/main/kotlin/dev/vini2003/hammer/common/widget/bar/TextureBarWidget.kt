package dev.vini2003.hammer.common.widget.bar

import dev.vini2003.hammer.client.scissor.Scissors
import dev.vini2003.hammer.client.texture.Texture
import dev.vini2003.hammer.client.util.Instances
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack

open class TextureBarWidget(
	maximum: () -> Float = { 100.0F },
	current: () -> Float = { 0.0F }
) : BarWidget(maximum, current) {
	override var foregroundTexture: Texture = StandardForegroundTexture
	
	override var backgroundTexture: Texture = StandardBackgroundTexture
	
	override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider, delta: Float) {
		val windowHeight = Instances.client.window.height.toFloat()
		val windowScale = Instances.client.window.scaleFactor.toFloat()
		
		val foregroundWidth = width / maximum() * current()
		val foregroundHeight = height / maximum() * current()

		var area: Scissors?
		
		if (vertical) {
			area = Scissors(provider, x * windowScale, windowHeight - (y + height - foregroundHeight) * windowScale, width * windowScale,(height - foregroundHeight) * windowScale)
			
			backgroundTexture.draw(matrices, provider, x, y, width, height)
			
			area.destroy(provider)
			
			area = Scissors(provider, x * windowScale, windowHeight - (y + height) * windowScale, width * windowScale, foregroundHeight * windowScale)
			
			foregroundTexture.draw(matrices, provider, x, y, width, height)
			
			area.destroy(provider)
		}
		
		if (horizontal) {
			area = Scissors(provider, x * windowScale, windowHeight - (y + height) * windowScale, width * windowScale, height * windowScale)
			
			backgroundTexture.draw(matrices, provider, x, y, width, height)
			
			area.destroy(provider)
			
			area = Scissors(provider, x * windowScale, windowHeight - (y + height) * windowScale, foregroundWidth * windowScale, height * windowScale)
			
			foregroundTexture.draw(matrices, provider, x, y, width, height)
			
			area.destroy(provider)
		}
	}
}