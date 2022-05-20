package dev.vini2003.hammer.core.api.common.math.size;

public interface SizeHolder {
	float getWidth();
	float getHeight();
	float getLength();
	
	default float getDepth() {
		return getLength();
	}
}
