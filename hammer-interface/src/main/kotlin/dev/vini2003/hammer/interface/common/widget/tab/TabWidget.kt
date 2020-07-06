package dev.vini2003.hammer.`interface`.common.widget.tab

import dev.vini2003.hammer.H
import dev.vini2003.hammer.`interface`.common.widget.Widget
import dev.vini2003.hammer.`interface`.common.widget.WidgetCollection
import dev.vini2003.hammer.`interface`.common.widget.panel.PanelWidget
import dev.vini2003.hammer.client.texture.Texture
import dev.vini2003.hammer.client.util.DrawingUtils
import dev.vini2003.hammer.client.util.LayerUtils
import dev.vini2003.hammer.common.geometry.Rectangle
import dev.vini2003.hammer.common.geometry.size.Size
import dev.vini2003.hammer.common.util.PositionUtils
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier


open class TabWidget : Widget(), WidgetCollection {
	companion object {
		@JvmField
		val STANDARD_ACTIVE_LEFT_TEXTURE: Identifier = H.id("textures/widget/tab_left_active.png")
		
		@JvmField
		val STANDARD_ACTIVE_MIDDLE_TEXTURE: Identifier = H.id("textures/widget/tab_middle_active.png")
		
		@JvmField
		val STANDARD_ACTIVE_RIGHT_TEXTURE: Identifier = H.id("textures/widget/tab_right_active.png")
		
		@JvmField
		var STANDARD_INACTIVE_LEFT_TEXTURE: Identifier = H.id("textures/widget/tab_left_inactive.png")
		
		@JvmField
		var STANDARD_INACTIVE_MIDDLE_TEXTURE: Identifier = H.id("textures/widget/tab_middle_inactive.png")
		
		@JvmField
		var STANDARD_INACTIVE_RIGHT_TEXTURE: Identifier = H.id("textures/widget/tab_right_inactive.png")
	}
	
	private val tabRectangles: MutableList<Rectangle> = mutableListOf()
	private val tabCollections: MutableList<TabWidgetCollection> = mutableListOf()
	private val tabSymbols: MutableList<ItemStack> = mutableListOf()
	private val tabTooltips: MutableList<() -> List<Text>> = mutableListOf()
	override val widgets : MutableList<Widget> = mutableListOf()
	
	var activeLeftTexture = STANDARD_ACTIVE_LEFT_TEXTURE
	var activeMiddleTexture = STANDARD_ACTIVE_MIDDLE_TEXTURE
	var activeRightTexture = STANDARD_ACTIVE_RIGHT_TEXTURE

	var inactiveLeftTexture = STANDARD_INACTIVE_LEFT_TEXTURE
	var inactiveMiddleTexture = STANDARD_INACTIVE_MIDDLE_TEXTURE
	var inactiveRightTexture = STANDARD_INACTIVE_RIGHT_TEXTURE

	var panelTexture: Texture = PanelWidget.STANDARD_TEXTURE
	
	var selected: Int = 0
	
	override fun onLayoutChanged() {
		super.onLayoutChanged()

		widgets.clear()

		tabCollections.forEach {
			widgets.addAll(it.widgets)
		}
	}

	override fun onAdded(handled: WidgetCollection.Handled, immediate: WidgetCollection) {
		super.onAdded(handled, immediate)
		
		syncMouseClicked = true

		tabCollections.forEachIndexed { index, collection ->
			collection.handled = handled
			collection.parent = this

			if (index != 0) {
				collection.widgets.forEach {
					it.hidden = true
				}
			}
		}
	}

	fun addTab(symbol: ItemStack): WidgetCollection {
		return addTab(symbol) {
			emptyList()
		}
	}

	fun addTab(symbol: Item): WidgetCollection {
		return addTab(ItemStack(symbol)) {
			emptyList()
		}
	}

	fun addTab(symbol: Item, tooltip: () -> List<Text>): WidgetCollection {
		return addTab(ItemStack(symbol), tooltip)
	}

	fun addTab(symbol: ItemStack, tooltip: () -> List<Text>): WidgetCollection {
		val collection = TabWidgetCollection(tabCollections.size)
		collection.handled = this.handled
		collection.parent = this

		tabCollections += collection
		tabSymbols += symbol
		tabTooltips += tooltip

		tabRectangles.also {
			it.clear()

			var x = 0F

			tabCollections.forEach { _ ->
				tabRectangles += Rectangle(position.offset(x, 0), Size.of(26F, 25F))
				x += 26F
			}
		}

		return collection
	}

	override fun getTooltip(): List<Text> {
		tabRectangles.forEachIndexed { index, rectangle ->
			if (rectangle.isWithin(PositionUtils.MOUSE_X, PositionUtils.MOUSE_Y)) {
				if (tabTooltips.size >= index) {
					return tabTooltips[index]()
				}
			}
		}

		return super.getTooltip()
	}

	override fun onMouseClicked(x: Float, y: Float, button: Int) {
		super.onMouseClicked(x, y, button)

		if (tabRectangles.any { it.isWithin(x, y) }) {
			tabRectangles.forEachIndexed { index, rectangle ->
				val hidden = !rectangle.isWithin(x, y)

				tabCollections[index].widgets.forEach {
					it.hidden = hidden
				}

				if (!hidden) {
					selected = index
				}
			}
		}
	}

	override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider, delta: Float) {
		panelTexture.draw(matrices, provider, position.x, position.y + 25F, size.width, size.height - 25F)

		if (provider is VertexConsumerProvider.Immediate) provider.draw()
		
		tabRectangles.forEachIndexed { index, rectangle ->
			if (selected == index) {
				when (rectangle.position.x) {
					position.x -> DrawingUtils.drawTexturedQuad(matrices, provider, LayerUtils.get(activeLeftTexture), position.x + (26F * index), position.y, 25F, 29F, activeLeftTexture)
					position.x + size.width - 25F -> DrawingUtils.drawTexturedQuad(matrices, provider, LayerUtils.get(activeRightTexture), position.x + (26F * index), position.y, 25F, 28F, activeRightTexture)
					else -> DrawingUtils.drawTexturedQuad(matrices, provider, LayerUtils.get(activeMiddleTexture), position.x + (26F * index), position.y, 25F, 28F, activeMiddleTexture)
				}
			} else {
				when (rectangle.position.x) {
					position.x -> DrawingUtils.drawTexturedQuad(matrices, provider, LayerUtils.get(inactiveLeftTexture), position.x + (26F * index), position.y + 3F, 25F, 24F, inactiveLeftTexture)
					position.x + size.width - 25F -> DrawingUtils.drawTexturedQuad(matrices, provider, LayerUtils.get(inactiveRightTexture), position.x + (26F * index), position.y + 2F, 25F, 26F, inactiveRightTexture)
					else -> DrawingUtils.drawTexturedQuad(matrices, provider, LayerUtils.get(inactiveMiddleTexture), position.x + (26F * index), position.y + 2F, 25F, 25F, inactiveMiddleTexture)
				}
			}
			
			DrawingUtils.ITEM_RENDERER?.renderGuiItemIcon(tabSymbols[index], (position.x + (26F * index) + 4.5F).toInt(), position.y.toInt() + 7)
		}
		
		widgets.asSequence().filterNot(Widget::hidden).forEach {
			it.drawWidget(matrices, provider, delta)
		}
	}
	
	class TabWidgetCollection(private val number: Int) : Widget(), WidgetCollection {
		override val widgets: MutableList<Widget> = mutableListOf()
		
		override fun add(widget: Widget) {
			super.add(widget)
			
			widget.hidden = widget.hidden || parent != null && (parent as TabWidget).selected != number
		}
	}
	
	fun TabWidget.tab(stack: ItemStack): TabWidgetCollection.() -> Unit {
		return { addTab(stack) }
	}
	
	fun TabWidget.tab(stack: ItemStack, tooltipBlock: () -> List<Text>): TabWidgetCollection.() -> Unit {
		return { addTab(stack, tooltipBlock) }
	}
}