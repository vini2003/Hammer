package com.github.vini2003.blade.common.data

interface Position : PositionHolder {
	fun offset(x: Number, y: Number): Position = of(this, x.toFloat(), y.toFloat())

	companion object {
		@JvmStatic
		fun of(x: Number, y: Number): Position = SimplePosition(x.toFloat(), y.toFloat())

		@JvmStatic
		fun of(x: () -> Number, y: () -> Number): Position = ProviderPosition(x, y)

		@JvmStatic
		fun of(anchor: PositionHolder, x: () -> Number, y: () -> Number): Position = of({ x().toFloat() + anchor.x }, { y().toFloat() + anchor.y })

		@JvmStatic
		fun of(anchor: PositionHolder, x: Number, y: Number): Position = of({ x.toFloat() + anchor.x }, { y.toFloat() + anchor.y })

		@JvmStatic
		fun absolute(anchor: PositionHolder): Position = of(anchor.x, anchor.y)
	}

	class ProviderPosition(
			private var xSupplier: () -> Number,
			private var ySupplier: () -> Number
	) : Position {
		override var x = 0F
		override var y = 0F

		override fun recalculate() {
			x = xSupplier.invoke().toFloat()
			y = ySupplier.invoke().toFloat()
		}

		init {
			recalculate()
		}
	}

	data class SimplePosition(
			override val x: Float = 0F,
			override val y: Float = 0F
	) : Position
}

interface Positioned : PositionHolder {
	var position: Position
	override var x: Float
		get() = position.x
		set(value) {
			position = Position.of({ value }, { position.y })
		}
	override var y: Float
		get() = position.y
		set(value) {
			position = Position.of({ position.x }, { value })
		}
}

interface PositionHolder : Recalculatale {
	val x: Float
	val y: Float
}