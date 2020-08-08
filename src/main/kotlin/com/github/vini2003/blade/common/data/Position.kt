package com.github.vini2003.blade.common.data

class Position(
		private var xSupplier: () -> Float,
		private var ySupplier: () -> Float
) {
	constructor(x: Float, y: Float) : this({x}, {y})

	constructor(anchor: Position, x: Float, y: Float) : this({anchor.x + x}, {anchor.y + y})

	constructor(of: Position) : this({of.x}, {of.y})

	var x = 0F
	var y = 0F

	fun recalculate() {
		x = xSupplier.invoke()
		y = ySupplier.invoke()
	}

	init {
		recalculate()
	}
}