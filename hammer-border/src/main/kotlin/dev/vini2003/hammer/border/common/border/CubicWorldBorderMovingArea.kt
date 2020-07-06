package dev.vini2003.hammer.border.common.border

import net.minecraft.util.Util
import net.minecraft.util.function.BooleanBiFunction
import net.minecraft.util.math.MathHelper
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.border.WorldBorder
import net.minecraft.world.border.WorldBorderStage
import kotlin.math.abs
import kotlin.math.ceil

class CubicWorldBorderMovingArea(override val border: WorldBorder, private val oldSize: Double, private val newSize: Double, private val timeDuration: Double) : CubicWorldBorderArea {
	private var timeEnd = 0L
	private var timeStart = 0L
	
	val cubicBorder
		get() = border as CubicWorldBorder
	
	init {
		timeStart = Util.getMeasuringTimeMs()
		timeEnd = (timeStart + timeDuration).toLong()
	}
	
	override val boundWest: Double
		get() = MathHelper.clamp(
            border.centerX - size / 2.0,
            (-border.maxRadius).toDouble(),
            border.maxRadius.toDouble()
        )
	
	override val boundNorth: Double
		get() = MathHelper.clamp(
            border.centerZ - size / 2.0,
            (-border.maxRadius).toDouble(),
            border.maxRadius.toDouble()
        )
	
	override val boundEast: Double
		get() = MathHelper.clamp(
            border.centerX + size / 2.0,
            (-border.maxRadius).toDouble(),
            border.maxRadius.toDouble()
        )
	
	override val boundSouth: Double
		get() = MathHelper.clamp(
            border.centerZ + size / 2.0,
            (-border.maxRadius).toDouble(),
            border.maxRadius.toDouble()
        )
	
	override val boundDown: Double
		get() = MathHelper.clamp(
            cubicBorder.centerY - border.size / 2.0,
            (-border.maxRadius).toDouble(),
            border.maxRadius.toDouble()
        )
	
	override val boundUp: Double
		get() = MathHelper.clamp(
            cubicBorder.centerY + border.size / 2.0,
            (-border.maxRadius).toDouble(),
            border.maxRadius.toDouble()
        )
	
	override val size: Double
		get() {
			val delta = (Util.getMeasuringTimeMs() - timeStart).toDouble() / timeDuration
			
			if (delta < 1.0) {
				return MathHelper.lerp(delta, oldSize, newSize)
			} else {
				return newSize
			}
		}
	
	override val shrinkingSpeed: Double
		get() = abs(oldSize - newSize) / (timeEnd - timeStart).toDouble()
	
	override val sizeLerpTime: Long
		get() = timeEnd - Util.getMeasuringTimeMs()
	
	override val sizeLerpTarget: Double
		get() = newSize
	
	override val stage: WorldBorderStage
		get() {
			if (newSize < oldSize) {
				return WorldBorderStage.SHRINKING
			} else {
				return WorldBorderStage.GROWING
			}
		}
	
	override fun onCenterChanged() = Unit
	
	override fun onMaxRadiusChanged() = Unit
	
	override val areaInstance: CubicWorldBorderArea
		get() {
			if (sizeLerpTime <= 0L) {
				return CubicWorldBorderStaticArea(border, newSize)
			} else {
				return this
			}
		}
	
	override val voxelShape: VoxelShape
		get() = VoxelShapes.combineAndSimplify(
            VoxelShapes.UNBOUNDED,
            VoxelShapes.cuboid(
                ceil(boundWest),
                ceil(boundDown),
                ceil(boundNorth),
                ceil(boundEast),
                ceil(boundUp),
                ceil(boundSouth)
            ),
            BooleanBiFunction.ONLY_FIRST
        )
}