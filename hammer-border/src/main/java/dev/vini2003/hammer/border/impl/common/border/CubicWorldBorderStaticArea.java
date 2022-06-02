/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 vini2003
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.vini2003.hammer.border.impl.common.border;

import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.border.WorldBorderStage;

public class CubicWorldBorderStaticArea implements CubicWorldBorderArea {
	private WorldBorder border;
	private CubicWorldBorder cubicBorder;
	
	private double size;
	
	public CubicWorldBorderStaticArea(WorldBorder border, double size) {
		this.border = border;
		this.size = size;
		
		this.cubicBorder = (CubicWorldBorder) border;
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
		return size;
	}
	
	@Override
	public WorldBorderStage getStage() {
		return WorldBorderStage.STATIONARY;
	}
	
	@Override
	public double getShrinkingSpeed() {
		return 0.0D;
	}
	
	@Override
	public long getSizeLerpTime() {
		return 0L;
	}
	
	@Override
	public double getSizeLerpTarget() {
		return size;
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
		return this;
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
