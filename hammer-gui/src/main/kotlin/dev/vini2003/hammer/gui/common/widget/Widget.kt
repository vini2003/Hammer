package dev.vini2003.hammer.gui.common.widget

import dev.vini2003.hammer.gui.common.packet.widget.*
import dev.vini2003.hammer.gui.registry.common.HGUINetworking
import dev.vini2003.hammer.client.util.InstanceUtils
import dev.vini2003.hammer.common.geometry.position.Position
import dev.vini2003.hammer.common.geometry.position.Positioned
import dev.vini2003.hammer.common.geometry.size.Size
import dev.vini2003.hammer.common.geometry.size.Sized
import dev.vini2003.hammer.common.util.BufUtils
import dev.vini2003.hammer.common.util.PositionUtils
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import java.util.*
import kotlin.properties.Delegates

abstract class Widget : Positioned, Sized {
	override var position: Position by Delegates.observable(Position.of(0, 0)) { _, _, _ ->
		onLayoutChanged()
	}
	
	override var size: Size by Delegates.observable(Size.of(0, 0)) { _, _, _ ->
		onLayoutChanged()
	}

	var handled: WidgetCollection.Handled? = null
	var immediate: WidgetCollection? = null
	
	val hash: Int
		get() = Objects.hash(this, parent)
	
	open var hidden: Boolean = false
		get() = if (parent == null) field else field || parent!!.hidden
	
	open var focused: Boolean = false
		get() = if (parent == null) field && !hidden else field && !parent!!.hidden

	open var held: Boolean = false
		get() = if (parent == null) field else field && !parent!!.hidden

	open var parent: Widget? = null
	
	var syncCharacterTyped: Boolean = false
	var syncFocusGained: Boolean = false
	var syncFocusReleased: Boolean = false
	var syncKeyPressed: Boolean = false
	var syncKeyReleased: Boolean = false
	var syncMouseClicked: Boolean = false
	var syncMouseDragged: Boolean = false
	var syncMouseMoved: Boolean = false
	var syncMouseReleased: Boolean = false
	var syncMouseScrolled: Boolean = false
	
	open fun onAdded(handled: WidgetCollection.Handled, immediate: WidgetCollection) {
		this.handled = handled
		this.immediate = immediate
	}

	open fun onRemoved(handled: WidgetCollection.Handled, immediate: WidgetCollection) {
		this.handled = null
		this.immediate = null
	}

	open fun onMouseMoved(x: Float, y: Float) {
		focus(x, y)

		if (this is WidgetCollection && !(handled != null && !handled!!.client)) {
			widgets.forEach {
				it.onMouseMoved(x, y)
			}
		}

		if (focused && handled != null && handled!!.handler != null && handled!!.client && syncMouseMoved) {
			val packet = MouseMovedPacket(handled!!.id!!, hash, x, y)
			val buf = BufUtils.toPacketByteBuf(packet)
			
			ClientPlayNetworking.send(HGUINetworking.MOUSE_MOVED, buf)
		}
	}

	open fun onMouseClicked(x: Float, y: Float, button: Int) {
		if (focused) held = true

		if (this is WidgetCollection && !(handled != null && !handled!!.client)) {
			widgets.forEach {
				it.onMouseClicked(x, y, button)
			}
		}

		if (focused && handled != null && handled!!.handler != null && handled!!.client && syncMouseClicked) {
			val packet = MouseClickedPacket(handled!!.id!!, hash, x, y, button)
			val buf = BufUtils.toPacketByteBuf(packet)
			
			ClientPlayNetworking.send(HGUINetworking.MOUSE_CLICKED, buf)
		}
	}

	open fun onMouseReleased(x: Float, y: Float, button: Int) {
		held = false

		if (this is WidgetCollection && !(handled != null && !handled!!.client)) {
			widgets.forEach {
				it.onMouseReleased(x, y, button)
			}
		}
		if (focused && handled != null && handled!!.handler != null && handled!!.client && syncMouseReleased) {
			val packet = MouseReleasedPacket(handled!!.id!!, hash, x, y, button)
			val buf = BufUtils.toPacketByteBuf(packet)
			
			ClientPlayNetworking.send(HGUINetworking.MOUSE_RELEASED, buf)
		}
	}

	open fun onMouseDragged(x: Float, y: Float, button: Int, deltaX: Double, deltaY: Double) {
		if (this is WidgetCollection && !(handled != null && !handled!!.client)) {
			widgets.forEach {
				it.onMouseDragged(x, y, button, deltaX, deltaY)
			}
		}

		if (focused && handled != null && handled!!.handler != null && handled!!.client && syncMouseDragged) {
			val packet = MouseDraggedPacket(handled!!.id!!, hash, x, y, button, deltaX, deltaY)
			val buf = BufUtils.toPacketByteBuf(packet)
			
			ClientPlayNetworking.send(HGUINetworking.MOUSE_DRAGGED, buf)
		}
	}

	open fun onMouseScrolled(x: Float, y: Float, deltaY: Double) {
		if (this is WidgetCollection && !(handled != null && !handled!!.client)) {
			widgets.forEach {
				it.onMouseScrolled(x, y, deltaY)
			}
		}

		if (focused && handled != null && handled!!.handler != null && handled!!.client && syncMouseScrolled) {
			val packet = MouseScrolledPacket(handled!!.id!!, hash, x, y, deltaY)
			val buf = BufUtils.toPacketByteBuf(packet)
			
			ClientPlayNetworking.send(HGUINetworking.MOUSE_SCROLLED, buf)
		}
	}

	open fun onKeyPressed(keyCode: Int, scanCode: Int, keyModifiers: Int) {
		if (this is WidgetCollection && !(handled != null && !handled!!.client)) {
			widgets.forEach {
				it.onKeyPressed(keyCode, scanCode, keyModifiers)
			}
		}

		if (focused && handled != null && handled!!.handler != null && handled!!.client && syncKeyPressed) {
			val packet = KeyPressedPacket(handled!!.id!!, hash, keyCode, scanCode, keyModifiers)
			val buf = BufUtils.toPacketByteBuf(packet)
			
			ClientPlayNetworking.send(HGUINetworking.KEY_PRESSED, buf)
		}
	}

	open fun onKeyReleased(keyCode: Int, scanCode: Int, keyModifiers: Int) {
		if (this is WidgetCollection && !(handled != null && !handled!!.client)) {
			widgets.forEach {
				it.onKeyReleased(keyCode, scanCode, keyModifiers)
			}
		}

		if (focused && handled != null && handled!!.handler != null && handled!!.client && syncKeyReleased) {
			val packet = KeyReleasedPacket(handled!!.id!!, hash, keyCode, scanCode, keyModifiers)
			val buf = BufUtils.toPacketByteBuf(packet)
			
			ClientPlayNetworking.send(HGUINetworking.KEY_RELEASED, buf)
		}
	}

	open fun onCharacterTyped(character: Char, keyCode: Int) {
		if (this is WidgetCollection && !(handled != null && !handled!!.client)) {
			widgets.forEach {
				it.onCharacterTyped(character, keyCode)
			}
		}

		if (focused && handled != null && handled!!.handler != null && handled!!.client && syncCharacterTyped) {
			val packet = CharacterTypedPacket(handled!!.id!!, hash, character, keyCode)
			val buf = BufUtils.toPacketByteBuf(packet)
			
			ClientPlayNetworking.send(HGUINetworking.CHARACTER_TYPED, buf)
		}
	}

	open fun onFocusGained() {
		if (this is WidgetCollection && !(handled != null && !handled!!.client)) {
			widgets.forEach {
				it.onFocusGained()
			}
		}

		if (focused && handled != null && handled!!.handler != null && handled!!.client && syncFocusGained) {
			val packet = FocusGainedPacket(handled!!.id!!, hash)
			val buf = BufUtils.toPacketByteBuf(packet)
			
			ClientPlayNetworking.send(HGUINetworking.FOCUS_GAINED, buf)
		}
	}

	open fun onFocusReleased() {
		if (this is WidgetCollection && !(handled != null && !handled!!.client)) {
			widgets.forEach {
				it.onFocusReleased()
			}
		}

		if (focused && handled != null && handled!!.handler != null && handled!!.client && syncFocusReleased) {
			val packet = FocusGainedPacket(handled!!.id!!, hash)
			val buf = BufUtils.toPacketByteBuf(packet)
			
			ClientPlayNetworking.send(HGUINetworking.FOCUS_RELEASED, buf)
		}
	}

	@Environment(EnvType.CLIENT)
	open fun getTooltip(): List<Text> {
		return emptyList()
	}

	@Environment(EnvType.CLIENT)
	open fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider, delta: Float) {
	
	}

	open fun onLayoutChanged() {
		focus(PositionUtils.MOUSE_X, PositionUtils.MOUSE_Y)

		parent?.onLayoutChanged()
		handled?.onLayoutChanged()
	}

	fun focus(x: Float, y: Float) {
		val wasFocused = focused
		focused = isWithin(x, y)

		if (wasFocused && !focused) onFocusReleased()
		if (!wasFocused && focused) onFocusGained()
	}

	fun isWithin(x: Float, y: Float): Boolean {
		return x > position.x && x < position.x + size.width && y > position.y && y < position.y + size.height
	}
	
	open fun tick() {
		if (this is WidgetCollection) {
			widgets.forEach(Widget::tick)
		}
	}
	
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Widget) return false
		
		if (position != other.position) return false
		if (size != other.size) return false
		if (parent != other.parent) return false
		
		return true
	}
	
	override fun hashCode(): Int {
		var result = position.hashCode()
		result = 31 * result + size.hashCode()
		result = 31 * result + (parent?.hashCode() ?: 0)
		return result
	}
	
	fun center() {
		if (parent == null) {
			position = Position.of(InstanceUtils.CLIENT.window.scaledWidth / 2.0F - size.width / 2.0F, InstanceUtils.CLIENT.window.scaledHeight / 2.0F - size.height / 2.0F)
		} else {
			position = Position.of(parent!!.position.x + parent!!.width / 2F - width / 2F, parent!!.position.y + parent!!.height / 2F - height / 2F)
		}
	}
	
	fun centerHorizontally() {
		position = Position.of(parent!!.position.x + parent!!.width / 2F - width / 2F, y)
	}
	
	fun centerVertically() {
		position = Position.of(x, parent!!.position.y + parent!!.height / 2F - height / 2F)
	}
}