package dev.vini2003.hammer.common.geometry.position

interface Positioned : PositionHolder {
	var position: Position

	override var x: Float
		get() = position.x
		set(value) {
			position = Position.of(value, position.y, position.z)
		}

	override var y: Float
		get() = position.y
		set(value) {
			position = Position.of(position.x, value, position.z)
		}
	
	override var z: Float
		get() = position.z
		set(value) {
			position = Position.of(position.x, position.y, value)
		}
}