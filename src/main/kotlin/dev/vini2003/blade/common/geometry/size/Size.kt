package dev.vini2003.blade.common.geometry.size

interface Size : SizeHolder {
	companion object {
		@JvmStatic
		fun of(width: Number, height: Number): Size = SimpleSize(width.toFloat(), height.toFloat())

		@JvmStatic
		fun of(anchor: SizeHolder, width: Number, height: Number): Size = of(width.toFloat() + anchor.width, height.toFloat() + anchor.height)

		@JvmStatic
		fun of(anchor: SizeHolder): Size = of(anchor, 0, 0)

		@JvmStatic
		fun absolute(anchor: SizeHolder): Size = of(anchor.width, anchor.height)
	}

	data class SimpleSize(override val width: Float = 0F, override val height: Float = 0F) : Size
}

