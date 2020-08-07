package com.github.vini2003.blade.common.data

class Position(
		private var xSupplier: () -> Float,
		private var ySupplier: () -> Float
) {
	constructor(x: Float, y: Float) : this({x}, {y})

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