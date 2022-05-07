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

package dev.vini2003.hammer.gui.api.common.widget.tab

import dev.vini2003.hammer.core.HC
import dev.vini2003.hammer.core.api.client.texture.BaseTexture
import dev.vini2003.hammer.core.api.client.util.DrawingUtils
import dev.vini2003.hammer.gui.api.common.widget.BaseWidget
import dev.vini2003.hammer.gui.api.common.widget.BaseWidgetCollection
import dev.vini2003.hammer.gui.api.common.widget.panel.PanelWidget
import dev.vini2003.hammer.core.api.client.util.PositionUtils
import dev.vini2003.hammer.core.api.common.math.shape.Shape
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier

/**
 * A [TabWidget] is a widget that has multiple panels, divided
 * into selectable tabs.
 *
 * A tab can be created with any of the [addTab] methods.
 *
 * A tab has an item as a symbol, and, optionally, a tooltip.
 */
open class TabWidget : BaseWidget(), BaseWidgetCollection {
	companion object {
		@JvmField
		val STANDARD_ACTIVE_LEFT_TEXTURE: Identifier = HC.id("textures/widget/tab_left_active.png")
		
		@JvmField
		val STANDARD_ACTIVE_MIDDLE_TEXTURE: Identifier = HC.id("textures/widget/tab_middle_active.png")
		
		@JvmField
		val STANDARD_ACTIVE_RIGHT_TEXTURE: Identifier = HC.id("textures/widget/tab_right_active.png")
		
		@JvmField
		var STANDARD_INACTIVE_LEFT_TEXTURE: Identifier = HC.id("textures/widget/tab_left_inactive.png")
		
		@JvmField
		var STANDARD_INACTIVE_MIDDLE_TEXTURE: Identifier = HC.id("textures/widget/tab_middle_inactive.png")
		
		@JvmField
		var STANDARD_INACTIVE_RIGHT_TEXTURE: Identifier = HC.id("textures/widget/tab_right_inactive.png")
	}
	
	override val widgets : MutableList<BaseWidget> = mutableListOf()
	
	protected open val tabRectangles: MutableList<Shape> = mutableListOf()
	protected open val tabCollections: MutableList<TabWidgetCollection> = mutableListOf()
	protected open val tabSymbols: MutableList<ItemStack> = mutableListOf()
	protected open val tabTooltips: MutableList<() -> List<Text>> = mutableListOf()
	
	open var activeLeftTexture: Identifier = STANDARD_ACTIVE_LEFT_TEXTURE
	open var activeMiddleTexture: Identifier = STANDARD_ACTIVE_MIDDLE_TEXTURE
	open var activeRightTexture: Identifier = STANDARD_ACTIVE_RIGHT_TEXTURE
	
	open var inactiveLeftTexture: Identifier = STANDARD_INACTIVE_LEFT_TEXTURE
	open var inactiveMiddleTexture: Identifier = STANDARD_INACTIVE_MIDDLE_TEXTURE
	open var inactiveRightTexture: Identifier = STANDARD_INACTIVE_RIGHT_TEXTURE

	var panelTexture: BaseTexture = PanelWidget.STANDARD_TEXTURE
	
	var selected: Int = 0
	
	/**
	 * Adds a tab with the given item symbol.
	 *
	 * @param symbolStack the symbol's stack.
	 */
	fun addTab(symbolStack: ItemStack): BaseWidgetCollection {
		return addTab(symbolStack) {
			emptyList()
		}
	}
	
	/**
	 * Adds a tab with the given item symbol.
	 *
	 * @param symbolItem the symbol item.
	 */
	fun addTab(symbolItem: Item): BaseWidgetCollection {
		return addTab(ItemStack(symbolItem)) {
			emptyList()
		}
	}
	
	/**
	 * Adds a tab with the given item symbol.
	 *
	 * @param symbolItem the symbol item.
	 */
	fun addTab(symbolItem: Item, tooltip: () -> List<Text>): BaseWidgetCollection {
		return addTab(ItemStack(symbolItem), tooltip)
	}
	
	/**
	 * Adds a tab with the given item model.
	 *
	 * @param symbolStack the symbol's stack.
	 */
	fun addTab(symbolStack: ItemStack, tooltip: () -> List<Text>): BaseWidgetCollection {
		val collection = TabWidgetCollection(tabCollections.size)
		collection.handled = this.handled
		collection.parent = this
		
		tabCollections += collection
		tabSymbols += symbolStack
		tabTooltips += tooltip
		
		tabRectangles.also { widget ->
			widget.clear()
			
			var x = 0.0F
			
			tabCollections.forEach { _ ->
				tabRectangles += Shape.ScreenRectangle(26.0F, 25.0F, position.offset(x, 0.0F))
				
				x += 26.0F
			}
		}
		
		return collection
	}
	
	override fun onLayoutChanged() {
		super.onLayoutChanged()

		widgets.clear()

		tabCollections.forEach { widget ->
			widgets.addAll(widget.widgets)
		}
	}

	override fun onAdded(handled: BaseWidgetCollection.Handled, immediate: BaseWidgetCollection) {
		super.onAdded(handled, immediate)
		
		syncMouseClicked = true

		tabCollections.forEachIndexed { index, collection ->
			collection.handled = handled
			collection.parent = this

			if (index != 0) {
				collection.widgets.forEach { widget ->
					widget.hidden = true
				}
			}
		}
	}

	override fun getTooltip(): List<Text> {
		tabRectangles.forEachIndexed { index, rectangle ->
			if (rectangle.isPositionWithin(PositionUtils.MOUSE_POSITION)) {
				if (tabTooltips.size >= index) {
					return tabTooltips[index]()
				}
			}
		}

		return super.getTooltip()
	}

	override fun onMouseClicked(x: Float, y: Float, button: Int) {
		super.onMouseClicked(x, y, button)

		val pos = PositionUtils.MOUSE_POSITION
		
		if (tabRectangles.any { widget -> widget.isPositionWithin(position) }) {
			tabRectangles.forEachIndexed { index, rectangle ->
				val hidden = !rectangle.isPositionWithin(pos)

				tabCollections[index].widgets.forEach { widget ->
					widget.hidden = hidden
				}

				if (!hidden) {
					selected = index
				}
			}
		}
	}

	override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider, tickDelta: Float) {
		panelTexture.draw(matrices, provider, position.x, position.y + 25.0F, size.width, size.height - 25.0F)

		if (provider is VertexConsumerProvider.Immediate) {
			provider.draw()
		}
		
		tabRectangles.forEachIndexed { index, rectangle ->
			if (selected == index) {
				when (rectangle.startPos.x) {
					position.x -> DrawingUtils.drawTexturedQuad(matrices, provider, activeLeftTexture, position.x + (26.0F * index), position.y, 25.0F, 29.0F)
					position.x + size.width - 25.0F -> DrawingUtils.drawTexturedQuad(matrices, provider, activeRightTexture, position.x + (26.0F * index), position.y, 25.0F, 28.0F)
					
					else -> DrawingUtils.drawTexturedQuad(matrices, provider, activeMiddleTexture, position.x + (26.0F * index), position.y, 25.0F, 28.0F)
				}
			} else {
				when (rectangle.startPos.x) {
					position.x ->DrawingUtils.drawTexturedQuad(matrices, provider, inactiveLeftTexture, position.x + (26.0F * index), position.y + 3.0F, 25.0F, 24.0F)
					position.x + size.width - 25.0F -> DrawingUtils.drawTexturedQuad(matrices, provider, inactiveRightTexture, position.x + (26.0F * index), position.y + 2.0F, 25.0F, 26.0F)
					
					else -> DrawingUtils.drawTexturedQuad(matrices, provider, inactiveMiddleTexture, position.x + (26.0F * index), position.y + 2.0F, 25.0F, 25.0F)
				}
			}
			
			val itemRenderer = DrawingUtils.ITEM_RENDERER ?: return@forEachIndexed
			
			itemRenderer.renderGuiItemIcon(tabSymbols[index], (position.x + (26.0F * index) + 4.5F).toInt(), position.y.toInt() + 7)
		}
		
		widgets.filterNot { widget ->
			widget.hidden
		}.forEach { widget ->
			widget.drawWidget(matrices, provider, tickDelta)
		}
	}
	
	class TabWidgetCollection(private val number: Int) : BaseWidget(), BaseWidgetCollection {
		override val widgets: MutableList<BaseWidget> = mutableListOf()
		
		override fun add(widget: BaseWidget) {
			super.add(widget)
			
			widget.hidden = widget.hidden || parent != null && (parent as TabWidget).selected != number
		}
	}
}