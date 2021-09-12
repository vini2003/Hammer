package dev.vini2003.blade.client.scissor

import dev.vini2003.blade.common.widget.AbstractWidget
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.VertexConsumerProvider.Immediate
import org.lwjgl.opengl.GL11

class Scissors(provider: VertexConsumerProvider?, x: Int, y: Int, width: Int, height: Int) {
	private var index = 0
	private var left = 0
	private var right = 0
	private var top = 0
	private var bottom = 0

	constructor(provider: VertexConsumerProvider?, element: AbstractWidget) : this(provider, (element.position.x * MinecraftClient.getInstance().window.scaleFactor).toInt(),
			(MinecraftClient.getInstance().window.height - (element.position.y + element.size.height) * MinecraftClient.getInstance().window.scaleFactor).toInt(),
			(element.size.width * MinecraftClient.getInstance().window.scaleFactor).toInt(),
			(element.size.height * MinecraftClient.getInstance().window.scaleFactor).toInt())

	private fun resume() {
		GL11.glEnable(GL11.GL_SCISSOR_TEST)
		
		glScissor(left, top, right - left + 1, bottom - top + 1)
	}

	fun destroy(provider: VertexConsumerProvider?) {
		if (provider is Immediate) {
			provider.draw()
		}
		
		GL11.glDisable(GL11.GL_SCISSOR_TEST)
		
		objects[index] = null
		lastObject--
		
		if (lastObject > -1) {
			objects[lastObject]!!.resume()
		}
	}

	companion object {
		private const val max = 512
		
		private val objects = arrayOfNulls<Scissors>(max)
		private var lastObject = -1
		
		private fun glScissor(x: Int, y: Int, width: Int, height: Int) {
			var width = width
			var height = height
			
			if (width < 0) width = 0
			if (height < 0) height = 0
			
			GL11.glScissor(x, y, width, height)
		}
	}

	init {
		if (provider is Immediate) {
			provider.draw()
		}
		
		lastObject++
		
		if (lastObject < max) {
			index = lastObject
			objects[index] = this
			left = x
			right = x + width - 1
			top = y
			bottom = y + height - 1
			
			if (index > 0) {
				val parent = objects[index - 1]
				if (left < parent!!.left) left = parent.left
				if (right > parent.right) right = parent.right
				if (top < parent.top) top = parent.top
				if (bottom > parent.bottom) bottom = parent.bottom
			}
			
			resume()
		}
	}
}