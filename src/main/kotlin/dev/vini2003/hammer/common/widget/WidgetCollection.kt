package dev.vini2003.hammer.common.widget

import dev.vini2003.hammer.common.geometry.position.Position
import dev.vini2003.hammer.common.geometry.position.PositionHolder
import dev.vini2003.hammer.common.geometry.size.Size
import dev.vini2003.hammer.common.screen.handler.BaseScreenHandler
import dev.vini2003.hammer.common.util.Slots
import dev.vini2003.hammer.common.widget.bar.FluidBarWidget
import dev.vini2003.hammer.common.widget.bar.TextureBarWidget
import dev.vini2003.hammer.common.widget.button.ButtonWidget
import dev.vini2003.hammer.common.widget.item.ItemWidget
import dev.vini2003.hammer.common.widget.list.ListWidget
import dev.vini2003.hammer.common.widget.list.slot.SlotListWidget
import dev.vini2003.hammer.common.widget.panel.PanelWidget
import dev.vini2003.hammer.common.widget.slot.SlotWidget
import dev.vini2003.hammer.common.widget.tab.TabWidget
import dev.vini2003.hammer.common.widget.text.TextWidget
import net.minecraft.inventory.Inventory
import net.minecraft.text.Text
import kotlin.math.max

interface WidgetCollection {
	val widgets: MutableList<Widget>
	
	val allWidgets: MutableList<Widget>
		get() = (widgets + widgets.map { if (it is WidgetCollection) it.allWidgets else mutableListOf(it) }.flatten()).toMutableList()
	
	operator fun plusAssign(widget: Widget) {
		add(widget)
	}
	
	fun add(widget: Widget) {
		widgets += widget
		
		if (this is Widget) {
			onLayoutChanged()
			
			if (handled != null) {
				widget.onAdded(handled!!, this)
			}
			
			widget.parent = this
		}
	}
	
	operator fun minusAssign(widget: Widget) {
		remove(widget)
	}
	
	fun remove(widget: Widget) {
		widgets -= widget
		
		if (this is Widget) {
			onLayoutChanged()
			
			if (handled != null) {
				widget.onRemoved(handled!!, this)
			}
			
			widget.parent = this
		}
	}
	
	fun button(block: ButtonWidget.() -> Unit) {
		val widget = ButtonWidget()
		this += widget
		widget.apply(block)
	}
	
	fun bar(block: TextureBarWidget.() -> Unit) {
		val widget = TextureBarWidget()
		this += widget
		widget.apply(block)
	}
	
	fun fluidBar(block: FluidBarWidget.() -> Unit) {
		val widget = FluidBarWidget()
		this += widget
		widget.apply(block)
	}
	
	fun item(block: ItemWidget.() -> Unit) {
		val widget = ItemWidget()
		this += widget
		widget.apply(block)
	}
	
	fun list(block: ListWidget.() -> Unit) {
		val widget = ListWidget()
		this += widget
		widget.apply(block)
	}
	
	fun panel(block: PanelWidget.() -> Unit) {
		val widget = PanelWidget()
		this += widget
		widget.apply(block)
	}
	
	fun slotList(widthInSlots: Int = 0, heightInSlots: Int = 0, maximumSlots: Int = 0, inventory: Inventory, block: SlotListWidget.() -> Unit) {
		val widget = SlotListWidget(inventory, widthInSlots, heightInSlots, maximumSlots)
		this += widget
		widget.apply(block)
	}
	
	fun slot(number: Int, inventory: Inventory, block: SlotWidget.() -> Unit) {
		val widget = SlotWidget(number, inventory)
		this += widget
		widget.apply(block)
	}
	
	fun tabs(block: TabWidget.() -> Unit) {
		val widget = TabWidget()
		this += widget
		widget.apply(block)
	}
	
	fun text(text: Text, block: TextWidget.() -> Unit) {
		val widget = TextWidget(text)
		this += widget
		widget.apply(block)
	}
	
	fun playerInventory(position: PositionHolder, size: Size, inventory: Inventory) {
		Slots.addPlayerInventory(position, size, this, inventory)
	}
	
	fun playerInventory(panel: Widget, inventory: Inventory) {
		playerInventory(
			Position.of(
				max(
					panel.x + 8F,
					panel.x + 8F + (panel.width / 2F - (9 * 18F - 4F))
				), panel.y + panel.height - 83F
			), Size.of(18, 18), inventory)
	}
	
	fun slotArray(position: PositionHolder, size: Size, slotNumber: Int, arrayWidth: Int, arrayHeight: Int, inventory: Inventory) {
		Slots.addArray(position, size, this, slotNumber, arrayWidth, arrayHeight, inventory)
	}
	
	interface Handled : WidgetCollection {
		val id: Int?
		
		val client: Boolean
		
		val handler: BaseScreenHandler?
		
		fun onLayoutChanged()
	}
}