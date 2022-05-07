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

package dev.vini2003.hammer.gui.api.common.widget.slot

import dev.vini2003.hammer.core.HC
import dev.vini2003.hammer.core.api.client.texture.BaseTexture
import dev.vini2003.hammer.core.api.client.texture.PartitionedTexture
import dev.vini2003.hammer.gui.api.common.screen.handler.BaseScreenHandler
import dev.vini2003.hammer.gui.api.common.widget.BaseWidget
import dev.vini2003.hammer.gui.api.common.widget.BaseWidgetCollection
import dev.vini2003.hammer.core.api.client.util.InstanceUtils
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.inventory.Inventory
import net.minecraft.screen.slot.Slot
import kotlin.properties.Delegates

/**
 * A [SlotWidget] is a widget that holds a stack.
 */
open class SlotWidget @JvmOverloads constructor(
	var slot: Int,
	var inventory: Inventory,
	var slotProvider: (Inventory, Int, Int, Int) -> Slot = { inventory, id, x, y -> Slot(inventory, id, x, y) }
) : BaseWidget() {
	companion object {
		@JvmField
		val STANDARD_TEXTURE: BaseTexture = PartitionedTexture(
			HC.id("textures/widget/slot.png"),
			18.0F,
			18.0F,
			0.055F,
			0.055F,
			0.055F,
			0.055F
		)
	}
	
	var texture: BaseTexture = STANDARD_TEXTURE
	
	var backendSlot: Slot? = null
	
	protected open val slotX: Int
		get() = (x + (if (size.width <= 18.0F) 1.0F else size.width / 2.0F - 9.0F)).toInt()
	
	protected open val slotY: Int
		get() = (y + (if (size.height <= 18.0F) 1.0F else size.height / 2.0F - 9.0F)).toInt()
	
	override var hidden: Boolean by Delegates.observable(false) { _, _, _ ->
		updateSlotPosition()
	}

	private fun updateSlotPosition() {
		if (hidden) {
			backendSlot?.x = Int.MAX_VALUE / 2
			backendSlot?.y = Int.MAX_VALUE / 2
		} else {
			backendSlot?.x = slotX
			backendSlot?.y = slotY
		}

		if (handled?.client == true) {
			updateSlotPositionOnClient()
		}
	}
	
	private fun updateSlotPositionOnClient() {
		val client = InstanceUtils.CLIENT ?: return
		
		val screen = client.currentScreen as HandledScreen<*>? ?: return

		backendSlot?.x = backendSlot?.x?.minus(screen.x)
		backendSlot?.y = backendSlot?.y?.minus(screen.y)
	}

	override fun onAdded(handled: BaseWidgetCollection.Handled, immediate: BaseWidgetCollection) {
		super.onAdded(handled, immediate)
		
		backendSlot = slotProvider(inventory, slot, slotX, slotY)
		backendSlot!!.index = slot

		if (handled is BaseScreenHandler) {
			handled.addSlot(backendSlot)
		}
	}

	override fun onRemoved(handled: BaseWidgetCollection.Handled, immediate: BaseWidgetCollection) {
		super.onRemoved(handled, immediate)

		if (handled is BaseScreenHandler) {
			handled.removeSlot(backendSlot)
		}
	}

	override fun onLayoutChanged() {
		super.onLayoutChanged()
		
		updateSlotPosition()
	}

	override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider, tickDelta: Float) {
		texture.draw(matrices, provider, position.x, position.y, size.width, size.height)
	}
}