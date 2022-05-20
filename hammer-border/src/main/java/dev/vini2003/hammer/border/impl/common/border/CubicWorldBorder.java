package dev.vini2003.hammer.border.impl.common.border;

public interface CubicWorldBorder {
	double getCenterY();
	
	double getBoundUp();
	
	double getBoundDown();
	
	boolean contains(double x, double y, double z);
	
	boolean contains(double x, double y, double z, double margin);
	
	void setCenter(double x, double y, double z);
	
	double getDistanceInsideBorder(double x, double y, double z);
}
