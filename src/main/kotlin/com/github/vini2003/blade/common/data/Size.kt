package com.github.vini2003.blade.common.data

class Size(
		private var widthSupplier: () -> Float,
		private var heightSupplier: () -> Float
) {
	constructor(width: Float, height: Float) : this({width}, {height})

	var width: Float = 0.0F
	var height: Float = 0.0F

	fun recalculate() {
		width = widthSupplier.invoke()
		height = heightSupplier.invoke()
	}

	init {
		recalculate()
	}
}