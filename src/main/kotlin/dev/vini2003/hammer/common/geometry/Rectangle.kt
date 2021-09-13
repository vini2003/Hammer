package dev.vini2003.hammer.common.geometry

import dev.vini2003.hammer.common.geometry.position.Position
import dev.vini2003.hammer.common.geometry.position.PositionHolder
import dev.vini2003.hammer.common.geometry.size.Size
import dev.vini2003.hammer.common.geometry.size.SizeHolder

class Rectangle(val position: PositionHolder, val size: SizeHolder) {
	companion object {
		@JvmStatic
		val Empty = Rectangle(Position.of(0F, 0F), Size.of(0F, 0F))
	}

	fun isWithin(x: Float, y: Float): Boolean {
		return x > position.x && x < position.x + size.width && y > position.y && y < position.y + size.height
	}
}