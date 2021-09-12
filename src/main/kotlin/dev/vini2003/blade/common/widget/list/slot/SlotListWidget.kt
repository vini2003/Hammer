package dev.vini2003.blade.common.widget.list.slot

import dev.vini2003.blade.BL
import dev.vini2003.blade.client.texture.PartitionedTexture
import dev.vini2003.blade.client.texture.Texture
import dev.vini2003.blade.common.collection.base.HandledWidgetCollection
import dev.vini2003.blade.common.collection.base.WidgetCollection
import dev.vini2003.blade.common.geometry.position.Position
import dev.vini2003.blade.common.geometry.Rectangle
import dev.vini2003.blade.common.geometry.size.Size
import dev.vini2003.blade.common.util.Networks
import dev.vini2003.blade.common.util.Positions
import dev.vini2003.blade.common.widget.AbstractWidget
import dev.vini2003.blade.common.widget.slot.SlotWidget
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import kotlin.math.max
import kotlin.math.min

open class SlotListWidget(
	var inventory: Inventory = SimpleInventory(0),
	var widthInSlots: Int = 0,
	var heightInSlots: Int = 0,
	var maximumSlots: Int = 0
) : AbstractWidget(), WidgetCollection {
	var scrollbarTexture: Texture = Texture.of(
		BL.id("textures/widget/scrollbar.png"),
		18F,
		18F,
		0.11111111111111111111F,
		0.11111111111111111111F,
		0.11111111111111111111F,
		0.16666666666666666667F
	)
	var scrollerTexture: Texture = Texture.of(
		BL.id("textures/widget/scroller.png"),
		18F,
		18F,
		0.11111111111111111111F,
		0.11111111111111111111F,
		0.11111111111111111111F,
		0.11111111111111111111F
	)
	var scrollerFocusTexture: Texture = Texture.of(
		BL.id("textures/widget/scroller_focus.png"),
		18F,
		18F,
		0.11111111111111111111F,
		0.11111111111111111111F,
		0.11111111111111111111F,
		0.11111111111111111111F
	)

	override val widgets: ArrayList<AbstractWidget> = ArrayList()

	private var row = 0

	private var scrollerHeld = false

	private var updateScrollerRectangle: Boolean = false
	private var updateScrollbarRectangle: Boolean = false

	private var scrollerRectangleCached: Rectangle = Rectangle.Empty
	private var scrollbarRectangleCached: Rectangle = Rectangle.Empty

	private val bottomRow: Int
		get() = maximumSlots - heightInSlots

	private val totalRows: Int
		get() = inventory.size() / widthInSlots

	private val scrollerHeight: Float
		get() = min(size.height - 2, heightInSlots.toFloat() / totalRows.toFloat() * (size.height - 2))

	private val scrollerY: Float
		get() = max(y + 2, min(y + size.height - scrollerHeight, row.toFloat() / totalRows.toFloat() * size.height + y + 1))

	private val scrollerRectangle: Rectangle
		get() {
			return if (updateScrollerRectangle) {
				scrollerRectangleCached = Rectangle(Position.of(position.x + size.width - 1 - 16F, scrollerY - 1), Size.of(16F, scrollerHeight))
				updateScrollerRectangle = false
				return scrollerRectangleCached
			} else {
				scrollerRectangleCached
			}
		}

	private val scrollbarRectangle: Rectangle
		get() {
			return if (updateScrollbarRectangle) {
				scrollbarRectangleCached = Rectangle(Position.of(position.x + size.width - 1 - 16F, position.y + 1), Size.of(16F, size.height - 2))
				updateScrollbarRectangle = false
				return scrollbarRectangleCached
			} else {
				scrollbarRectangleCached
			}
		}

	override fun addWidget(widget: AbstractWidget) {
		widgets.add(widget)
		this.handled?.also { widget.onAdded(it, this) }

		widgets.forEach { _ ->
			widget.onLayoutChanged()
		}

		super.addWidget(widget)
	}

	override fun removeWidget(widget: AbstractWidget) {
		widgets.remove(widget)
		this.handled?.also { widget.onRemoved(it, this) }

		widgets.forEach { _ ->
			widget.onLayoutChanged()
		}

		super.removeWidget(widget)
	}

	override fun onLayoutChanged() {
		super.onLayoutChanged()

		updateScrollerRectangle = true
		updateScrollbarRectangle = true
	}

	override fun onAdded(handled: HandledWidgetCollection, immediate: WidgetCollection) {
		super.onAdded(handled, immediate)

		synchronize.add(Networks.MouseScrolled)
		synchronize.add(Networks.MouseClicked)

		widthInSlots = (size.width / 18 - 1).toInt()
		heightInSlots = (size.height / 18).toInt()
		maximumSlots = inventory.size() / widthInSlots

		for (h in 0 until heightInSlots) {
			for (w in 0 until widthInSlots) {
				if ((inventory.size() >= h + w)) {
					val slot = SlotWidget(0 + h * widthInSlots + w, inventory)
					slot.position = position.offset(w * 18, h * 18)
					slot.size = Size.of(18F, 18F)
					widgets.add(slot)
					immediate.addWidget(slot)
				}
			}
		}
	}

	override fun onMouseClicked(x: Float, y: Float, button: Int) {
		if (handled!!.client) {
			super.onMouseClicked(x, y, button)
		}

		if (scrollerRectangle.isWithin(x, y)) {
			scrollerHeld = true
		} else if (scrollbarRectangle.isWithin(x, y)) {
			if (y > scrollerY) {
				onMouseScrolled(x, y, -1.0)
			} else if (y < scrollerY) {
				onMouseScrolled(x, y, 1.0)
			}
		}
	}

	override fun onMouseReleased(x: Float, y: Float, button: Int) {
		if (handled!!.client) {
			super.onMouseReleased(x, y, button)
		}

		scrollerHeld = false
	}

	override fun onMouseDragged(x: Float, y: Float, button: Int, deltaX: Double, deltaY: Double) {
		if (handled!!.client) {
			super.onMouseDragged(x, y, button, deltaX, deltaY)
		}

		if (scrollerHeld) {
			if (deltaY > 0) {
				while (scrollerY < y - scrollerHeight / 2 && row < bottomRow) {
					onMouseScrolled(x, y, -deltaY)
				}
			} else if (deltaY < 0) {
				while (scrollerY > y - scrollerHeight / 2 && row > 0) {
					onMouseScrolled(x, y, -deltaY)
				}
			}
		}
	}

	override fun onMouseScrolled(x: Float, y: Float, deltaY: Double) {
		if (handled!!.client) {
			if (focused || scrollerHeld) {
				super.onMouseScrolled(x, y, deltaY)
			} else {
				return
			}
		}

		if (deltaY > 0 && row > 0) {
			--row
			
			widgets.forEach {
				it as SlotWidget

				val slot = it.backendSlot!!

				if (slot.index - widthInSlots >= 0) {
					slot.index = slot.index - widthInSlots
				}

				handled!!.handler!!.sendContentUpdates()
			}
		} else if (deltaY < 0 && row < bottomRow) {
			++row
			
			widgets.forEach {
				it as SlotWidget

				val slot = it.backendSlot!!

				if (slot.index + widthInSlots <= inventory.size()) {
					slot.index = slot.index + widthInSlots
				}

				handled!!.handler!!.sendContentUpdates()
			}
		}

		updateScrollerRectangle = true
		updateScrollbarRectangle = true
	}

	override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider) {
		if (hidden) return

		scrollbarTexture.draw(matrices, provider, position.x + size.width - 18F, position.y, 18F, size.height)

		val scrollerFocus = scrollerRectangle.isWithin(Positions.MouseX, Positions.MouseY)

		if (scrollerFocus || scrollerHeld) {
			scrollerFocusTexture.draw(matrices, provider, position.x + size.width - 18F + 1F, scrollerY - 1, 16F, scrollerHeight)
		} else {
			scrollerTexture.draw(matrices, provider, position.x + size.width - 18F + 1F, scrollerY - 1, 16F, scrollerHeight)
		}
	}
}