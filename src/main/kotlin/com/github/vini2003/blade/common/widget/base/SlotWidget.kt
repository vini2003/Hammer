package com.github.vini2003.blade.common.widget.base

import com.github.vini2003.blade.Blade
import com.github.vini2003.blade.client.data.PartitionedTexture
import com.github.vini2003.blade.client.utilities.Instances
import com.github.vini2003.blade.common.handler.BaseScreenHandler
import com.github.vini2003.blade.common.widget.OriginalWidgetCollection
import com.github.vini2003.blade.common.widget.WidgetCollection
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.inventory.Inventory
import net.minecraft.screen.slot.Slot
import kotlin.properties.Delegates

class SlotWidget(
	private val slot: Int, private val inventory: Inventory,
	private val slotProvider: (Inventory, Int, Int, Int) -> Slot = { inv, id, x, y -> Slot(inv, id, x, y) }
) : AbstractWidget() {
	var backendSlot: Slot? = null

	var texture = PartitionedTexture(Blade.identifier("textures/widget/slot.png"), 18F, 18F, 0.05555555555555555556F, 0.05555555555555555556F, 0.05555555555555555556F, 0.05555555555555555556F)

	override var hidden: Boolean by Delegates.observable(false) { _, _, _ ->
		updateSlotPosition()
	}

	fun updateSlotPosition() {
		if (hidden) {
			backendSlot?.x = Int.MAX_VALUE
			backendSlot?.y = Int.MAX_VALUE
		} else {
			backendSlot?.x = slotX
			backendSlot?.y = slotY
		}

		if (handler?.client == true) {
			updateSlotPositionDelegate()
		}
	}

	@Environment(EnvType.CLIENT)
	fun updateSlotPositionDelegate() {
		val screen = Instances.client().currentScreen as? HandledScreen<*> ?: return

		backendSlot?.x = backendSlot?.x?.minus(screen.x)
		backendSlot?.y = backendSlot?.y?.minus(screen.y)
	}

	private val slotX: Int
		get() = (x + (if (size.width <= 18) 1F else size.width / 2F - 9F)).toInt()

	private val slotY: Int
		get() = (y + (if (size.height <= 18) 1F else size.height / 2F - 9F)).toInt()

	override fun onAdded(original: OriginalWidgetCollection, immediate: WidgetCollection) {
		super.onAdded(original, immediate)
		backendSlot = slotProvider(inventory, slot, slotX, slotY)
		backendSlot!!.index = slot

		if (original is BaseScreenHandler) {
			original.addSlot(backendSlot)
		}
	}

	override fun onRemoved(original: OriginalWidgetCollection, immediate: WidgetCollection) {
		super.onRemoved(original, immediate)

		if (original is BaseScreenHandler) {
			original.removeSlot(backendSlot)
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