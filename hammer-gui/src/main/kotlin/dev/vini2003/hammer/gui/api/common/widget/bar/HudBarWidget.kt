package dev.vini2003.hammer.gui.api.common.widget.bar

import dev.vini2003.hammer.core.api.client.texture.BaseTexture
import dev.vini2003.hammer.core.api.client.texture.TiledImageTexture

/**
 * A [HudBarWidget] is a [BaseBarWidget] whose data is located through a sprite and
 * [maximum] and [current] are configurable, while its position and size are dictated
 * by its [side] on the hotbar.
 */
open class HudBarWidget @JvmOverloads constructor(
	var side: Side,
	var type: Type,
	maximum: () -> Float = { 100.0F },
	current: () -> Float = { 0.0F },
) : ImageBarWidget(maximum, current) {
	init {
		smooth = false
		
		if (type == Type.CONTINUOS) {
			scissor = true
		}
	}
	
	var show: () -> Boolean = { true }
	
	fun shouldShow() = show()
	
	enum class Side {
		LEFT,
		RIGHT
	}
	
	enum class Type {
		CONTINUOS,
		TILED
	}
}