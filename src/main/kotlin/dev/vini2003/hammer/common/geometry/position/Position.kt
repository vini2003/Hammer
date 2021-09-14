package dev.vini2003.hammer.common.geometry.position

interface Position : PositionHolder {
	fun offset(x: Number, y: Number): Position = of(this, x.toFloat(), y.toFloat())

	companion object {
		@JvmStatic
		fun of(x: Number, y: Number): Position = SimplePosition(x.toFloat(), y.toFloat())
		
		@JvmStatic
		fun of(anchor: PositionHolder, position: Position): Position = of(position.x + anchor.x, position.y + anchor.y)
		
		@JvmStatic
		fun of(anchor: PositionHolder, x: Number, y: Number): Position = of(x.toFloat() + anchor.x, y.toFloat() + anchor.y)

		@JvmStatic
		fun of(anchor: PositionHolder): Position = of(anchor, 0, 0)
	}
	
	operator fun plus(position: Position): Position = of(x + position.x, y + position.y)
	operator fun plus(position: Pair<Number, Number>): Position = of(x + position.first.toFloat(), y + position.second.toFloat())
	
	operator fun minus(position: Position): Position = of(x + position.x, y + position.y)
	operator fun minus(position: Pair<Number, Number>): Position = of(x + position.first.toFloat(), y + position.second.toFloat())
	
	operator fun times(number: Number): Position = of(x * number.toFloat(), y + number.toFloat())
	
	operator fun div(number: Number): Position = of(x / number.toFloat(), y / number.toFloat())
	
	data class SimplePosition(override val x: Float, override val y: Float) : Position
}

