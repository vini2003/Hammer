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

package dev.vini2003.hammer.gui.api.common.widget.bar

import dev.vini2003.hammer.core.api.client.scissor.Scissors
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.texture.Sprite
import net.minecraft.client.util.math.MatrixStack

/**
 * A [SpriteBarWidget] is a [BaseBarWidget] whose data is located through a sprite and
 * [maximum] and [current] are configurable.
 */
open class SpriteBarWidget @JvmOverloads constructor(
	maximum: () -> Float = { 100.0F },
	current: () -> Float = { 0.0F },
	var stepWidth: Float = -1.0F,
	var stepHeight: Float = -1.0F
) : BaseBarWidget(maximum, current) {
	var smooth: Boolean = false
	
	var invert: Boolean = false
	
	var foregroundSprite: Sprite? = null
		get() = field
		set(value) {
			value ?: return
			
			foregroundTexture = TiledSpriteTexture(value)
			
			field = value
		}
	
	var backgroundSprite: Sprite? = null
		get() = field
		set(value) {
			value ?: return
			
			backgroundTexture = TiledSpriteTexture(value)
			
			field = value
		}
	
	override var foregroundTexture: BaseTexture = STANDARD_FOREGROUND_TEXTURE
	
	override var backgroundTexture: BaseTexture = STANDARD_BACKGROUND_TEXTURE
	
	override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider, tickDelta: Float) {
		var foregroundWidth = width / maximum() * current()
		var foregroundHeight = height / maximum() * current()
		
		if (stepWidth != 1.0F) {
			foregroundWidth -= foregroundWidth % stepWidth
		}
		
		if (stepHeight != 1.0F) {
			foregroundHeight -= foregroundHeight % stepHeight
		}
		
		if (!smooth) {
			foregroundWidth = foregroundWidth.toInt().toFloat()
			foregroundHeight = foregroundHeight.toInt().toFloat()
		}
		
		lateinit var scissors: Scissors
		
		if (vertical) {
			backgroundTexture.draw(matrices, provider, x, y, width, height)
			
			if (stepHeight == -1.0F) {
				if (!invert) {
					scissors = Scissors(x, y + (height - foregroundHeight), width, foregroundHeight, provider)
				} else {
					scissors = Scissors(x, y, width, foregroundHeight, provider)
				}
			}
			
			if (!invert) {
				foregroundTexture.draw(matrices, provider, x, y, width, height)
			} else {
				foregroundTexture.draw(matrices, provider, x, y + (height - foregroundHeight), width, height)
			}
			
			if (stepHeight == -1.0F) {
				scissors.destroy()
			}
		}
		
		if (horizontal) {
			backgroundTexture.draw(matrices, provider, x, y, width, height)
			
			if (stepWidth == -1.0F) {
				if (!invert) {
					scissors = Scissors(x, y, foregroundWidth, height, provider)
				} else {
					scissors = Scissors(x + (width - foregroundWidth), y, foregroundWidth, height, provider)
				}
			}
			
			if (!invert) {
				foregroundTexture.draw(matrices, provider, x, y, width, height)
			} else {
				foregroundTexture.draw(matrices, provider, x + (width - foregroundWidth), y, foregroundWidth, height)
			}
			
			if (stepWidth == -1.0F) {
				scissors.destroy()
			}
		}
	}
}