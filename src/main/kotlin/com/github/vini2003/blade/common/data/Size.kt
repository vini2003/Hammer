package com.github.vini2003.blade.common.data

interface Size : SizeHolder {
	companion object {
		@JvmStatic
		fun of(width: Number, height: Number): Size = SimpleSize(width.toFloat(), height.toFloat())

		@JvmStatic
		fun of(width: () -> Number, height: () -> Number): Size = ProviderSize(width, height)

		@JvmStatic
		fun of(anchor: SizeHolder, width: () -> Number, height: () -> Number): Size = of({ width().toFloat() + anchor.width }, { height().toFloat() + anchor.height })

		@JvmStatic
		fun of(anchor: SizeHolder, width: Number, height: Number): Size = of({ width.toFloat() + anchor.width }, { height.toFloat() + anchor.height })

		@JvmStatic
		fun absolute(anchor: SizeHolder): Size = of(anchor.width, anchor.height)
	}

	class ProviderSize(
			private var widthSupplier: () -> Number,
			private var heightSupplier: () -> Number
	) : Size {
		override var width = 0F
		override var height = 0F

		override fun recalculate() {
			width = widthSupplier.invoke().toFloat()
			height = heightSupplier.invoke().toFloat()
		}

		init {
			recalculate()
		}
	}

	data class SimpleSize(
			override val width: Float = 0F,
			override val height: Float = 0F
	) : Size
}

interface Sized : SizeHolder {
	var size: Size
	override var width: Float
		get() = size.width
		set(value) {
			size = Size.of({ value }, { size.height })
		}
	override var height: Float
		get() = size.height
		set(value) {
			size = Size.of({ size.width }, { value })
		}
}

interface SizeHolder : Recalculatale {
	val width: Float
	val height: Float
}