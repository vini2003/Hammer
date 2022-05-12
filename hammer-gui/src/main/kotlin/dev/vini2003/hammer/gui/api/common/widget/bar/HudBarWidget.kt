package dev.vini2003.hammer.gui.api.common.widget.bar

/**
 * A [HudBarWidget] is a [BaseBarWidget] whose data is located through a sprite and
 * [maximum] and [current] are configurable, while its position and size are dictated
 * by its [side] on the hotbar.
 */
open class HudBarWidget @JvmOverloads constructor(
	maximum: () -> Float = { 100.0F },
	current: () -> Float = { 0.0F },
	var side: Side,
	var show: () -> Boolean = { true }
) : ImageBarWidget(maximum, current, 9.0F, 9.0F) {
	fun shouldShow() = show()
	
	enum class Side {
		LEFT,
		RIGHT
	}
}