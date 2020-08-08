package com.github.vini2003.blade.common.widget.base

import com.github.vini2003.blade.common.data.*
import com.github.vini2003.blade.common.handler.BaseScreenHandler
import com.github.vini2003.blade.common.utilities.Networks
import com.github.vini2003.blade.common.utilities.Positions
import com.github.vini2003.blade.common.utilities.Styles
import com.github.vini2003.blade.common.widget.OriginalWidgetCollection
import com.github.vini2003.blade.common.widget.WidgetCollection
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import java.util.*

abstract class AbstractWidget : Positioned, Sized {
	override var position: Position = Position({ 0F }, { 0F })
		set(value) {
			field = value
			onLayoutChanged()
		}
	override var size: Size = Size({ 0F }, { 0F })
		set(value) {
			field = value
			onLayoutChanged()
		}

	val x: Float
		get() = position.x

	val y: Float
		get() = position.y

	val width: Float
		get() = size.width

	val height: Float
		get() = size.height

	var original: OriginalWidgetCollection? = null
	var immediate: WidgetCollection? = null

	var style = "default"
	val hash = Objects.hash(javaClass.name + "_" + position.x + "_" + position.y + "_" + size.width + "_" + size.height)
	var handler: BaseScreenHandler? = null

	val synchronize: MutableSet<Identifier> = mutableSetOf()

	open var hidden: Boolean = false
		get() {
			return (if (parent == null) field else field || parent!!.hidden)
		}

	open var focused: Boolean = false
		get() {
			return (if (parent == null) field && !hidden else field && !parent!!.hidden)
		}

	open var held: Boolean = false
		get() {
			return (if (parent == null) field else field && !parent!!.hidden)
		}

	open var parent: AbstractWidget? = null

	fun style(): Style {
		return Styles.get(style).also {
			if (it == Style.EMPTY) if (parent == null) Style.EMPTY else parent!!.style() else it
		}
	}

	fun color(property: String): Color {
		return style().asColor(property)
	}

	open fun onAdded(original: OriginalWidgetCollection, immediate: WidgetCollection) {
		this.original = original
		this.immediate = immediate
		this.handler = original.getHandler()
	}

	open fun onRemoved(original: OriginalWidgetCollection, immediate: WidgetCollection) {
		this.original = null
		this.immediate = null
		this.handler = null
	}

	open fun onMouseMoved(x: Float, y: Float) {
		focus(x, y)

		if (this is WidgetCollection) {
			this.widgets.forEach {
				it.onMouseMoved(x, y)
			}
		}

		if (handler != null && handler!!.client && synchronize.contains(Networks.MOUSE_MOVE)) {
			Networks.toServer(Networks.WIDGET_UPDATE, Networks.ofMouseMove(handler!!.syncId, hash, x, y))
		}
	}

	open fun onMouseClicked(x: Float, y: Float, button: Int) {
		if (focused) held = true

		if (this is WidgetCollection) {
			this.widgets.forEach {
				it.onMouseClicked(x, y, button)
			}
		}

		if (handler != null && handler!!.client && synchronize.contains(Networks.MOUSE_CLICK)) {
			Networks.toServer(Networks.WIDGET_UPDATE, Networks.ofMouseClick(handler!!.syncId, hash, x, y, button))
		}
	}

	open fun onMouseReleased(x: Float, y: Float, button: Int) {
		held = false

		if (this is WidgetCollection) {
			this.widgets.forEach {
				it.onMouseReleased(x, y, button)
			}
		}
		if (handler != null && handler!!.client && synchronize.contains(Networks.MOUSE_RELEASE)) {
			Networks.toServer(Networks.WIDGET_UPDATE, Networks.ofMouseRelease(handler!!.syncId, hash, x, y, button))
		}
	}

	open fun onMouseDragged(x: Float, y: Float, button: Int, deltaX: Double, deltaY: Double) {
		if (this is WidgetCollection) {
			this.widgets.forEach {
				it.onMouseDragged(x, y, button, deltaX, deltaY)
			}
		}

		if (handler != null && handler!!.client && synchronize.contains(Networks.MOUSE_DRAG)) {
			Networks.toServer(Networks.WIDGET_UPDATE, Networks.ofMouseDrag(handler!!.syncId, hash, x, y, button, deltaX, deltaY))
		}
	}

	open fun onMouseScrolled(x: Float, y: Float, deltaY: Double) {
		if (this is WidgetCollection) {
			this.widgets.forEach {
				it.onMouseScrolled(x, y, deltaY)
			}
		}

		if (handler != null && handler!!.client && synchronize.contains(Networks.MOUSE_SCROLL)) {
			Networks.toServer(Networks.WIDGET_UPDATE, Networks.ofMouseScroll(handler!!.syncId, hash, x, y, deltaY))
		}
	}

	open fun onKeyPressed(keyCode: Int, scanCode: Int, keyModifiers: Int) {
		if (this is WidgetCollection) {
			this.widgets.forEach {
				it.onKeyPressed(keyCode, scanCode, keyModifiers)
			}
		}

		if (handler != null && handler!!.client && synchronize.contains(Networks.KEY_PRESS)) {
			Networks.toServer(Networks.WIDGET_UPDATE, Networks.ofKeyPress(handler!!.syncId, hash, keyCode, scanCode, keyModifiers))
		}
	}

	open fun onKeyReleased(keyCode: Int, character: Int, keyModifiers: Int) {
		if (this is WidgetCollection) {
			this.widgets.forEach {
				it.onKeyReleased(keyCode, character, keyModifiers)
			}
		}

		if (handler != null && handler!!.client && synchronize.contains(Networks.KEY_RELEASE)) {
			Networks.toServer(Networks.WIDGET_UPDATE, Networks.ofKeyRelease(handler!!.syncId, hash, keyCode, character, keyModifiers))
		}
	}

	open fun onCharTyped(character: Char, keyCode: Int) {
		if (this is WidgetCollection) {
			this.widgets.forEach {
				it.onCharTyped(character, keyCode)
			}
		}

		if (handler != null && handler!!.client && synchronize.contains(Networks.CHAR_TYPE)) {
			Networks.toServer(Networks.WIDGET_UPDATE, Networks.ofCharType(handler!!.syncId, hash, character, keyCode))
		}
	}

	open fun onFocusGained() {
		if (this is WidgetCollection) {
			this.widgets.forEach {
				it.onFocusGained()
			}
		}

		if (handler != null && handler!!.client && synchronize.contains(Networks.FOCUS_GAIN)) {
			Networks.toServer(Networks.WIDGET_UPDATE, Networks.ofFocusGain(handler!!.syncId, hash))
		}
	}

	open fun onFocusReleased() {
		if (this is WidgetCollection) {
			this.widgets.forEach {
				it.onFocusReleased()
			}
		}

		if (handler != null && handler!!.client && synchronize.contains(Networks.FOCUS_RELEASE)) {
			Networks.toServer(Networks.WIDGET_UPDATE, Networks.ofFocusRelease(handler!!.syncId, hash))
		}
	}

	open fun getTooltip(): List<Text> {
		return emptyList()
	}

	open fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider) {
		if (hidden) return
	}

	open fun onLayoutChanged() {
		position.recalculate()
		size.recalculate()

		focus(Positions.mouseX, Positions.mouseY)

		parent?.onLayoutChanged()
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
}