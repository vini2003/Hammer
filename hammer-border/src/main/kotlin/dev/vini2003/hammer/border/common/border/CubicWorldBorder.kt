package dev.vini2003.hammer.border.common.border

interface CubicWorldBorder {
	val centerY: Double
	
	val boundUp: Double
	
	val boundDown: Double
	
	fun contains(x: Double, y: Double, z: Double): Boolean
	
	fun contains(x: Double, y: Double, z: Double, margin: Double): Boolean
	
	fun setCenter(x: Double, y: Double, z: Double)
	
	fun getDistanceInsideBorder(x: Double, y: Double, z: Double): Double
}