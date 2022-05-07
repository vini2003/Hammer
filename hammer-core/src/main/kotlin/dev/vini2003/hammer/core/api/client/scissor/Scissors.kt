/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 vini2003
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.vini2003.hammer.core.api.client.scissor

import dev.vini2003.hammer.core.api.client.util.InstanceUtils
import net.minecraft.client.render.VertexConsumerProvider
import org.lwjgl.opengl.GL11

/**
 * [Scissors] are an OpenGL scissor area, outside which no content
 * can be rendered.
 */
class Scissors(
	/**
	 * Constructs scissors.
	 *
	 * @param x the X position to use.
	 * @param y the Y position to use.
	 * @param width the width to use.
	 * @param height the height to use.
	 * @param provider the provider to use.
	 * @return the scissors.
	 */
	x: Int,
	y: Int,
	width: Int,
	height: Int,
	val provider: VertexConsumerProvider?
) {
	/**
	 * Constructs scissors.
	 *
	 * @param x the X position to use.
	 * @param y the Y position to use.
	 * @param width the width to use.
	 * @param height the height to use.
	 * @param provider the provider to use.
	 * @return the scissors.
	 */
	constructor(
		x: Float,
		y: Float,
		width: Float,
		height: Float,
		provider: VertexConsumerProvider?
	) : this(x.toInt(), y.toInt(), width.toInt(), height.toInt(), provider)
	
	companion object {
		private const val MAX_SCISSORS = 512
		
		private val SCISSORS = arrayOfNulls<Scissors>(MAX_SCISSORS)
		
		private var LAST_SCISSOR = -1
	}
	
	private var index = 0
	private var left = 0
	private var right = 0
	private var top = 0
	private var bottom = 0
	
	init {
		if (provider is VertexConsumerProvider.Immediate) {
			provider.draw()
		}
		
		LAST_SCISSOR++
		
		if (LAST_SCISSOR < MAX_SCISSORS) {
			index = LAST_SCISSOR
			
			SCISSORS[index] = this
			
			val client = InstanceUtils.CLIENT
			
			if (client != null) {
				val windowHeight = client.window.height.toFloat()
				val windowScale = client.window.scaleFactor.toFloat()
				
				val scaledX = (x * windowScale).toInt()
				val scaledY = (windowHeight - (y + height) * windowScale).toInt()
				
				val scaledWidth = (width * windowScale).toInt()
				val scaledHeight = (height * windowScale).toInt()
				
				left = scaledX
				right = scaledX + scaledWidth - 1
				
				top = scaledY
				bottom = scaledY + scaledHeight - 1
				
				if (index > 0) {
					val parent = SCISSORS[index - 1]
					
					if (left < parent!!.left) left = parent.left
					if (right > parent.right) right = parent.right
					
					if (top < parent.top) top = parent.top
					if (bottom > parent.bottom) bottom = parent.bottom
				}
			}
			
			create()
		}
	}
	
	/**
	 * Enables scissoring.
	 */
	fun create() {
		GL11.glEnable(GL11.GL_SCISSOR_TEST)
		
		GL11.glScissor(
			left,
			top,
			if (right - left + 1 < 0) 0 else right - left + 1,
			if (bottom - top + 1 < 0) 0 else bottom - top + 1
		)
	}
	
	/**
	 * Disables scissoring.
	 */
	fun destroy() {
		if (provider is VertexConsumerProvider.Immediate) {
			provider.draw()
		}
		
		GL11.glDisable(GL11.GL_SCISSOR_TEST)
		
		SCISSORS[index] = null
		
		LAST_SCISSOR--
		
		if (LAST_SCISSOR > -1) {
			SCISSORS[LAST_SCISSOR]!!.create()
		}
	}
}