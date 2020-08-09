package com.github.vini2003.blade.common.widget.base

import com.github.vini2003.blade.Blade
import com.github.vini2003.blade.client.data.PartitionedTexture
import com.github.vini2003.blade.client.utilities.Drawings
import com.github.vini2003.blade.client.utilities.Layers
import com.github.vini2003.blade.common.data.Position
import com.github.vini2003.blade.common.data.Size
import com.github.vini2003.blade.common.data.geometry.Rectangle
import com.github.vini2003.blade.common.data.widget.TabCollection
import com.github.vini2003.blade.common.utilities.Networks
import com.github.vini2003.blade.common.utilities.Positions
import com.github.vini2003.blade.common.widget.OriginalWidgetCollection
import com.github.vini2003.blade.common.widget.WidgetCollection
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text


class TabWidget : AbstractWidget(), WidgetCollection {
	private val tabRectangles: ArrayList<Rectangle> = ArrayList()

	private val tabCollections: ArrayList<TabCollection> = ArrayList()

	private val tabSymbols: ArrayList<ItemStack> = ArrayList()

	private val tabTooltips: ArrayList<() -> List<Text>> = ArrayList()

	override val widgets: ArrayList<AbstractWidget> = ArrayList()

	var selected: Int = 0

	private val leftActive = Blade.identifier("textures/widget/tab_left_active.png")
	private val middleActive = Blade.identifier("textures/widget/tab_middle_active.png")
	private val rightActive = Blade.identifier("textures/widget/tab_right_active.png")

	private val leftInactive = Blade.identifier("textures/widget/tab_left_inactive.png")
	private val middleInactive = Blade.identifier("textures/widget/tab_middle_inactive.png")
	private val rightInactive = Blade.identifier("textures/widget/tab_right_inactive.png")

	var panel = PartitionedTexture(Blade.identifier("textures/widget/panel.png"), 18F, 18F, 0.25F, 0.25F, 0.25F, 0.25F)

	override fun onLayoutChanged() {
		super.onLayoutChanged()

		widgets.clear()

		tabCollections.forEach {
			widgets.addAll(it.widgets)
		}
	}

	override fun onAdded(original: OriginalWidgetCollection, immediate: WidgetCollection) {
		super.onAdded(original, immediate)

		tabCollections.forEachIndexed { index, collection ->
			collection.original = original
			collection.parent = this

			if (index != 0) {
				collection.widgets.forEach {
					it.hidden = true
				}
			}
		}

		synchronize.add(Networks.MOUSE_CLICK)
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
		val collection = TabCollection(tabCollections.size)
		collection.original = original
		collection.parent = this

		tabCollections.add(collection)
		tabSymbols.add(symbol)
		tabTooltips.add(tooltip)

		tabRectangles.also {
			it.clear()

			var x = 0F

			tabCollections.forEach { _ ->
				tabRectangles.add(Rectangle(Position({ position.x + x }, { position.y }), Size({ 26F }, { 25F })))
				x += 26F
			}
		}

		return collection
	}

	override fun getTooltip(): List<Text> {
		tabRectangles.forEachIndexed { index, rectangle ->
			if (rectangle.isWithin(Positions.mouseX, Positions.mouseY)) {
				if (tabTooltips.size >= index) {
					return tabTooltips[index].invoke()
				}
			}
		}

		return super.getTooltip()
	}

	override fun onMouseClicked(x: Float, y: Float, button: Int) {
		super.onMouseClicked(x, y, button)

		if (y < position.y + 25F && y > position.y && x > position.x && x < position.x + size.width) {
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

	override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider) {
		panel.draw(matrices, provider, position.x, position.y + 25F, size.width, size.height)

		if (provider is VertexConsumerProvider.Immediate) provider.draw()

		tabRectangles.forEachIndexed { index, rectangle ->
			if (selected == index) {
				when (rectangle.position.x) {
					position.x -> Drawings.drawTexturedQuad(matrices, provider, Layers.get(leftActive), position.x + (26F * index), position.y, 25F, 29F, leftActive)
					position.x + size.width - 25F -> Drawings.drawTexturedQuad(matrices, provider, Layers.get(rightActive), position.x + (26F * index), position.y, 25F, 28F, rightActive)
					else -> Drawings.drawTexturedQuad(matrices, provider, Layers.get(middleActive), position.x + (26F * index), position.y, 25F, 28F, middleActive)
				}
			} else {
				when (rectangle.position.x) {
					position.x -> Drawings.drawTexturedQuad(matrices, provider, Layers.get(leftInactive), position.x + (26F * index), position.y + 3F, 25F, 24F, leftInactive)
					position.x + size.width - 25F -> Drawings.drawTexturedQuad(matrices, provider, Layers.get(rightInactive), position.x + (26F * index), position.y + 2F, 25F, 26F, rightInactive)
					else -> Drawings.drawTexturedQuad(matrices, provider, Layers.get(middleInactive), position.x + (26F * index), position.y + 2F, 25F, 25F, middleInactive)
				}
			}

			Drawings.getItemRenderer()?.renderGuiItemIcon(tabSymbols[index], (position.x + (26F * index) + 4.5F).toInt(), position.y.toInt() + 7)
		}

		widgets.forEach {
			it.drawWidget(matrices, provider)
		}
	}
}