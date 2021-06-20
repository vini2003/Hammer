package dev.vini2003.blade.common.widget.base

import dev.vini2003.blade.common.collection.base.ExtendedWidgetCollection
import dev.vini2003.blade.common.collection.base.WidgetCollection
import dev.vini2003.blade.common.miscellaneous.*
import dev.vini2003.blade.common.utilities.Networks
import dev.vini2003.blade.common.utilities.Positions
import dev.vini2003.blade.common.utilities.Styles
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

	var extended: ExtendedWidgetCollection? = null
	var immediate: WidgetCollection? = null

	var style = "default"

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

	fun style(): Style {
		return Styles.get(style).also {
			if (it == Style.EMPTY) if (parent == null) Style.EMPTY else parent!!.style() else it
		}
	}

	fun color(property: String): Color {
		return style().asColor(property)
	}

	open fun onAdded(extended: ExtendedWidgetCollection, immediate: WidgetCollection) {
		this.extended = extended
		this.immediate = immediate
	}

	open fun onRemoved(extended: ExtendedWidgetCollection, immediate: WidgetCollection) {
		this.extended = null
		this.immediate = null
	}

	open fun onMouseMoved(x: Float, y: Float) {
		focus(x, y)

		if (this is WidgetCollection && !(extended != null && !extended!!.client)) {
			this.widgets.forEach {
				it.onMouseMoved(x, y)
			}
		}

		if (focused && this.extended != null && this.extended!!.handler != null && this.extended!!.client && synchronize.contains(Networks.MouseMoved)) {
			Networks.toServer(Networks.WidgetUpdate, Networks.ofMouseMove(this.extended!!.id!!, hash, x, y))
		}
	}

	open fun onMouseClicked(x: Float, y: Float, button: Int) {
		if (focused) held = true

		if (this is WidgetCollection && !(extended != null && !extended!!.client)) {
			this.widgets.forEach {
				it.onMouseClicked(x, y, button)
			}
		}

		if (focused && this.extended != null && this.extended!!.handler != null && this.extended!!.client && synchronize.contains(Networks.MouseClicked)) {
			Networks.toServer(Networks.WidgetUpdate, Networks.ofMouseClick(this.extended!!.id!!, hash, x, y, button))
		}
	}

	open fun onMouseReleased(x: Float, y: Float, button: Int) {
		held = false

		if (this is WidgetCollection && !(extended != null && !extended!!.client)) {
			this.widgets.forEach {
				it.onMouseReleased(x, y, button)
			}
		}
		if (focused && this.extended != null && this.extended!!.handler != null && this.extended!!.client && synchronize.contains(Networks.MouseReleased)) {
			Networks.toServer(Networks.WidgetUpdate, Networks.ofMouseRelease(this.extended!!.id!!, hash, x, y, button))
		}
	}

	open fun onMouseDragged(x: Float, y: Float, button: Int, deltaX: Double, deltaY: Double) {
		if (this is WidgetCollection && !(extended != null && !extended!!.client)) {
			this.widgets.forEach {
				it.onMouseDragged(x, y, button, deltaX, deltaY)
			}
		}

		if (focused && this.extended != null && this.extended!!.handler != null && this.extended!!.client && synchronize.contains(Networks.MouseDragged)) {
			Networks.toServer(Networks.WidgetUpdate, Networks.ofMouseDrag(this.extended!!.id!!, hash, x, y, button, deltaX, deltaY))
		}
	}

	open fun onMouseScrolled(x: Float, y: Float, deltaY: Double) {
		if (this is WidgetCollection && !(extended != null && !extended!!.client)) {
			this.widgets.forEach {
				it.onMouseScrolled(x, y, deltaY)
			}
		}

		if (focused && this.extended != null && this.extended!!.handler != null && this.extended!!.client && synchronize.contains(Networks.MouseScrolled)) {
			Networks.toServer(Networks.WidgetUpdate, Networks.ofMouseScroll(this.extended!!.id!!, hash, x, y, deltaY))
		}
	}

	open fun onKeyPressed(keyCode: Int, scanCode: Int, keyModifiers: Int) {
		if (this is WidgetCollection && !(extended != null && !extended!!.client)) {
			this.widgets.forEach {
				it.onKeyPressed(keyCode, scanCode, keyModifiers)
			}
		}

		if (focused && this.extended != null && this.extended!!.handler != null && this.extended!!.client && synchronize.contains(Networks.KeyPressed)) {
			Networks.toServer(Networks.WidgetUpdate, Networks.ofKeyPress(this.extended!!.id!!, hash, keyCode, scanCode, keyModifiers))
		}
	}

	open fun onKeyReleased(keyCode: Int, character: Int, keyModifiers: Int) {
		if (this is WidgetCollection && !(extended != null && !extended!!.client)) {
			this.widgets.forEach {
				it.onKeyReleased(keyCode, character, keyModifiers)
			}
		}

		if (focused && this.extended != null && this.extended!!.handler != null && this.extended!!.client && synchronize.contains(Networks.KeyReleased)) {
			Networks.toServer(Networks.WidgetUpdate, Networks.ofKeyRelease(this.extended!!.id!!, hash, keyCode, character, keyModifiers))
		}
	}

	open fun onCharacterTyped(character: Char, keyCode: Int) {
		if (this is WidgetCollection && !(extended != null && !extended!!.client)) {
			this.widgets.forEach {
				it.onCharacterTyped(character, keyCode)
			}
		}

		if (focused && this.extended != null && this.extended!!.handler != null && this.extended!!.client && synchronize.contains(Networks.CharacterTyped)) {
			Networks.toServer(Networks.WidgetUpdate, Networks.ofCharType(this.extended!!.id!!, hash, character, keyCode))
		}
	}

	open fun onFocusGained() {
		if (this is WidgetCollection && !(extended != null && !extended!!.client)) {
			this.widgets.forEach {
				it.onFocusGained()
			}
		}

		if (focused && this.extended != null && this.extended!!.handler != null && this.extended!!.client && synchronize.contains(Networks.FocusGained)) {
			Networks.toServer(Networks.WidgetUpdate, Networks.ofFocusGain(this.extended!!.id!!, hash))
		}
	}

	open fun onFocusReleased() {
		if (this is WidgetCollection && !(extended != null && !extended!!.client)) {
			this.widgets.forEach {
				it.onFocusReleased()
			}
		}

		if (focused && this.extended != null && this.extended!!.handler != null && this.extended!!.client && synchronize.contains(Networks.FocusReleased)) {
			Networks.toServer(Networks.WidgetUpdate, Networks.ofFocusRelease(this.extended!!.id!!, hash))
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
		focus(Positions.mouseX, Positions.mouseY)

		parent?.onLayoutChanged()
		this.extended?.onLayoutChanged()
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