package com.github.vini2003.blade.common.widget.base

import com.github.vini2003.blade.client.utilities.Drawings
import com.github.vini2003.blade.client.utilities.Layers
import com.github.vini2003.blade.common.data.Position
import com.github.vini2003.blade.common.data.Size
import com.github.vini2003.blade.common.data.geometry.Rectangle
import com.github.vini2003.blade.common.utilities.Networks
import com.github.vini2003.blade.common.utilities.Positions
import com.github.vini2003.blade.common.widget.OriginalWidgetCollection
import com.github.vini2003.blade.common.widget.WidgetCollection
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.inventory.Inventory
import kotlin.math.min

class SlotListWidget(
        private val widthInSlots: Int,
        private val heightInSlots: Int,
        private val maximumSlots: Int,
        private val inventory: Inventory
) : AbstractWidget(), WidgetCollection {
    private val widgets = mutableListOf<AbstractWidget>()

    private var row = 0

    private var scrollerHeld = false

    override fun getWidgets(): Collection<AbstractWidget> {
        return widgets
    }

    override fun addWidget(widget: AbstractWidget) {
        widgets.add(widget)
        original?.let { widget.onAdded(it, this) }

        widgets.forEach { _ ->
            widget.onLayoutChanged()
        }
    }

    override fun removeWidget(widget: AbstractWidget) {
        widgets.remove(widget)
        original?.let { widget.onRemoved(it, this) }

        widgets.forEach { _ ->
            widget.onLayoutChanged()
        }
    }

    override fun onAdded(original: OriginalWidgetCollection, immediate: WidgetCollection) {
        super.onAdded(original, immediate)

        synchronize.add(Networks.MOUSE_SCROLL)
        synchronize.add(Networks.MOUSE_CLICK)

        for (h in 0 until heightInSlots) {
            for (w in 0 until widthInSlots) {
                if ((inventory.size() >= h + w)) {
                    val slot = SlotWidget(0 + h * widthInSlots + w, inventory)
                    slot.setPosition(Position({ this.getPosition().x + w * 18F }, { this.getPosition().y + h * 18F }))
                    slot.setSize(Size({ 18F }, { 18F }))
                    widgets.add(slot)
                    immediate.addWidget(slot)
                }
            }
        }
    }

    private fun bottomRow(): Int {
        return maximumSlots / widthInSlots - heightInSlots
    }

    private fun totalRows(): Int {
        return (inventory.size() / widthInSlots)
    }

    private fun scrollerHeight(): Float {
        return min(getSize().height - 2, heightInSlots.toFloat() / totalRows().toFloat() * getSize().height)
    }

    private fun scrollerY(): Float {
        return min((row.toFloat() / totalRows().toFloat()) * getSize().height + getPosition().y + 1, getPosition().y + getSize().height - scrollerHeight() - 1)
    }

    private fun scrollerRectangle(): Rectangle {
        return Rectangle(Position({ getPosition().x + getSize().width  - 1 - 16F }, { scrollerY() - 4F }), Size({ 16F }, { scrollerHeight() + 8F }))
    }

    private fun scrollbarRectangle(): Rectangle {
        return Rectangle(Position({ getPosition().x + getSize().width - 1 - 16F }, { getPosition().y + 1 }), Size({ 16F }, { getSize().height - 1 }))
    }

    override fun onMouseClicked(x: Float, y: Float, button: Int) {
        if (handler!!.client) {
            super.onMouseClicked(x, y, button)
        }

        if (scrollerRectangle().isWithin(x, y)) {
            scrollerHeld = true
        } else if (scrollbarRectangle().isWithin(x, y)) {
            if (y > scrollerY()) {
                onMouseScrolled(x, y, -1.0)
            } else if (y < scrollerY()) {
                onMouseScrolled(x, y, 1.0)
            }
        }
    }

    override fun onMouseReleased(x: Float, y: Float, button: Int) {
        if (handler!!.client) {
            super.onMouseReleased(x, y, button)
        }

        scrollerHeld = false
    }

    override fun onMouseDragged(x: Float, y: Float, button: Int, deltaX: Double, deltaY: Double) {
        if (handler!!.client) {
            super.onMouseDragged(x, y, button, deltaX, deltaY)
        }

        if (scrollerHeld) {
            if (deltaY > 0) {
                while (scrollerY() < y - scrollerHeight() / 2 && row < bottomRow()) {
                    onMouseScrolled(x, y, -deltaY)
                }
            } else if (deltaY < 0) {
                while (scrollerY() > y - scrollerHeight() / 2 && row > 0) {
                    onMouseScrolled(x, y, -deltaY)
                }
            }
        }
    }

    override fun onMouseScrolled(x: Float, y: Float, deltaY: Double) {
        if (handler!!.client) {
            super.onMouseScrolled(x, y, deltaY)
        }

        if (deltaY > 0 && row > 0) {
            --row

            if (!handler!!.client) {
                widgets.forEach {
                    it as SlotWidget

                    val slot = it.backendSlot!!

                    if (slot.index - widthInSlots >= 0) {
                        slot.canInsert = true
                        slot.index = slot.index - widthInSlots
                    }
                }
            }
        } else if (deltaY < 0 && row < bottomRow()) {
            ++row

            if (!handler!!.client) {
                widgets.forEach {
                    it as SlotWidget

                    val slot = it.backendSlot!!

                    if (slot.index + widthInSlots <= inventory.size()) {
                        slot.canInsert = true
                        slot.index = slot.index + widthInSlots
                    }
                }
            }
        }
    }

    override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider) {
        super.drawWidget(matrices, provider)

        Drawings.drawBeveledPanel(matrices, provider, Layers.flat(), getPosition().x + getSize().width - 1F - 18F, getPosition().y - 1, 18F, getSize().height, color("slot_list.top_left"), color("slot_list.background"), color("slot_list.bottom_right"))

        val scrollerFocus = scrollerRectangle().isWithin(Positions.mouseX, Positions.mouseY)

        if (scrollerFocus || scrollerHeld) {
            Drawings.drawBeveledPanel(matrices, provider, Layers.flat(), getPosition().x + getSize().width - 18F, scrollerY() - 1, 16F, scrollerHeight(), color("slot_list.scroller.focused.top_left"), color("slot_list.scroller.focused.background"), color("slot_list.scroller.focused.bottom_right"))
        } else {
            Drawings.drawBeveledPanel(matrices, provider, Layers.flat(), getPosition().x + getSize().width  - 18F, scrollerY() - 1, 16F, scrollerHeight(), color("slot_list.scroller.unfocused.top_left"), color("slot_list.scroller.unfocused.background"), color("slot_list.scroller.unfocused.bottom_right"))
        }
    }
}