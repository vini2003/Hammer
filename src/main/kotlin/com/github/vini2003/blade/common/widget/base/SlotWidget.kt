package com.github.vini2003.blade.common.widget.base

import com.github.vini2003.blade.client.utilities.Colors
import com.github.vini2003.blade.client.utilities.Delays
import com.github.vini2003.blade.client.utilities.Drawings
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import java.util.function.Predicate

class SlotWidget(public val slot: Int, public val inventory: Int) : AbstractWidget() {
    companion object {
        const val LEFT = 0
        const val RIGHT = 1
        const val MIDDLE = 2

        var draggedButton: Int = -1
        var dragged: Set<SlotWidget> = emptySet()
    }

    private val insertionFilters: List<Predicate<ItemStack>> = emptyList()

    var skip: Boolean = false
    var disabled: Boolean = false
    var whitelist = false

    var previewStack: ItemStack = ItemStack.EMPTY

    override fun onMouseDragged(x: Float, y: Float, button: Int, deltaX: Double, deltaY: Double) {
        super.onMouseDragged(x, y, button, deltaX, deltaY)

        if (!focused) return

        if (button != draggedButton) {
            draggedButton = button
            dragged = emptySet()
        }

        dragged.plus(this)
    }

    override fun onMouseReleased(x: Float, y: Float, button: Int) {
        if (button == MIDDLE || disabled || !focused) return

        val slotNumbers: IntArray = dragged.map { it.slot }.toIntArray()
        val inventoryNumbers: IntArray = dragged.map { it.slot }.toIntArray()

        val dragging = dragged.isNotEmpty() && Delays.interval() > Delays.delay()
        val cursorEmpty = origin

        super.onMouseReleased(x, y, button)
    }

    fun getStack(): ItemStack {
        return origin?.getInventory(inventory)?.getStack(slot) ?: ItemStack.EMPTY
    }

    fun setStack(stack: ItemStack) {
        origin?.getInventory(inventory)?.setStack(slot, stack)
    }

    fun canInsert(stack: ItemStack): Boolean {
        if (disabled) return false

        return if (whitelist) {
            insertionFilters.find { predicate -> predicate.test(stack) } != null
        } else {
            insertionFilters.find { predicate -> predicate.test(stack) } == null
        }
    }

    override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider) {
        if (hidden) return
        super.drawWidget(matrices, provider)

        val x: Float = getPosition().x
        val y: Float = getPosition().y

        val sX: Float = getSize().width
        val sY: Float = getSize().height

        Drawings.drawBeveledPanel(matrices, provider, x, y, sX, sY, Colors.SLOT_TOP_LEFT, Colors.SLOT_BACKGROUND, Colors.SLOT_BOTTOM_RIGHT)

        val drawStack = if (previewStack.isEmpty) getStack() else previewStack

        Drawings.getItemRenderer()?.renderInGui(drawStack, ((1 + x) + ((sX - 18) / 2)).toInt(), ((1 + y) + ((sY - 18) / 2)).toInt());
        Drawings.getItemRenderer()?.renderGuiItemOverlay(Drawings.getTextRenderer(), drawStack, x.toInt(), y.toInt(), if (drawStack.count == 1) "" else drawStack.count.toString());

        if (focused) {
            Drawings.drawQuad(matrices, provider, x + 1, y + 1, sX - 2, sY - 2, Colors.SLOT_OVERLAY);
        }
    }
}