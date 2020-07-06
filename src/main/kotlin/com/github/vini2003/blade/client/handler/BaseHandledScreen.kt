package com.github.vini2003.blade.client.handler

import com.github.vini2003.blade.client.utilities.Instances
import com.github.vini2003.blade.common.handler.BaseScreenHandler
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text

class BaseHandledScreen(handler: BaseScreenHandler, inventory: PlayerInventory, title: Text) : HandledScreen<BaseScreenHandler>(handler, inventory, title) {
    override fun drawBackground(matrices: MatrixStack?, delta: Float, mouseX: Int, mouseY: Int) {
    }

    override fun isClickOutsideBounds(mouseX: Double, mouseY: Double, left: Int, top: Int, button: Int): Boolean {
        return false
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
        val provider: VertexConsumerProvider.Immediate = Instances.getClientInstance().bufferBuilders.entityVertexConsumers

        handler.getWidgets().forEach {
            it.drawWidget(matrices!!, provider)
        }

        super.render(matrices, mouseX, mouseY, delta)
    }
}