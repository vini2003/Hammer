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

package dev.vini2003.hammer.gui.energy.api.common.widget.bar

import dev.vini2003.hammer.gui.energy.api.common.util.EnergyTextUtils
import dev.vini2003.hammer.core.HC
import dev.vini2003.hammer.core.api.client.scissor.Scissors
import dev.vini2003.hammer.core.api.common.util.extension.gray
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import team.reborn.energy.api.EnergyStorage

open class EnergyBarWidget(
	maximum: () -> Long = { 1L},
	current: () -> Long = { 0L}
) : BaseBarWidget({ maximum().toFloat() }, { current().toFloat() }) {
	var storage: EnergyStorage? = null
		set(value) {
			field = value
			
			current = { value!!.amount.toFloat() }
			maximum = { value!!.capacity.toFloat() }
		}
	
	companion object {
		@JvmField
		val STANDARD_FILLED_TEXTURE = ImageTexture(HC.id("textures/widget/energy_bar_filled.png"))
		
		@JvmField
		val STANDARD_EMPTY_TEXTURE = ImageTexture(HC.id("textures/widget/energy_bar_background.png"))
	}
	
	constructor(storage: EnergyStorage) : this(
		{ storage.amount },
		{ storage.capacity }
	)
	
	var smooth: Boolean = false
	
	override var foregroundTexture: BaseTexture = STANDARD_FILLED_TEXTURE
	
	override var backgroundTexture: BaseTexture = STANDARD_EMPTY_TEXTURE

	override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider, tickDelta: Float) {
		var foregroundHeight = height / maximum() * current()
		
		if (!smooth) {
			foregroundHeight = foregroundHeight.toInt().toFloat()
		}
		
		val scissors: Scissors
		
		backgroundTexture.draw(matrices, provider, x, y, width, height)
		
		scissors = Scissors(x, y + (height - foregroundHeight), width, foregroundHeight, provider)
		
		foregroundTexture.draw(matrices, provider, x, y, width, height)
		
		scissors.destroy()
	}
	
	override fun getTooltip(): List<Text> {
		val storage = storage ?: return listOf(TextUtils.EMPTY.gray())
		
		if (Screen.hasShiftDown()) {
			return EnergyTextUtils.getDetailedTooltips(storage)
		} else {
			return EnergyTextUtils.getShortenedTooltips(storage)
		}
	}
}