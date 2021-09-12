package dev.vini2003.blade.common.widget

import dev.vini2003.blade.common.collection.base.HandledWidgetCollection
import dev.vini2003.blade.common.collection.base.WidgetCollection
import dev.vini2003.blade.common.geometry.position.Position
import dev.vini2003.blade.common.geometry.position.Positioned
import dev.vini2003.blade.common.geometry.size.Size
import dev.vini2003.blade.common.geometry.size.Sized
import dev.vini2003.blade.common.util.Networks
import dev.vini2003.blade.common.util.Positions
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import java.util.*
import kotlin.properties.Delegates

abstract class AbstractWidget : Positioned, Sized {
	override var position: Position by Delegates.observable(Position.of(0, 0)) { _, _, _ ->
		onLayoutChanged()
	}
	override var size: Size by Delegates.observable(Size.of(0, 0)) { _, _, _ ->
		onLayoutChanged()
	}

	var handled: HandledWidgetCollection? = null
	var immediate: WidgetCollection? = null

	val hash: Int
		get() = Objects.hash(javaClass.name + "_" + position.x + "_" + position.y + "_" + size.width + "_" + size.height + "_" + hidden + (if (parent != null) "_" + parent!!.hash else "") + (if (salt != null) "_" + salt!! else ""))

	var salt: String? = null

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
	
	open fun onAdded(handled: HandledWidgetCollection, immediate: WidgetCollection) {
		this.handled = handled
		this.immediate = immediate
	}

	open fun onRemoved(handled: HandledWidgetCollection, immediate: WidgetCollection) {
		this.handled = null
		this.immediate = null
	}

	open fun onMouseMoved(x: Float, y: Float) {
		focus(x, y)

		if (this is WidgetCollection && !(handled != null && !handled!!.client)) {
			this.widgets.forEach {
				it.onMouseMoved(x, y)
			}
		}

		if (focused && this.handled != null && this.handled!!.handler != null && this.handled!!.client && synchronize.contains(Networks.MouseMoved)) {
			Networks.toServer(Networks.WidgetUpdate, Networks.ofMouseMove(this.handled!!.id!!, hash, x, y))
		}
	}

	open fun onMouseClicked(x: Float, y: Float, button: Int) {
		if (focused) held = true

		if (this is WidgetCollection && !(handled != null && !handled!!.client)) {
			this.widgets.forEach {
				it.onMouseClicked(x, y, button)
			}
		}

		if (focused && this.handled != null && this.handled!!.handler != null && this.handled!!.client && synchronize.contains(Networks.MouseClicked)) {
			Networks.toServer(Networks.WidgetUpdate, Networks.ofMouseClick(this.handled!!.id!!, hash, x, y, button))
		}
	}

	open fun onMouseReleased(x: Float, y: Float, button: Int) {
		held = false

		if (this is WidgetCollection && !(handled != null && !handled!!.client)) {
			this.widgets.forEach {
				it.onMouseReleased(x, y, button)
			}
		}
		if (focused && this.handled != null && this.handled!!.handler != null && this.handled!!.client && synchronize.contains(Networks.MouseReleased)) {
			Networks.toServer(Networks.WidgetUpdate, Networks.ofMouseRelease(this.handled!!.id!!, hash, x, y, button))
		}
	}

	open fun onMouseDragged(x: Float, y: Float, button: Int, deltaX: Double, deltaY: Double) {
		if (this is WidgetCollection && !(handled != null && !handled!!.client)) {
			this.widgets.forEach {
				it.onMouseDragged(x, y, button, deltaX, deltaY)
			}
		}

		if (focused && this.handled != null && this.handled!!.handler != null && this.handled!!.client && synchronize.contains(Networks.MouseDragged)) {
			Networks.toServer(Networks.WidgetUpdate, Networks.ofMouseDrag(this.handled!!.id!!, hash, x, y, button, deltaX, deltaY))
		}
	}

	open fun onMouseScrolled(x: Float, y: Float, deltaY: Double) {
		if (this is WidgetCollection && !(handled != null && !handled!!.client)) {
			this.widgets.forEach {
				it.onMouseScrolled(x, y, deltaY)
			}
		}

		if (focused && this.handled != null && this.handled!!.handler != null && this.handled!!.client && synchronize.contains(Networks.MouseScrolled)) {
			Networks.toServer(Networks.WidgetUpdate, Networks.ofMouseScroll(this.handled!!.id!!, hash, x, y, deltaY))
		}
	}

	open fun onKeyPressed(keyCode: Int, scanCode: Int, keyModifiers: Int) {
		if (this is WidgetCollection && !(handled != null && !handled!!.client)) {
			this.widgets.forEach {
				it.onKeyPressed(keyCode, scanCode, keyModifiers)
			}
		}

		if (focused && this.handled != null && this.handled!!.handler != null && this.handled!!.client && synchronize.contains(Networks.KeyPressed)) {
			Networks.toServer(Networks.WidgetUpdate, Networks.ofKeyPress(this.handled!!.id!!, hash, keyCode, scanCode, keyModifiers))
		}
	}

	open fun onKeyReleased(keyCode: Int, character: Int, keyModifiers: Int) {
		if (this is WidgetCollection && !(handled != null && !handled!!.client)) {
			this.widgets.forEach {
				it.onKeyReleased(keyCode, character, keyModifiers)
			}
		}

		if (focused && this.handled != null && this.handled!!.handler != null && this.handled!!.client && synchronize.contains(Networks.KeyReleased)) {
			Networks.toServer(Networks.WidgetUpdate, Networks.ofKeyRelease(this.handled!!.id!!, hash, keyCode, character, keyModifiers))
		}
	}

	open fun onCharacterTyped(character: Char, keyCode: Int) {
		if (this is WidgetCollection && !(handled != null && !handled!!.client)) {
			this.widgets.forEach {
				it.onCharacterTyped(character, keyCode)
			}
		}

		if (focused && this.handled != null && this.handled!!.handler != null && this.handled!!.client && synchronize.contains(Networks.CharacterTyped)) {
			Networks.toServer(Networks.WidgetUpdate, Networks.ofCharType(this.handled!!.id!!, hash, character, keyCode))
		}
	}

	open fun onFocusGained() {
		if (this is WidgetCollection && !(handled != null && !handled!!.client)) {
			this.widgets.forEach {
				it.onFocusGained()
			}
		}

		if (focused && this.handled != null && this.handled!!.handler != null && this.handled!!.client && synchronize.contains(Networks.FocusGained)) {
			Networks.toServer(Networks.WidgetUpdate, Networks.ofFocusGain(this.handled!!.id!!, hash))
		}
	}

	open fun onFocusReleased() {
		if (this is WidgetCollection && !(handled != null && !handled!!.client)) {
			this.widgets.forEach {
				it.onFocusReleased()
			}
		}

		if (focused && this.handled != null && this.handled!!.handler != null && this.handled!!.client && synchronize.contains(Networks.FocusReleased)) {
			Networks.toServer(Networks.WidgetUpdate, Networks.ofFocusRelease(this.handled!!.id!!, hash))
		}
	}

	@Environment(EnvType.CLIENT)
	open fun getTooltip(): List<Text> {
		return emptyList()
	}

	@Environment(EnvType.CLIENT)
	open fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider) {
		if (hidden) return
	}

	open fun onLayoutChanged() {
		focus(Positions.MouseX, Positions.MouseY)

		parent?.onLayoutChanged()
		this.handled?.onLayoutChanged()
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
			this.widgets.forEach {
				it.tick()
			}
		}
	}
}