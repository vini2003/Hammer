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

package dev.vini2003.hammer.gui.api.common.widget.panel

import dev.vini2003.hammer.core.HC
import dev.vini2003.hammer.core.api.client.texture.BaseTexture
import dev.vini2003.hammer.core.api.client.texture.PartitionedTexture
import dev.vini2003.hammer.gui.api.common.widget.BaseWidget
import dev.vini2003.hammer.gui.api.common.widget.BaseWidgetCollection
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack

/**
 * A [PanelWidget] is a widget that holds other widgets.
 */
open class PanelWidget : BaseWidget(), BaseWidgetCollection {
	companion object {
		@JvmField
		val STANDARD_TEXTURE: BaseTexture = PartitionedTexture(
			HC.id("textures/widget/panel.png"),
			18F,
			18F,
			0.25F,
			0.25F,
			0.25F,
			0.25F
		)
	}
	
	override val widgets: MutableList<BaseWidget> = mutableListOf()

	var texture: BaseTexture = STANDARD_TEXTURE
	
	override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider, tickDelta: Float) {
		texture.draw(matrices, provider, position.x, position.y, size.width, size.height)

		if (provider is VertexConsumerProvider.Immediate) {
			provider.draw()
		}
		
		widgets.filterNot {widget ->
			widget.hidden
		}.forEach { widget ->
			widget.drawWidget(matrices, provider, tickDelta)
		}
	}
}