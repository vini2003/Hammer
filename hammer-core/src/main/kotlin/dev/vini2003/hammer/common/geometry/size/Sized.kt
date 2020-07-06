package dev.vini2003.hammer.common.geometry.size

interface Sized : SizeHolder {
	var size: Size
	
	override var width: Float
		get() = size.width
		set(value) {
			size = Size.of(value, size.height, size.length)
		}
	
	override var height: Float
		get() = size.height
		set(value) {
			size = Size.of(size.width, value, size.length)
		}
	
	override var length: Float
		get() = size.length
		set(value) {
			size = Size.of(size.width, size.height, value)
		}
}