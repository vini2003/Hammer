package dev.vini2003.hammer.border.common.border

import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.border.WorldBorder
import net.minecraft.world.border.WorldBorderStage

interface CubicWorldBorderArea {
	val boundEast: Double
	
	val boundWest: Double
	
	val boundNorth: Double
	
	val boundSouth: Double
	
	val boundUp: Double
	
	val boundDown: Double
	
	val size: Double
	
	val shrinkingSpeed: Double
	
	val sizeLerpTime: Long
	
	val sizeLerpTarget: Double
	
	val stage: WorldBorderStage
	
	fun onMaxRadiusChanged(): Unit
	
	fun onCenterChanged(): Unit
	
	val areaInstance: CubicWorldBorderArea
	
	val border: WorldBorder
	
	@Suppress("INAPPLICABLE_JVM_NAME")
	@get:JvmName("asVoxelShape")
	val voxelShape: VoxelShape
}