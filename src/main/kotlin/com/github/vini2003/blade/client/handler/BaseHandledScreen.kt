package com.github.vini2003.blade.client.handler

import com.github.vini2003.blade.client.utilities.Instances
import com.github.vini2003.blade.client.utilities.Layers
import com.github.vini2003.blade.common.handler.BaseScreenHandler
import com.github.vini2003.blade.common.utilities.Positions
import com.github.vini2003.blade.common.widget.base.ItemWidget
import com.github.vini2003.blade.common.widget.base.SlotWidget
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text

open class BaseHandledScreen<T : BaseScreenHandler>(handler: BaseScreenHandler, inventory: PlayerInventory, title: Text) : HandledScreen<T>(handler as T, inventory, title) {
    override fun init(client: MinecraftClient?, width: Int, height: Int) {
        super.init(client, width, height)

        x = 0
        y = 0

        this.width = width
        this.height = height

        this.backgroundWidth = width
        this.backgroundHeight = height
    }

    override fun drawBackground(matrices: MatrixStack?, delta: Float, mouseX: Int, mouseY: Int) {

    }

    override fun isClickOutsideBounds(mouseX: Double, mouseY: Double, left: Int, top: Int, button: Int): Boolean {
        return handler.getWidgets().find { widget -> widget.isWithin(mouseX.toFloat(), mouseY.toFloat()) } == null
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        handler.getWidgets().forEach{
            it.onMouseClicked(mouseX.toFloat(), mouseY.toFloat(), button)
        }

        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        handler.getWidgets().forEach{
            it.onMouseReleased(mouseX.toFloat(), mouseY.toFloat(), button)
        }

        return super.mouseReleased(mouseX, mouseY, button)
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
        handler.getWidgets().forEach{
            it.onMouseDragged(mouseX.toFloat(), mouseY.toFloat(), button, deltaX, deltaY)
        }

        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
    }

    override fun mouseMoved(mouseX: Double, mouseY: Double) {
        handler.getWidgets().forEach{
            it.onMouseMoved(mouseX.toFloat(), mouseY.toFloat())
        }

        Positions.mouseX = mouseX.toFloat()
        Positions.mouseY = mouseY.toFloat()

        super.mouseMoved(mouseX, mouseY)
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, deltaY: Double): Boolean {
        handler.getWidgets().forEach{
            it.onMouseScrolled(mouseX.toFloat(), mouseY.toFloat(), deltaY)
        }

        return super.mouseScrolled(mouseX, mouseY, deltaY)
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, keyModifiers: Int): Boolean {
        handler.getWidgets().forEach{
            it.onKeyPressed(keyCode, scanCode, keyModifiers)
        }

        return super.keyPressed(keyCode, scanCode, keyModifiers)
    }

    override fun keyReleased(keyCode: Int, scanCode: Int, keyModifiers: Int): Boolean {
        handler.getWidgets().forEach{
            it.onKeyReleased(keyCode, scanCode, keyModifiers)
        }

        return super.keyReleased(keyCode, scanCode, keyModifiers)
    }

    override fun charTyped(character: Char, keyCode: Int): Boolean {
        handler.getWidgets().forEach{
            it.onCharTyped(character, keyCode)
        }

        return super.charTyped(character, keyCode)
    }

    override fun render(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        val provider: VertexConsumerProvider.Immediate = Instances.getClientInstance().bufferBuilders.effectVertexConsumers

        handler.getWidgets().forEach{
            it.drawWidget(matrices!!, provider)
        }

        super.render(matrices, mouseX, mouseY, delta)

        super.drawMouseoverTooltip(matrices, mouseX, mouseY)

        provider.draw(Layers.getFlat())
        provider.draw(Layers.getTooltip())
        provider.draw()
    }

    override fun resize(client: MinecraftClient?, width: Int, height: Int) {
        super.resize(client, width, height)

        handler.getWidgets().forEach{
            it.onLayoutChanged()
        }
    }
}