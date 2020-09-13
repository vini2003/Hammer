package com.github.vini2003.blade.common.widget

import com.github.vini2003.blade.client.data.PartitionedTexture
import com.github.vini2003.blade.common.data.Position
import com.github.vini2003.blade.common.data.Size
import com.github.vini2003.blade.common.data.widget.TabCollection
import com.github.vini2003.blade.common.widget.base.*
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
			this.original?.also { widget.onAdded(it, this) }
			widget.parent = this
		}
	}

	fun removeWidget(widget: AbstractWidget) {
		widgets.remove(widget)
		if (this is AbstractWidget) {
			this.onLayoutChanged()
			this.original?.also { widget.onRemoved(it, this) }
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

	fun AbstractWidget.floatTopLeftOut(widget: AbstractWidget, marginHorizontal: Float, marginVertical: Float) {
		this.position = widget.position.offset(-marginHorizontal - width, marginVertical - height)
	}

	fun AbstractWidget.floatMiddleLeftOut(widget: AbstractWidget, marginHorizontal: Float, marginVertical: Float) {
		this.position = widget.position.offset(-marginHorizontal - width, widget.height / 2F - height / 2F + marginVertical)
	}

	fun AbstractWidget.floatBottomLeftOut(widget: AbstractWidget, marginHorizontal: Float, marginVertical: Float) {
		this.position = widget.position.offset(-marginHorizontal - width, widget.height + marginVertical)
	}

	fun AbstractWidget.floatMiddleTopOut(widget: AbstractWidget, marginHorizontal: Float, marginVertical: Float) {
		this.position = widget.position.offset(widget.width / 2F - width / 2F + marginHorizontal, -marginVertical - height)
	}

	fun AbstractWidget.floatMiddleBottomOut(widget: AbstractWidget, marginHorizontal: Float, marginVertical: Float) {
		this.position = widget.position.offset(widget.width / 2F - width / 2F + marginHorizontal, widget.height + marginVertical)
	}

	fun AbstractWidget.floatTopRightOut(widget: AbstractWidget, marginHorizontal: Float, marginVertical: Float) {
		this.position = widget.position.offset(widget.width + marginHorizontal, -marginVertical - height)
	}

	fun AbstractWidget.floatMiddleRightOut(widget: AbstractWidget, marginHorizontal: Float, marginVertical: Float) {
		this.position = widget.position.offset(widget.width + marginHorizontal, widget.height / 2F - height / 2F + marginVertical)
	}

	fun AbstractWidget.floatBottomRightOut(widget: AbstractWidget, marginHorizontal: Float, marginVertical: Float) {
		this.position = widget.position.offset(widget.width + marginHorizontal, widget.height + marginVertical)
	}

	fun AbstractWidget.floatTopLeftIn(widget: AbstractWidget, marginHorizontal: Float, marginVertical: Float) {
		this.position = widget.position.offset(marginHorizontal, marginVertical)
	}

	fun AbstractWidget.floatMiddleLeftIn(widget: AbstractWidget, marginHorizontal: Float, marginVertical: Float) {
		this.position = widget.position.offset(marginHorizontal, widget.height / 2F - height / 2F + marginVertical)
	}

	fun AbstractWidget.floatBottomLeftIn(widget: AbstractWidget, marginHorizontal: Float, marginVertical: Float) {
		this.position = widget.position.offset(marginHorizontal, widget.height - height - marginVertical)
	}

	fun AbstractWidget.floatMiddleTopIn(widget: AbstractWidget, marginHorizontal: Float, marginVertical: Float) {
		this.position = widget.position.offset(widget.width / 2F - width / 2F + marginHorizontal, marginVertical)
	}

	fun AbstractWidget.floatMiddleBottomIn(widget: AbstractWidget, marginHorizontal: Float, marginVertical: Float) {
		this.position = widget.position.offset(widget.width / 2F - width / 2F + marginHorizontal, widget.height - height - marginVertical)
	}

	fun AbstractWidget.floatTopRightIn(widget: AbstractWidget, marginHorizontal: Float, marginVertical: Float) {
		this.position = widget.position.offset(widget.width - width - marginHorizontal, marginVertical)
	}

	fun AbstractWidget.floatMiddleRightIn(widget: AbstractWidget, marginHorizontal: Float, marginVertical: Float) {
		this.position = widget.position.offset(widget.width - width - marginHorizontal, widget.height / 2F - height / 2F + marginVertical)
	}

	fun AbstractWidget.floatBottomRightIn(widget: AbstractWidget, marginHorizontal: Float, marginVertical: Float) {
		this.position = widget.position.offset(widget.width - width - marginHorizontal, widget.height - height - marginVertical)
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

	fun HorizontalBarWidget.textureBackground(backgroundTexture: PartitionedTexture) {
		this.backgroundTexture = backgroundTexture
	}

	fun HorizontalBarWidget.textureForeground(foregroundTexture: PartitionedTexture) {
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

	fun VerticalBarWidget.textureBackground(backgroundTexture: PartitionedTexture) {
		this.backgroundTexture = backgroundTexture
	}

	fun VerticalBarWidget.textureForeground(foregroundTexture: PartitionedTexture) {
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

	fun ListWidget.textureScrollbar(scrollbarTexture: PartitionedTexture) {
		this.scrollbarTexture = scrollbarTexture
	}

	fun ListWidget.textureScroller(scrollerTexture: PartitionedTexture) {
		this.scrollerTexture = scrollerTexture
	}

	fun ListWidget.textureScrollerFocus(scrollerFocusTexture: PartitionedTexture) {
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

	fun SlotListWidget.textureScrollbar(scrollbarTexture: PartitionedTexture) {
		this.scrollbarTexture = scrollbarTexture
	}

	fun SlotListWidget.textureScroller(scrollerTexture: PartitionedTexture) {
		this.scrollerTexture = scrollerTexture
	}

	fun SlotListWidget.textureScrollerFocus(scrollerFocusTexture: PartitionedTexture) {
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

	fun TabWidget.tab(stack: ItemStack): TabCollection.() -> Unit {
		return { addTab(stack) }
	}

	fun TabWidget.tab(stack: ItemStack, tooltipBlock: () -> List<Text>): TabCollection.() -> Unit {
		return { addTab(stack, tooltipBlock) }
	}

	fun TabWidget.textureLeftActive(leftActiveTexturelock: Identifier) {
		this.leftActiveTexture = leftActiveTexturelock
	}

	fun TabWidget.textureMiddleActive(middleActiveTexture: Identifier) {
		this.middleActiveTexture = middleActiveTexture
	}

	fun TabWidget.textureRightActive(rightActiveTexture: Identifier) {
		this.rightActiveTexture = rightActiveTexture
	}

	fun TabWidget.textureLeftInactive(leftInactiveTexturelock: Identifier) {
		this.leftInactiveTexture = leftInactiveTexturelock
	}

	fun TabWidget.textureMiddleInactive(middleInactiveTexture: Identifier) {
		this.middleInactiveTexture = middleInactiveTexture
	}

	fun TabWidget.textureRightInactive(rightInactiveTexture: Identifier) {
		this.rightInactiveTexture = rightInactiveTexture
	}

	fun TabWidget.texturePanel(panelTexture: PartitionedTexture) {
		this.panelTexture = panelTexture
	}

	fun text(text: Text, block: TextWidget.() -> Unit) {
		val widget = TextWidget(text)
		this.addWidget(widget)
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
}