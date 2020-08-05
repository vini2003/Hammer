package com.github.vini2003.blade.common.data.geometry

import com.github.vini2003.blade.common.data.Position
import com.github.vini2003.blade.common.data.Size

class Rectangle(val position: Position, val size: Size) {
	companion object {
		fun empty(): Rectangle {
			return Rectangle(Position({ 0F }, { 0F }), Size({ 0F }, { 0F }))
		}
	}

	fun isWithin(x: Float, y: Float): Boolean {
		return x > position.x && x < position.x + size.width && y > position.y && y < position.y + size.height
	}

	fun relativeHeight(y: Float): Float {
		return (y - position.y) / size.height
	}

	fun relativeWidth(x: Float): Float {
		return (x - position.x) / size.width
	}
}