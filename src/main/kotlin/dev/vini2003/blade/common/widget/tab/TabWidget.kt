package dev.vini2003.blade.common.widget.tab

import dev.vini2003.blade.BL
import dev.vini2003.blade.client.texture.PartitionedTexture
import dev.vini2003.blade.client.texture.Texture
import dev.vini2003.blade.client.util.Drawings
import dev.vini2003.blade.client.util.Layers
import dev.vini2003.blade.common.collection.base.HandledWidgetCollection
import dev.vini2003.blade.common.collection.base.WidgetCollection
import dev.vini2003.blade.common.geometry.Rectangle
import dev.vini2003.blade.common.geometry.size.Size
import dev.vini2003.blade.common.util.Networks
import dev.vini2003.blade.common.util.Positions
import dev.vini2003.blade.common.widget.AbstractWidget
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text


open class TabWidget : AbstractWidget(), WidgetCollection {
	private val tabRectangles: MutableList<Rectangle> = mutableListOf()
	private val tabCollections: MutableList<WidgetCollection> = mutableListOf()
	private val tabSymbols: MutableList<ItemStack> = mutableListOf()
	private val tabTooltips: MutableList<() -> List<Text>> = mutableListOf()
	override val widgets : MutableList<AbstractWidget> = mutableListOf()

	var selected = 0

	var leftActiveTexture = BL.id("textures/widget/tab_left_active.png")
	var middleActiveTexture = BL.id("textures/widget/tab_middle_active.png")
	var rightActiveTexture = BL.id("textures/widget/tab_right_active.png")

	var leftInactiveTexture = BL.id("textures/widget/tab_left_inactive.png")
	var middleInactiveTexture = BL.id("textures/widget/tab_middle_inactive.png")
	var rightInactiveTexture = BL.id("textures/widget/tab_right_inactive.png")

	var panelTexture: Texture = Texture.of(
		BL.id("textures/widget/panel.png"),
		18F,
		18F,
		0.25F,
		0.25F,
		0.25F,
		0.25F
	)

	override fun onLayoutChanged() {
		super.onLayoutChanged()

		widgets.clear()

		tabCollections.forEach {
			widgets.addAll(it.widgets)
		}
	}

	override fun onAdded(handled: HandledWidgetCollection, immediate: dev.vini2003.blade.common.collection.base.WidgetCollection) {
		super.onAdded(handled, immediate)

		tabCollections.forEachIndexed { index, collection ->
			collection.handled = handled
			collection.parent = this

			if (index != 0) {
				collection.widgets.forEach {
					it.hidden = true
				}
			}
		}

		synchronize.add(Networks.MouseClicked)
	}

	fun addTab(symbol: ItemStack): dev.vini2003.blade.common.collection.base.WidgetCollection {
		return addTab(symbol) {
			emptyList()
		}
	}

	fun addTab(symbol: Item): dev.vini2003.blade.common.collection.base.WidgetCollection {
		return addTab(ItemStack(symbol)) {
			emptyList()
		}
	}

	fun addTab(symbol: Item, tooltip: () -> List<Text>): dev.vini2003.blade.common.collection.base.WidgetCollection {
		return addTab(ItemStack(symbol), tooltip)
	}

	fun addTab(symbol: ItemStack, tooltip: () -> List<Text>): dev.vini2003.blade.common.collection.base.WidgetCollection {
		val collection = WidgetCollection(tabCollections.size)
		collection.handled = this.handled
		collection.parent = this

		tabCollections.add(collection)
		tabSymbols.add(symbol)
		tabTooltips.add(tooltip)

		tabRectangles.also {
			it.clear()

			var x = 0F

			tabCollections.forEach { _ ->
				tabRectangles.add(Rectangle(position.offset(x, 0), Size.of(26F, 25F)))
				x += 26F
			}
		}

		return collection
	}

	override fun getTooltip(): List<Text> {
		tabRectangles.forEachIndexed { index, rectangle ->
			if (rectangle.isWithin(Positions.MouseX, Positions.MouseY)) {
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

	override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider) {
		panelTexture.draw(matrices, provider, position.x, position.y + 25F, size.width, size.height - 25F)

		if (provider is VertexConsumerProvider.Immediate) provider.draw()
		
		with (Drawings) {
		
		}
		
		tabRectangles.forEachIndexed { index, rectangle ->
			if (selected == index) {
				when (rectangle.position.x) {
					position.x -> Drawings.drawTexturedQuad(matrices, provider, Layers.get(leftActiveTexture), position.x + (26F * index), position.y, 25F, 29F, leftActiveTexture)
					position.x + size.width - 25F -> Drawings.drawTexturedQuad(matrices, provider, Layers.get(rightActiveTexture), position.x + (26F * index), position.y, 25F, 28F, rightActiveTexture)
					else -> Drawings.drawTexturedQuad(matrices, provider, Layers.get(middleActiveTexture), position.x + (26F * index), position.y, 25F, 28F, middleActiveTexture)
				}
			} else {
				when (rectangle.position.x) {
					position.x -> Drawings.drawTexturedQuad(matrices, provider, Layers.get(leftInactiveTexture), position.x + (26F * index), position.y + 3F, 25F, 24F, leftInactiveTexture)
					position.x + size.width - 25F -> Drawings.drawTexturedQuad(matrices, provider, Layers.get(rightInactiveTexture), position.x + (26F * index), position.y + 2F, 25F, 26F, rightInactiveTexture)
					else -> Drawings.drawTexturedQuad(matrices, provider, Layers.get(middleInactiveTexture), position.x + (26F * index), position.y + 2F, 25F, 25F, middleInactiveTexture)
				}
			}
			
			Drawings.itemRenderer?.renderGuiItemIcon(tabSymbols[index], (position.x + (26F * index) + 4.5F).toInt(), position.y.toInt() + 7)
		}

		widgets.forEach {
			it.drawWidget(matrices, provider)
		}
	}
	
	class WidgetCollection(private val number: Int) : AbstractWidget(),
		dev.vini2003.blade.common.collection.base.WidgetCollection {
		override val widgets: MutableList<AbstractWidget> = mutableListOf()
		
		override fun addWidget(widget: AbstractWidget) {
			super.addWidget(widget)
			widget.hidden = widget.hidden || parent != null && (parent as TabWidget).selected != number
		}
	}
}