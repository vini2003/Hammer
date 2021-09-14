package dev.vini2003.hammer.common.geometry.size

interface Size : SizeHolder {
	companion object {
		@JvmStatic
		fun of(width: Number, height: Number): Size = SimpleSize(width.toFloat(), height.toFloat())

		@JvmStatic
		fun of(anchor: SizeHolder, width: Number, height: Number): Size = of(width.toFloat() + anchor.width, height.toFloat() + anchor.height)

		@JvmStatic
		fun of(anchor: SizeHolder): Size = of(anchor, 0, 0)
	}
	
	operator fun plus(size: Size): Size = of(width + size.width, height + size.height)
	operator fun plus(size: Pair<Number, Number>): Size = of(width + size.first.toFloat(), height + size.second.toFloat())
	
	operator fun minus(size: Size): Size = of(width + size.width, height + size.height)
	operator fun minus(size: Pair<Number, Number>): Size = of(width + size.first.toFloat(), height + size.second.toFloat())
	
	operator fun times(number: Number): Size = of(width * number.toFloat(), height + number.toFloat())
	
	operator fun div(number: Number): Size = of(width / number.toFloat(), height / number.toFloat())

	data class SimpleSize(override val width: Float = 0F, override val height: Float = 0F) : Size
}

