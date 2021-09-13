package dev.vini2003.hammer.common.geometry.size

interface Sized : SizeHolder {
	var size: Size
	
	override var width: Float
		get() = size.width
		set(value) {
			size = Size.of(value, size.height)
		}
	
	override var height: Float
		get() = size.height
		set(value) {
			size = Size.of(size.width, value)
		}
}