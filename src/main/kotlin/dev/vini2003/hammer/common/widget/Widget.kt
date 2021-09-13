package dev.vini2003.hammer.common.widget

import dev.vini2003.hammer.common.geometry.position.Position
import dev.vini2003.hammer.common.geometry.position.Positioned
import dev.vini2003.hammer.common.geometry.size.Size
import dev.vini2003.hammer.common.geometry.size.Sized
import dev.vini2003.hammer.common.util.Networks
import dev.vini2003.hammer.common.util.Positions
import dev.vini2003.hammer.common.util.extension.plus
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier
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
	
	val synchronize: MutableSet<Identifier> = mutableSetOf()
	
	val hash: Int
		get() = Objects.hash(this, parent)
	
	open var hidden: Boolean = false
		get() = if (parent == null) field else field || parent!!.hidden
	
	open var focused: Boolean = false
		get() = if (parent == null) field && !hidden else field && !parent!!.hidden

	open var held: Boolean = false
		get() = if (parent == null) field else field && !parent!!.hidden

	open var parent: Widget? = null
	
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

		if (focused && handled != null && handled!!.handler != null && handled!!.client && synchronize.contains(Networks.MouseMoved)) {
			Networks.toServer(Networks.WidgetUpdate, Networks.ofMouseMove(handled!!.id!!, hash, x, y))
		}
	}

	open fun onMouseClicked(x: Float, y: Float, button: Int) {
		if (focused) held = true

		if (this is WidgetCollection && !(handled != null && !handled!!.client)) {
			widgets.forEach {
				it.onMouseClicked(x, y, button)
			}
		}

		if (focused && handled != null && handled!!.handler != null && handled!!.client && synchronize.contains(Networks.MouseClicked)) {
			Networks.toServer(Networks.WidgetUpdate, Networks.ofMouseClick(handled!!.id!!, hash, x, y, button))
		}
	}

	open fun onMouseReleased(x: Float, y: Float, button: Int) {
		held = false

		if (this is WidgetCollection && !(handled != null && !handled!!.client)) {
			widgets.forEach {
				it.onMouseReleased(x, y, button)
			}
		}
		if (focused && handled != null && handled!!.handler != null && handled!!.client && synchronize.contains(Networks.MouseReleased)) {
			Networks.toServer(Networks.WidgetUpdate, Networks.ofMouseRelease(handled!!.id!!, hash, x, y, button))
		}
	}

	open fun onMouseDragged(x: Float, y: Float, button: Int, deltaX: Double, deltaY: Double) {
		if (this is WidgetCollection && !(handled != null && !handled!!.client)) {
			widgets.forEach {
				it.onMouseDragged(x, y, button, deltaX, deltaY)
			}
		}

		if (focused && handled != null && handled!!.handler != null && handled!!.client && synchronize.contains(Networks.MouseDragged)) {
			Networks.toServer(Networks.WidgetUpdate, Networks.ofMouseDrag(handled!!.id!!, hash, x, y, button, deltaX, deltaY))
		}
	}

	open fun onMouseScrolled(x: Float, y: Float, deltaY: Double) {
		if (this is WidgetCollection && !(handled != null && !handled!!.client)) {
			widgets.forEach {
				it.onMouseScrolled(x, y, deltaY)
			}
		}

		if (focused && handled != null && handled!!.handler != null && handled!!.client && synchronize.contains(Networks.MouseScrolled)) {
			Networks.toServer(Networks.WidgetUpdate, Networks.ofMouseScroll(handled!!.id!!, hash, x, y, deltaY))
		}
	}

	open fun onKeyPressed(keyCode: Int, scanCode: Int, keyModifiers: Int) {
		if (this is WidgetCollection && !(handled != null && !handled!!.client)) {
			widgets.forEach {
				it.onKeyPressed(keyCode, scanCode, keyModifiers)
			}
		}

		if (focused && handled != null && handled!!.handler != null && handled!!.client && synchronize.contains(Networks.KeyPressed)) {
			Networks.toServer(Networks.WidgetUpdate, Networks.ofKeyPress(handled!!.id!!, hash, keyCode, scanCode, keyModifiers))
		}
	}

	open fun onKeyReleased(keyCode: Int, character: Int, keyModifiers: Int) {
		if (this is WidgetCollection && !(handled != null && !handled!!.client)) {
			widgets.forEach {
				it.onKeyReleased(keyCode, character, keyModifiers)
			}
		}

		if (focused && handled != null && handled!!.handler != null && handled!!.client && synchronize.contains(Networks.KeyReleased)) {
			Networks.toServer(Networks.WidgetUpdate, Networks.ofKeyRelease(handled!!.id!!, hash, keyCode, character, keyModifiers))
		}
	}

	open fun onCharacterTyped(character: Char, keyCode: Int) {
		if (this is WidgetCollection && !(handled != null && !handled!!.client)) {
			widgets.forEach {
				it.onCharacterTyped(character, keyCode)
			}
		}

		if (focused && handled != null && handled!!.handler != null && handled!!.client && synchronize.contains(Networks.CharacterTyped)) {
			Networks.toServer(Networks.WidgetUpdate, Networks.ofCharType(handled!!.id!!, hash, character, keyCode))
		}
	}

	open fun onFocusGained() {
		if (this is WidgetCollection && !(handled != null && !handled!!.client)) {
			widgets.forEach {
				it.onFocusGained()
			}
		}

		if (focused && handled != null && handled!!.handler != null && handled!!.client && synchronize.contains(Networks.FocusGained)) {
			Networks.toServer(Networks.WidgetUpdate, Networks.ofFocusGain(handled!!.id!!, hash))
		}
	}

	open fun onFocusReleased() {
		if (this is WidgetCollection && !(handled != null && !handled!!.client)) {
			widgets.forEach {
				it.onFocusReleased()
			}
		}

		if (focused && handled != null && handled!!.handler != null && handled!!.client && synchronize.contains(Networks.FocusReleased)) {
			Networks.toServer(Networks.WidgetUpdate, Networks.ofFocusRelease(handled!!.id!!, hash))
		}
	}

	@Environment(EnvType.CLIENT)
	open fun getTooltip(): List<Text> {
		return emptyList()
	}

	@Environment(EnvType.CLIENT)
	open fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider) {
	
	}

	open fun onLayoutChanged() {
		focus(Positions.MouseX, Positions.MouseY)

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
}