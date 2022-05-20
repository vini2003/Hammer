package dev.vini2003.hammer.border.impl.common.border;

import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.border.WorldBorderStage;

public interface CubicWorldBorderArea {
	double getBoundEast();
	
	double getBoundWest();
	
	double getBoundNorth();
	
	double getBoundSouth();
	
	double getBoundUp();
	
	double getBoundDown();
	
	double getSize();
	
	double getShrinkingSpeed();
	
	long getSizeLerpTime();
	
	double getSizeLerpTarget();
	
	WorldBorderStage getStage();
	
	void onMaxRadiusChanged();
	
	void onCenterChanged();
	
	CubicWorldBorderArea getAreaInstance();
	
	WorldBorder getBorder();
	
	VoxelShape asVoxelShape();
}
