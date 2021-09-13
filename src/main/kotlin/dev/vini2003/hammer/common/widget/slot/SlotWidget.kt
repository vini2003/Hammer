package dev.vini2003.hammer.common.widget.slot

import dev.vini2003.hammer.H
import dev.vini2003.hammer.client.texture.Texture
import dev.vini2003.hammer.client.util.Instances
import dev.vini2003.hammer.common.widget.WidgetCollection
import dev.vini2003.hammer.common.screen.handler.BaseScreenHandler
import dev.vini2003.hammer.common.widget.Widget
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.inventory.Inventory
import net.minecraft.screen.slot.Slot
import kotlin.properties.Delegates

open class SlotWidget(
	var slot: Int,
	var inventory: Inventory,
	var slotProvider: (Inventory, Int, Int, Int) -> Slot
) : Widget() {
	companion object {
		var StandardTexture: Texture = Texture.of(
			H.id("textures/widget/slot.png"),
			18F,
			18F,
			0.05555555555555555556F,
			0.05555555555555555556F,
			0.05555555555555555556F,
			0.05555555555555555556F
		)
	}
	
	constructor(slot: Int, inventory: Inventory) : this(slot, inventory, { inv, id, x, y -> Slot(inv, id, x, y) })
	
	var texture: Texture = StandardTexture
	
	var backendSlot: Slot? = null
	
	override var hidden: Boolean by Delegates.observable(false) { _, _, _ ->
		updateSlotPosition()
	}

	fun updateSlotPosition() {
		if (hidden) {
			backendSlot?.x = Int.MAX_VALUE / 2
			backendSlot?.y = Int.MAX_VALUE / 2
		} else {
			backendSlot?.x = slotX
			backendSlot?.y = slotY
		}

		if (handled?.client == true) {
			updateSlotPositionDelegate()
		}
	}

	@Environment(EnvType.CLIENT)
	fun updateSlotPositionDelegate() {
		val screen = Instances.client.currentScreen as? HandledScreen<*> ?: return

		backendSlot?.x = backendSlot?.x?.minus(screen.x)
		backendSlot?.y = backendSlot?.y?.minus(screen.y)
	}

	private val slotX: Int
		get() = (x + (if (size.width <= 18) 1F else size.width / 2F - 9F)).toInt()

	private val slotY: Int
		get() = (y + (if (size.height <= 18) 1F else size.height / 2F - 9F)).toInt()

	override fun onAdded(handled: WidgetCollection.Handled, immediate: WidgetCollection) {
		super.onAdded(handled, immediate)
		backendSlot = slotProvider(inventory, slot, slotX, slotY)
		backendSlot!!.index = slot

		if (handled is BaseScreenHandler) {
			handled.addSlot(backendSlot)
		}
	}

	override fun onRemoved(handled: WidgetCollection.Handled, immediate: WidgetCollection) {
		super.onRemoved(handled, immediate)

		if (handled is BaseScreenHandler) {
			handled.removeSlot(backendSlot)
		}
	}

	override fun onLayoutChanged() {
		super.onLayoutChanged()
		updateSlotPosition()
	}

	override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider) {
		if (hidden) return

		texture.draw(matrices, provider, position.x, position.y, size.width, size.height)

		super.drawWidget(matrices, provider)
	}
}