package dev.vini2003.hammer.border.common.border

import net.minecraft.util.function.BooleanBiFunction
import net.minecraft.util.math.MathHelper
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.border.WorldBorder
import net.minecraft.world.border.WorldBorderStage
import kotlin.math.ceil
import kotlin.math.floor

class CubicWorldBorderStaticArea(override val border: WorldBorder, override val size: Double) : CubicWorldBorderArea {
	val cubicBorder
		get() = border as CubicWorldBorder
	
	override val boundWest: Double
		get() = MathHelper.clamp(border.centerX - size / 2.0, (-border.maxRadius).toDouble(), border.maxRadius.toDouble())
	
	override val boundNorth: Double
		get() = MathHelper.clamp(border.centerZ - size / 2.0, (-border.maxRadius).toDouble(), border.maxRadius.toDouble())
	
	override val boundEast: Double
		get() = MathHelper.clamp(border.centerX + size / 2.0, (-border.maxRadius).toDouble(), border.maxRadius.toDouble())
	
	override val boundSouth: Double
		get() = MathHelper.clamp(border.centerZ + size / 2.0, (-border.maxRadius).toDouble(), border.maxRadius.toDouble())
	
	override val boundDown: Double
		get() = MathHelper.clamp(cubicBorder.centerY - border.size / 2.0, (-border.maxRadius).toDouble(), border.maxRadius.toDouble())
	
	override val boundUp: Double
		get() = MathHelper.clamp(cubicBorder.centerY + border.size / 2.0, (-border.maxRadius).toDouble(), border.maxRadius.toDouble())
	
	override val stage: WorldBorderStage
		get() = WorldBorderStage.STATIONARY
	
	override val shrinkingSpeed: Double
		get() = 0.0
	
	override val sizeLerpTime: Long
		get() = 0L
	
	override val sizeLerpTarget: Double
		get() = size
	
	override fun onCenterChanged() = Unit
	
	override fun onMaxRadiusChanged() = Unit
	
	override val areaInstance: CubicWorldBorderArea
		get() = this
	
	override val voxelShape: VoxelShape
		get() = VoxelShapes.combineAndSimplify(VoxelShapes.UNBOUNDED, VoxelShapes.cuboid(floor(boundWest), floor(boundDown), floor(boundNorth), ceil(boundEast), ceil(boundUp), ceil(boundSouth)), BooleanBiFunction.ONLY_FIRST)
}