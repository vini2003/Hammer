package dev.vini2003.blade.common.collection.base

import dev.vini2003.blade.client.data.PartitionedTexture
import dev.vini2003.blade.common.collection.TabWidgetCollection
import dev.vini2003.blade.common.miscellaneous.Position
import dev.vini2003.blade.common.miscellaneous.PositionHolder
import dev.vini2003.blade.common.miscellaneous.Size
import dev.vini2003.blade.common.utilities.Slots
import dev.vini2003.blade.common.widget.base.*
import net.minecraft.inventory.Inventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Identifier

interface WidgetCollection {
	val widgets: MutableList<AbstractWidget>

	val allWidgets: MutableList<AbstractWidget>
		get() = (widgets + widgets.map { if (it is WidgetCollection) it.allWidgets else mutableListOf(it) }.flatten()).toMutableList()

	fun addWidget(widget: AbstractWidget) {
		widgets.add(widget)
		if (this is AbstractWidget) {
			this.onLayoutChanged()
			this.extended?.also { widget.onAdded(it, this) }
			widget.parent = this
		}
	}

	fun removeWidget(widget: AbstractWidget) {
		widgets.remove(widget)
		if (this is AbstractWidget) {
			this.onLayoutChanged()
			this.extended?.also { widget.onRemoved(it, this) }
			widget.parent = this
		}
	}

	fun AbstractWidget.position(position: Position) {
		this.position = position
	}

	fun AbstractWidget.position(x: Float, y: Float) {
		this.position = Position.of(x, y)
	}

	fun AbstractWidget.position(anchor: Position, x: Float, y: Float) {
		this.position = Position.of(anchor, x, y)
	}

	fun AbstractWidget.center() {
		this.position = Position.of(parent!!.position.x + parent!!.width / 2F - width / 2F, parent!!.position.y + parent!!.height / 2F - height / 2F)
	}

	fun AbstractWidget.centerHorizontally() {
		this.position = Position.of(parent!!.position.x + parent!!.width / 2F - width / 2F, y)
	}

	fun AbstractWidget.centerVertically() {
		this.position = Position.of(x, parent!!.position.y + parent!!.height / 2F - height / 2F)
	}

	fun AbstractWidget.size(size: Size) {
		this.size = size
	}

	fun AbstractWidget.size(width: Float, height: Float) {
		this.size = Size.of(width, height)
	}

	fun AbstractWidget.size(side: Float) {
		this.size = Size.of(side, side)
	}

	fun AbstractWidget.style(style: String) {
		this.style = style
	}

	fun button(block: ButtonWidget.() -> Unit) {
		val widget = ButtonWidget()
		addWidget(widget)
		widget.apply(block)
	}

	fun ButtonWidget.click(clickAction: () -> Unit) {
		this.clickAction = clickAction
	}

	fun ButtonWidget.label(label: Text) {
		this.label = label
	}

	fun ButtonWidget.label(label: String) {
		this.label = LiteralText(label)
	}

	fun ButtonWidget.disabled(block: () -> Boolean) {
		this.disabled = block
	}

	fun ButtonWidget.textureOn(textureOn: PartitionedTexture) {
		this.textureOn = textureOn
	}

	fun ButtonWidget.textureOff(textureOff: PartitionedTexture) {
		this.textureOff = textureOff
	}

	fun ButtonWidget.textureFocus(textureFocus: PartitionedTexture) {
		this.textureFocus = textureFocus
	}

	fun horizontalBar(block: HorizontalBarWidget.() -> Unit) {
		val widget = HorizontalBarWidget()
		addWidget(widget)
		widget.apply(block)
	}

	fun HorizontalBarWidget.maximum(maximum: () -> Float) {
		this.maximum = maximum
	}

	fun HorizontalBarWidget.current(current: () -> Float) {
		this.current = current
	}

	fun HorizontalBarWidget.backgroundTexture(backgroundTexture: PartitionedTexture) {
		this.backgroundTexture = backgroundTexture
	}

	fun HorizontalBarWidget.foregroundTexture(foregroundTexture: PartitionedTexture) {
		this.foregroundTexture = foregroundTexture
	}

	fun verticalBar(block: VerticalBarWidget.() -> Unit) {
		val widget = VerticalBarWidget()
		addWidget(widget)
		widget.apply(block)
	}

	fun VerticalBarWidget.maximum(maximum: () -> Float) {
		this.maximum = maximum
	}

	fun VerticalBarWidget.current(current: () -> Float) {
		this.current = current
	}

	fun VerticalBarWidget.backgroundTexture(backgroundTexture: PartitionedTexture) {
		this.backgroundTexture = backgroundTexture
	}

	fun VerticalBarWidget.foregroundTexture(foregroundTexture: PartitionedTexture) {
		this.foregroundTexture = foregroundTexture
	}

	fun item(block: ItemWidget.() -> Unit) {
		val widget = ItemWidget()
		addWidget(widget)
		widget.apply(block)
	}

	fun ItemWidget.item(item: Item) {
		this.stack = ItemStack(item)
	}

	fun ItemWidget.stack(stack: ItemStack) {
		this.stack = stack
	}

	fun list(block: ListWidget.() -> Unit) {
		val widget = ListWidget()
		addWidget(widget)
		widget.apply(block)
	}

	fun ListWidget.scrollbarTexture(scrollbarTexture: PartitionedTexture) {
		this.scrollbarTexture = scrollbarTexture
	}

	fun ListWidget.scrollerTexture(scrollerTexture: PartitionedTexture) {
		this.scrollerTexture = scrollerTexture
	}

	fun ListWidget.scrollerFocusTexture(scrollerFocusTexture: PartitionedTexture) {
		this.scrollerFocusTexture = scrollerFocusTexture
	}

	fun panel(block: PanelWidget.() -> Unit) {
		val widget = PanelWidget()
		addWidget(widget)
		widget.apply(block)
	}

	fun PanelWidget.texture(texture: PartitionedTexture) {
		this.texture = texture
	}

	fun slotList(widthInSlots: Int = 0, heightInSlots: Int = 0, maximumSlots: Int = 0, inventory: Inventory, block: SlotListWidget.() -> Unit) {
		val widget = SlotListWidget(inventory, widthInSlots, heightInSlots, maximumSlots)
		addWidget(widget)
		widget.apply(block)
	}

	fun SlotListWidget.scrollbarTexture(scrollbarTexture: PartitionedTexture) {
		this.scrollbarTexture = scrollbarTexture
	}

	fun SlotListWidget.scrollerTexture(scrollerTexture: PartitionedTexture) {
		this.scrollerTexture = scrollerTexture
	}

	fun SlotListWidget.scrollerFocusTexture(scrollerFocusTexture: PartitionedTexture) {
		this.scrollerFocusTexture = scrollerFocusTexture
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

	fun TabWidget.tab(stack: ItemStack): TabWidgetCollection.() -> Unit {
		return { addTab(stack) }
	}

	fun TabWidget.tab(stack: ItemStack, tooltipBlock: () -> List<Text>): TabWidgetCollection.() -> Unit {
		return { addTab(stack, tooltipBlock) }
	}

	fun TabWidget.leftActiveTexture(leftActiveTexturelock: Identifier) {
		this.leftActiveTexture = leftActiveTexturelock
	}

	fun TabWidget.middleActiveTexture(middleActiveTexture: Identifier) {
		this.middleActiveTexture = middleActiveTexture
	}

	fun TabWidget.rightActiveTexture(rightActiveTexture: Identifier) {
		this.rightActiveTexture = rightActiveTexture
	}

	fun TabWidget.leftInactiveTexture(leftInactiveTexturelock: Identifier) {
		this.leftInactiveTexture = leftInactiveTexturelock
	}

	fun TabWidget.middleInactiveTexture(middleInactiveTexture: Identifier) {
		this.middleInactiveTexture = middleInactiveTexture
	}

	fun TabWidget.rightInactiveTexture(rightInactiveTexture: Identifier) {
		this.rightInactiveTexture = rightInactiveTexture
	}

	fun TabWidget.panelTexture(panelTexture: PartitionedTexture) {
		this.panelTexture = panelTexture
	}

	fun text(text: Text, block: TextWidget.() -> Unit) {
		val widget = TextWidget(text)
		addWidget(widget)
		widget.apply(block)
	}

	fun TextWidget.color(color: Int) {
		this.color = color
	}

	fun TextWidget.shadow(shadow: Boolean) {
		this.shadow = shadow
	}

	fun String.literal(): LiteralText {
		return LiteralText(this)
	}

	fun String.translatable(): TranslatableText {
		return TranslatableText(this)
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