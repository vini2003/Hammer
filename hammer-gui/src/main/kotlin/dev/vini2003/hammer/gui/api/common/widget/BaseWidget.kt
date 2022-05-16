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

package dev.vini2003.hammer.gui.api.common.widget

import dev.vini2003.hammer.core.api.client.util.PositionUtils
import dev.vini2003.hammer.core.api.common.math.position.Position
import dev.vini2003.hammer.gui.registry.common.HGUINetworking
import dev.vini2003.hammer.core.api.common.math.position.Positioned
import dev.vini2003.hammer.core.api.common.math.size.Size
import dev.vini2003.hammer.core.api.common.math.size.Sized
import dev.vini2003.hammer.core.api.common.util.BufUtils
import dev.vini2003.hammer.gui.impl.common.packet.widget.*
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import java.util.*
import kotlin.properties.Delegates

/**
 * A [WidgetYeet] is a screen component, responsible for displaying data to the user and handlign user interactions.
 *
 * Implementations should avoid doing tasks in constructors. Instead, they should utilize [onAdded]/[onRemoved].
 */
abstract class BaseWidget : Positioned, Sized {
	/**
	 * This widget's screen position.
	 */
	override var position: Position by Delegates.observable(Position(0.0F, 0.0F)) { _, _, _ ->
		onLayoutChanged()
	}
	
	/**
	 * This widget's screen size.
	 */
	override var size: Size by Delegates.observable(Size(0.0F, 0.0F)) { _, _, _ ->
		onLayoutChanged()
	}
	
	/**
	 * This widget's outermost collection, directly attached to the screen or screen handler.
	 */
	open var handled: BaseWidgetCollection.Handled? = null
	
	/**
	 * This widget's immediate parent's collection.
	 */
	open var immediate: BaseWidgetCollection? = null
	
	/**
	 * This widget's immediate parent.
	 */
	open var parent: BaseWidget? = null
	
	/**
	 * A unique hash for this widget.
	 */
	open val hash: Int
		get() = Objects.hashCode(this)
	
	/**
	 * Whether this widget is hidden or not.
	 *
	 * **All user interaction handlers should stop when a widget is hidden.**
	 */
	@Suppress("INAPPLICABLE_JVM_NAME")
	@get:JvmName("isHidden")
	open var hidden: Boolean = false
		get() = if (parent == null) field else field || parent!!.hidden
	
	/**
	 * Whether this widget is being focused (by the mouse) or not.
	 */
	@Suppress("INAPPLICABLE_JVM_NAME")
	@get:JvmName("isFocused")
	open var focused: Boolean = false
		get() = if (parent == null) field && !hidden else field && !parent!!.hidden
	
	/**
	 * Whether this widget is being held (by the mouse) or not.
	 */
	@Suppress("INAPPLICABLE_JVM_NAME")
	@get:JvmName("isHeld")
	open var held: Boolean = false
		get() = if (parent == null) field else field && !parent!!.hidden
	
	/**
	 * Whether this widget is locking player input or not.
	 */
	@Suppress("INAPPLICABLE_JVM_NAME")
	@get:JvmName("isLocking")
	open var locking: Boolean = false
	
	/**
	 * Whether [onMouseMoved] will be synchronized to the server or not.
	 */
	@Suppress("INAPPLICABLE_JVM_NAME")
	@get:JvmName("shouldSyncMoseMouved")
	open var syncMouseMoved: Boolean = false
	
	/**
	 * Whether [onMouseClicked] will be synchronized to the server or not.
	 */
	@Suppress("INAPPLICABLE_JVM_NAME")
	@get:JvmName("shouldSyncMouseClicked")
	open var syncMouseClicked: Boolean = false
	
	/**
	 * Whether [onMouseReleased] will be synchronized to the server or not.
	 */
	@Suppress("INAPPLICABLE_JVM_NAME")
	@get:JvmName("shouldSyncMouseReleased")
	open var syncMouseReleased: Boolean = false
	
	/**
	 * Whether [onMouseDragged] will be synchronized to the server or not.
	 */
	@Suppress("INAPPLICABLE_JVM_NAME")
	@get:JvmName("shouldSyncMouseDragged")
	open var syncMouseDragged: Boolean = false
	
	/**
	 * Whether [onMouseScrolled] will be synchronized to the server or not.
	 */
	@Suppress("INAPPLICABLE_JVM_NAME")
	@get:JvmName("shouldSyncMouseScrolled")
	open var syncMouseScrolled: Boolean = false
	
	/**
	 * Whether [onKeyPressed] will be synchronized to the server or not.
	 */
	@Suppress("INAPPLICABLE_JVM_NAME")
	@get:JvmName("shouldSyncKeyPressed")
	open var syncKeyPressed: Boolean = false
	
	/**
	 * Whether [onKeyReleased] will be synchronized to the server or not.
	 */
	@Suppress("INAPPLICABLE_JVM_NAME")
	@get:JvmName("shouldSyncKeyReleased")
	open var syncKeyReleased: Boolean = false
	
	/**
	 * Must be checked from [onCharacterTyped], and synchronize if necessary.
	 */
	@Suppress("INAPPLICABLE_JVM_NAME")
	@get:JvmName("shouldSyncCharacterTyped")
	open var syncCharacterTyped: Boolean = false
	
	/**
	 * Whether [onFocusGained] will be synchronized to the server or not.
	 */
	@Suppress("INAPPLICABLE_JVM_NAME")
	@get:JvmName("shouldSyncFocusGained")
	open var syncFocusGained: Boolean = false
	
	/**
	 * Whether [onFocusReleased] will be synchronized to the server or not.
	 */
	@Suppress("INAPPLICABLE_JVM_NAME")
	@get:JvmName("shouldSyncFocusReleased")
	open var syncFocusReleased: Boolean = false
	
	/**
	 * Called when this widget is added to a collection.
	 *
	 * **All tasks related to initializing this widget should happen here.**
	 *
	 * @param handled the outermost collection, directly attached to the screen or screen handler.
	 * @param immediate the parent collection, which can be any widget.
	 */
	open fun onAdded(handled: BaseWidgetCollection.Handled, immediate: BaseWidgetCollection) {
		this.handled = handled
		this.immediate = immediate
	}
	
	/**
	 * Called when this widget is removed from a collection.
	 *
	 * **All tasks related to finalizing this widget should happen here.**
	 *
	 * @param handled the outermost collection, directly attached to the screen or screen handler.
	 * @param immediate the parent collection, which can be any widget.
	 */
	open fun onRemoved(handled: BaseWidgetCollection.Handled, immediate: BaseWidgetCollection) {
		this.handled = null
		this.immediate = null
	}
	
	/**
	 * Called when the mouse is moved.
	 *
	 * **This method is synchronized if [syncMouseMoved] is `true`.**
	 *
	 * **This method is called even when the mouse is outside this widget's boundaries - for that, check if the widget is [focused]!**
	 *
	 * @param x the mouse X coordinate.
	 * @param y the mouse Y coordinate.
	 */
	open fun onMouseMoved(x: Float, y: Float) {
		updateFocus(x, y)
		
		if (this is BaseWidgetCollection && !(handled != null && !handled!!.client)) {
			widgets.forEach { widget ->
				widget.onMouseMoved(x, y)
			}
		}
		
		if (focused && handled != null && handled!!.handler != null && handled!!.client && syncMouseMoved) {
			val packet = MouseMovedPacket(handled!!.id!!, hash, x, y)
			val buf = BufUtils.toPacketByteBuf(packet)
			
			ClientPlayNetworking.send(HGUINetworking.MOUSE_MOVED, buf)
		}
	}
	
	/**
	 * Called when the mouse is clicked.
	 *
	 * **This method is synchronized if [syncMouseClicked] is `true`.**
	 *
	 * **This method is called even when the mouse is outside this widget's boundaries - for that, check if the widget is [focused]!**
	 *
	 * @param x the mouse X coordinate.
	 * @param y the mouse Y coordinate.
	 * @param button the mouse button - `0` for Left Click, `1` for Right Click, `3` for Middle/Scroll Click.
	 */
	open fun onMouseClicked(x: Float, y: Float, button: Int) {
		if (focused) held = true
		
		if (this is BaseWidgetCollection && !(handled != null && !handled!!.client)) {
			widgets.forEach { widget ->
				widget.onMouseClicked(x, y, button)
			}
		}
		
		if (focused && handled != null && handled!!.handler != null && handled!!.client && syncMouseClicked) {
			val packet = MouseClickedPacket(handled!!.id!!, hash, x, y, button)
			val buf = BufUtils.toPacketByteBuf(packet)
			
			ClientPlayNetworking.send(HGUINetworking.MOUSE_CLICKED, buf)
		}
	}
	
	/**
	 * Called when the mouse is released.
	 *
	 * **This method is synchronized if [syncMouseReleased] is `true`.**
	 *
	 * This method is called after [onMouseClicked].
	 *
	 * **This method is called even when the mouse is outside this widget's boundaries - for that, check if the widget is [focused]!**
	 *
	 * @param x the mouse X coordinate.
	 * @param y the mouse Y coordinate.
	 * @param button the mouse button - `0` for Left Click, `1` for Right Click, `3` for Middle/Scroll Click.
	 */
	open fun onMouseReleased(x: Float, y: Float, button: Int) {
		held = false
		
		if (this is BaseWidgetCollection && !(handled != null && !handled!!.client)) {
			widgets.forEach { widget ->
				widget.onMouseReleased(x, y, button)
			}
		}
		if (focused && handled != null && handled!!.handler != null && handled!!.client && syncMouseReleased) {
			val packet = MouseReleasedPacket(handled!!.id!!, hash, x, y, button)
			val buf = BufUtils.toPacketByteBuf(packet)
			
			ClientPlayNetworking.send(HGUINetworking.MOUSE_RELEASED, buf)
		}
	}
	
	/**
	 * Called when the mouse is dragged.
	 *
	 * **This method is synchronized if [syncMouseDragged] is `true`.**
	 *
	 * **This method is called even when the mouse is outside this widget's boundaries - for that, check if the widget is [focused]!**
	 *
	 * @param x the mouse X coordinate.
	 * @param y the mouse Y coordinate.
	 * @param button the mouse button - `0` for Left Click, `1` for Right Click, `3` for Middle/Scroll Click.
	 * @param deltaX the mouse X-axis drag distance.
	 * @param deltaY the mouse Y-axis drag distance.
	 */
	open fun onMouseDragged(x: Float, y: Float, button: Int, deltaX: Double, deltaY: Double) {
		if (this is BaseWidgetCollection && !(handled != null && !handled!!.client)) {
			widgets.forEach { widget ->
				widget.onMouseDragged(x, y, button, deltaX, deltaY)
			}
		}
		
		if (focused && handled != null && handled!!.handler != null && handled!!.client && syncMouseDragged) {
			val packet = MouseDraggedPacket(handled!!.id!!, hash, x, y, button, deltaX, deltaY)
			val buf = BufUtils.toPacketByteBuf(packet)
			
			ClientPlayNetworking.send(HGUINetworking.MOUSE_DRAGGED, buf)
		}
	}
	
	/**
	 * Called when the mouse is scrolled.
	 *
	 * **This method is synchronized if [syncMouseScrolled] is `true`.**
	 *
	 * **This method is called even when the mouse is outside this widget's boundaries - for that, check if the widget is [focused]!**
	 *
	 * @param x the mouse X coordinate.
	 * @param y the mouse Y coordinate.
	 * @param deltaY the mouse Y-axis scroll distance.
	 */
	open fun onMouseScrolled(x: Float, y: Float, deltaY: Double) {
		if (this is BaseWidgetCollection && !(handled != null && !handled!!.client)) {
			widgets.forEach { widget ->
				widget.onMouseScrolled(x, y, deltaY)
			}
		}
	
		if (focused && handled != null && handled!!.handler != null && handled!!.client && syncMouseScrolled) {
			val packet = MouseScrolledPacket(handled!!.id!!, hash, x, y, deltaY)
			val buf = BufUtils.toPacketByteBuf(packet)
		
			ClientPlayNetworking.send(HGUINetworking.MOUSE_SCROLLED, buf)
		}
	}
	
	/**
	 * Called when a key is pressed.
	 *
	 * **This method is synchronized if [syncKeyPressed] is `true`.**
	 *
	 * **This method is called even when the mouse is outside this widget's boundaries - for that, check if the widget is [focused]!**
	 *
	 * @param keyCode the key's code.
	 * @param scanCode the key's scan code.
	 * @param keyModifiers the key's modifiers.
	 */
	open fun onKeyPressed(keyCode: Int, scanCode: Int, keyModifiers: Int) {
		if (this is BaseWidgetCollection && !(handled != null && !handled!!.client)) {
			widgets.forEach { widget ->
				widget.onKeyPressed(keyCode, scanCode, keyModifiers)
			}
		}
		
		if (focused && handled != null && handled!!.handler != null && handled!!.client && syncKeyPressed) {
			val packet = KeyPressedPacket(handled!!.id!!, hash, keyCode, scanCode, keyModifiers)
			val buf = BufUtils.toPacketByteBuf(packet)
			
			ClientPlayNetworking.send(HGUINetworking.KEY_PRESSED, buf)
		}
	}
	
	/**
	 * Called when a key is released.
	 *
	 * **This method is synchronized if [syncKeyReleased] is `true`.**
	 *
	 * **This method is called even when the mouse is outside this widget's boundaries - for that, check if the widget is [focused]!**
	 *
	 * @param keyCode the key's code.
	 * @param scanCode the key's scan code.
	 * @param keyModifiers the key's modifiers.
	 */
	open fun onKeyReleased(keyCode: Int, scanCode: Int, keyModifiers: Int) {
		if (this is BaseWidgetCollection && !(handled != null && !handled!!.client)) {
			widgets.forEach { widget ->
				widget.onKeyReleased(keyCode, scanCode, keyModifiers)
			}
		}
		
		if (focused && handled != null && handled!!.handler != null && handled!!.client && syncKeyReleased) {
			val packet = KeyReleasedPacket(handled!!.id!!, hash, keyCode, scanCode, keyModifiers)
			val buf = BufUtils.toPacketByteBuf(packet)
			
			ClientPlayNetworking.send(HGUINetworking.KEY_RELEASED, buf)
		}
	}
	
	/**
	 * Called when a character is typed.
	 *
	 * **This method is synchronized if [syncCharacterTyped] is `true`.**
	 *
	 * **This method is called even when the mouse is outside this widget's boundaries - for that, check if the widget is [focused]!**
	 *
	 * @param character the character.
	 * @param keyCode the character's key's code.
	 */
	open fun onCharacterTyped(character: Char, keyCode: Int) {
		if (this is BaseWidgetCollection && !(handled != null && !handled!!.client)) {
			widgets.forEach { widget ->
				widget.onCharacterTyped(character, keyCode)
			}
		}
		
		if (focused && handled != null && handled!!.handler != null && handled!!.client && syncCharacterTyped) {
			val packet = CharacterTypedPacket(handled!!.id!!, hash, character, keyCode)
			val buf = BufUtils.toPacketByteBuf(packet)
			
			ClientPlayNetworking.send(HGUINetworking.CHARACTER_TYPED, buf)
		}
	}
	
	/**
	 * Called when focus is gained.
	 *
	 * **This method is synchronized if [syncFocusGained] is `true`.**
	 *
	 * Can also be checked with [focused].
	 */
	open fun onFocusGained() {
		if (this is BaseWidgetCollection && !(handled != null && !handled!!.client)) {
			widgets.forEach { widget ->
				widget.onFocusGained()
			}
		}
		
		if (focused && handled != null && handled!!.handler != null && handled!!.client && syncFocusGained) {
			val packet = FocusGainedPacket(handled!!.id!!, hash)
			val buf = BufUtils.toPacketByteBuf(packet)
			
			ClientPlayNetworking.send(HGUINetworking.FOCUS_GAINED, buf)
		}
	}
	
	/**
	 * Called when focus is released.
	 *
	 * **This method is synchronized if [syncFocusReleased] is `true`.**
	 *
	 * Can also be checked with [focused].
	 */
	open fun onFocusReleased() {
		if (this is BaseWidgetCollection && !(handled != null && !handled!!.client)) {
			widgets.forEach { widget ->
				widget.onFocusReleased()
			}
		}
		
		if (focused && handled != null && handled!!.handler != null && handled!!.client && syncFocusReleased) {
			val packet = FocusGainedPacket(handled!!.id!!, hash)
			val buf = BufUtils.toPacketByteBuf(packet)
			
			ClientPlayNetworking.send(HGUINetworking.FOCUS_RELEASED, buf)
		}
	}
	
	/**
	 * Called when this widget's position, size, parents or any other attribute of such type is changed.
	 */
	open fun onLayoutChanged() {
		updateFocus(PositionUtils.MOUSE_X, PositionUtils.MOUSE_Y)
		
		parent?.onLayoutChanged()
		handled?.onLayoutChanged()
	}
	
	/**
	 * Returns this widget's tooltip.
	 *
	 * @return the tooltip.
	 */
	open fun getTooltip(): List<Text> {
		return emptyList()
	}
	
	/**
	 * Called when this widget is ticked.
	 */
	open fun tick() {
		if (this is BaseWidgetCollection) {
			widgets.forEach { widget ->
				widget.tick()
			}
		}
	}
	
	/**
	 * Returns whether a point is within this widget's boundaries or not.
	 *
	 * @return the result.
	 */
	fun isPointWithin(x: Float, y: Float): Boolean {
		return x > position.x && x < position.x + size.width && y > position.y && y < position.y + size.height
	}
	
	/**
	 * Draws this widget on the screen.
	 *
	 * @param matrices the matrices to use.
	 * @param provider the provider to use.
	 * @param tickDelta the tick delta.
	 */
	open fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider, tickDelta: Float) {
	}

	private fun updateFocus(x: Float, y: Float) {
		val wasFocused = focused
		focused = isPointWithin(x, y)

		if (wasFocused && !focused) onFocusReleased()
		if (!wasFocused && focused) onFocusGained()
	}
	
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is BaseWidget) return false
		
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
}