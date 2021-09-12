package dev.vini2003.blade.common.collection.base

import dev.vini2003.blade.common.geometry.position.Position
import dev.vini2003.blade.common.geometry.position.PositionHolder
import dev.vini2003.blade.common.geometry.size.Size
import dev.vini2003.blade.common.util.Slots
import dev.vini2003.blade.common.widget.AbstractWidget
import dev.vini2003.blade.common.widget.bar.HorizontalBarWidget
import dev.vini2003.blade.common.widget.bar.HorizontalFluidBarWidget
import dev.vini2003.blade.common.widget.bar.VerticalBarWidget
import dev.vini2003.blade.common.widget.bar.VerticalFluidBarWidget
import dev.vini2003.blade.common.widget.button.ButtonWidget
import dev.vini2003.blade.common.widget.panel.*
import dev.vini2003.blade.common.widget.item.ItemWidget
import dev.vini2003.blade.common.widget.list.ListWidget
import dev.vini2003.blade.common.widget.list.slot.SlotListWidget
import dev.vini2003.blade.common.widget.slot.SlotWidget
import dev.vini2003.blade.common.widget.tab.TabWidget
import dev.vini2003.blade.common.widget.text.TextWidget
import net.minecraft.inventory.Inventory
import net.minecraft.text.Text

interface WidgetCollection {
	val widgets: MutableList<AbstractWidget>

	val allWidgets: MutableList<AbstractWidget>
		get() = (widgets + widgets.map { if (it is WidgetCollection) it.allWidgets else mutableListOf(it) }.flatten()).toMutableList()

	fun addWidget(widget: AbstractWidget) {
		widgets.add(widget)
		
		if (this is AbstractWidget) {
			this.onLayoutChanged()
			this.handled?.also { widget.onAdded(it, this) }
			widget.parent = this
		}
	}

	fun removeWidget(widget: AbstractWidget) {
		widgets.remove(widget)
		
		if (this is AbstractWidget) {
			this.onLayoutChanged()
			this.handled?.also { widget.onRemoved(it, this) }
			widget.parent = this
		}
	}
	
	fun button(block: ButtonWidget.() -> Unit) {
		val widget = ButtonWidget()
		addWidget(widget)
		widget.apply(block)
	}

	fun horizontalBar(block: HorizontalBarWidget.() -> Unit) {
		val widget = HorizontalBarWidget()
		addWidget(widget)
		widget.apply(block)
	}

	fun verticalBar(block: VerticalBarWidget.() -> Unit) {
		val widget = VerticalBarWidget()
		addWidget(widget)
		widget.apply(block)
	}
	
	fun horizontalFluidBar(block: HorizontalFluidBarWidget.() -> Unit) {
		val widget = HorizontalFluidBarWidget()
		addWidget(widget)
		widget.apply(block)
	}
	
	fun verticalFluidBar(block: VerticalFluidBarWidget.() -> Unit) {
		val widget = VerticalFluidBarWidget()
		addWidget(widget)
		widget.apply(block)
	}

	fun item(block: ItemWidget.() -> Unit) {
		val widget = ItemWidget()
		addWidget(widget)
		widget.apply(block)
	}

	fun list(block: ListWidget.() -> Unit) {
		val widget = ListWidget()
		addWidget(widget)
		widget.apply(block)
	}

	fun panel(block: PanelWidget.() -> Unit) {
		val widget = PanelWidget()
		addWidget(widget)
		widget.apply(block)
	}

	fun slotList(widthInSlots: Int = 0, heightInSlots: Int = 0, maximumSlots: Int = 0, inventory: Inventory, block: SlotListWidget.() -> Unit) {
		val widget = SlotListWidget(inventory, widthInSlots, heightInSlots, maximumSlots)
		addWidget(widget)
		widget.apply(block)
	}

	fun slot(number: Int, inventory: Inventory, block: SlotWidget.() -> Unit) {
		val widget = SlotWidget(number, inventory)
		addWidget(widget)
		widget.apply(block)
	}

	fun tabs(block: TabWidget.() -> Unit) {
		val widget = TabWidget()
		addWidget(widget)
		widget.apply(block)
	}

	fun text(text: Text, block: TextWidget.() -> Unit) {
		val widget = TextWidget(text)
		addWidget(widget)
		widget.apply(block)
	}

	fun playerInventory(position: PositionHolder, size: Size, inventory: Inventory) {
		Slots.addPlayerInventory(position, size, this, inventory)
	}

	fun playerInventory(panel: AbstractWidget, inventory: Inventory) {
		playerInventory(Position.of(java.lang.Float.max(panel.x + 8F, panel.x + 8F + (panel.width / 2F - (9 * 18F - 4F))), panel.y + panel.height - 83F), Size.of(18, 18), inventory)
	}

	fun slotArray(position: PositionHolder, size: Size, slotNumber: Int, arrayWidth: Int, arrayHeight: Int, inventory: Inventory) {
		Slots.addArray(position, size, this, slotNumber, arrayWidth, arrayHeight, inventory)
	}
}