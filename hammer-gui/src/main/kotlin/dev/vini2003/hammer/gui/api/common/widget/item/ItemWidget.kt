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

package dev.vini2003.hammer.gui.api.common.widget.item

import dev.vini2003.hammer.gui.api.common.widget.BaseWidget
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

/**
 * An [ItemWidget] is an item that displays an item.
 */
open class ItemWidget @JvmOverloads constructor(
	var stack: ItemStack = ItemStack.EMPTY
) : BaseWidget() {
	override fun getTooltip(): List<Text> {
		if (stack.isEmpty) {
			return emptyList()
		}

		return stack.getTooltip(null) {
			val client = InstanceUtils.CLIENT ?: return@getTooltip false
			
			client.options.advancedItemTooltips
		}
	}

	override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider, tickDelta: Float) {
		val itemRenderer = DrawingUtils.ITEM_RENDERER ?: return
		
		itemRenderer.renderInGui(stack, position.x.toInt(), position.y.toInt())
	}
}