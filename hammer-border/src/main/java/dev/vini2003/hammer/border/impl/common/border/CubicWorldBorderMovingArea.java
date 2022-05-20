package dev.vini2003.hammer.border.impl.common.border;

import net.minecraft.util.Util;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.border.WorldBorderStage;

public class CubicWorldBorderMovingArea implements CubicWorldBorderArea {
	private WorldBorder border;
	private CubicWorldBorder cubicBorder;
	
	private double oldSize;
	private double newSize;
	
	private double timeDuration;
	
	private long timeStart = 0L;
	private long timeEnd = 0L;
	
	public CubicWorldBorderMovingArea(WorldBorder border, double oldSize, double newSize, double timeDuration) {
		this.border = border;
		
		this.oldSize = oldSize;
		this.newSize = newSize;
		
		this.timeDuration = timeDuration;
	}
	
	@Override
	public double getBoundWest() {
		return MathHelper.clamp(border.getCenterX() - getSize() / 2.0D, (-border.getMaxRadius()), border.getMaxRadius());
	}
	
	@Override
	public double getBoundNorth() {
		return MathHelper.clamp(border.getCenterZ() - getSize() / 2.0D, (-border.getMaxRadius()), border.getMaxRadius());
	}
	
	@Override
	public double getBoundEast() {
		return MathHelper.clamp(border.getCenterX() + getSize() / 2.0D, (-border.getMaxRadius()), border.getMaxRadius());
	}
	
	@Override
	public double getBoundSouth() {
		return MathHelper.clamp(border.getCenterZ() + getSize() / 2.0D, (-border.getMaxRadius()), border.getMaxRadius());
	}
	
	@Override
	public double getBoundDown() {
		return MathHelper.clamp(cubicBorder.getCenterY() - border.getSize() / 2.0D, (-border.getMaxRadius()), border.getMaxRadius());
	}
	
	@Override
	public double getBoundUp() {
		return MathHelper.clamp(cubicBorder.getCenterY() + border.getSize() / 2.0D, (-border.getMaxRadius()), border.getMaxRadius());
	}
	
	@Override
	public double getSize() {
		var delta = (Util.getMeasuringTimeMs() - timeStart) / timeDuration;
		
		return delta < 1.0 ? MathHelper.lerp(delta, oldSize, newSize) : newSize;
	}
	
	@Override
	public double getShrinkingSpeed() {
		return Math.abs(oldSize - newSize) / (double) (timeEnd - timeStart);
	}
	
	@Override
	public long getSizeLerpTime() {
		return timeEnd - Util.getMeasuringTimeMs();
	}
	
	@Override
	public double getSizeLerpTarget() {
		return newSize;
	}
	
	@Override
	public WorldBorderStage getStage() {
		return newSize < oldSize ? WorldBorderStage.SHRINKING : WorldBorderStage.GROWING;
	}
	
	@Override
	public void onCenterChanged() {
	
	}
	
	@Override
	public void onMaxRadiusChanged() {
	
	}
	
	@Override
	public WorldBorder getBorder() {
		return border;
	}
	
	@Override
	public CubicWorldBorderArea getAreaInstance() {
		return getSizeLerpTime() <= 0L ? new CubicWorldBorderStaticArea(border, newSize) : this;
	}
	
	@Override
	public VoxelShape asVoxelShape() {
		return VoxelShapes.combineAndSimplify(
				VoxelShapes.UNBOUNDED,
				VoxelShapes.cuboid(
						Math.floor(getBoundWest()),
						Math.floor(getBoundDown()),
						Math.floor(getBoundNorth()),
						Math.ceil(getBoundEast()),
						Math.ceil(getBoundUp()),
						Math.ceil(getBoundSouth())
				),
				BooleanBiFunction.ONLY_FIRST
		);
	}
}
