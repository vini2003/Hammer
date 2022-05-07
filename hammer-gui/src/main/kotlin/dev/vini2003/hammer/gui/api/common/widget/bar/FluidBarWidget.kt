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

@file:Suppress("DEPRECATION", "UnstableApiUsage")

package dev.vini2003.hammer.gui.api.common.widget.bar

import dev.vini2003.hammer.core.api.client.scissor.Scissors
import dev.vini2003.hammer.core.api.client.texture.BaseTexture
import dev.vini2003.hammer.core.api.client.texture.TiledFluidTexture
import dev.vini2003.hammer.core.api.client.util.InstanceUtils
import dev.vini2003.hammer.core.api.common.util.FluidTextUtils
import dev.vini2003.hammer.core.api.common.util.TextUtils
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text


/**
 * A [FluidBarWidget] is a [BaseBarWidget] whose texture,
 * [maximum] and [current] are dictated by a storage,
 * or manually configurable.
 */
open class FluidBarWidget @JvmOverloads constructor(
	maximum: () -> Float = { 1.0F },
	current: () -> Float = { 0.0F }
) : BaseBarWidget(maximum, current) {
	var storage: SingleSlotStorage<FluidVariant>? = null
		set(value) {
			field = value
			
			current = { value!!.amount.toFloat() }
			maximum = { value!!.capacity.toFloat() }
			
			foregroundTexture = TiledFluidTexture(value!!.resource)
		}
	
	override var foregroundTexture: BaseTexture = STANDARD_FOREGROUND_TEXTURE
	
	override var backgroundTexture: BaseTexture = STANDARD_BACKGROUND_TEXTURE

	override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider, tickDelta: Float) {
		val client = InstanceUtils.CLIENT ?: return
		
		val windowHeight = client.window.height.toFloat()
		val windowScale = client.window.scaleFactor.toFloat()
		
		val foregroundWidth = width / maximum() * current()
		val foregroundHeight = height / maximum() * current()
		
		var scissors: Scissors
		
		if (vertical) {
			backgroundTexture.draw(matrices, provider, x, y, width, height)
			
			scissors = Scissors((x * windowScale).toInt(), (windowHeight - (y + height) * windowScale).toInt(), (width * windowScale).toInt(), (foregroundHeight * windowScale).toInt(), provider)
			
			foregroundTexture.draw(matrices, provider, x + 1.0F, y + 1.0F, width - 2.0F, height - 2.0F)
			
			scissors.destroy()
		}
		
		if (horizontal) {
			backgroundTexture.draw(matrices, provider, x, y, width, height)
			
			scissors = Scissors((x * windowScale).toInt(), (windowHeight - (y + height) * windowScale).toInt(), (foregroundWidth * windowScale).toInt(), (height * windowScale).toInt(), provider)
			
			foregroundTexture.draw(matrices, provider, x + 1.0F, y + 1.0F, width - 2.0F, height - 2.0F)
			
			scissors.destroy()
		}
	}
	
	override fun getTooltip(): List<Text> {
		val storage = storage ?: return listOf(TextUtils.EMPTY)
		
		if (current() == 0.0F) {
			return listOf(TextUtils.EMPTY)
		} else {
			if (Screen.hasShiftDown()) {
				return FluidTextUtils.getDetailedStorageTooltips(storage)
			} else {
				return FluidTextUtils.getShortenedStorageTooltips(storage)
			}
		}
	}
}