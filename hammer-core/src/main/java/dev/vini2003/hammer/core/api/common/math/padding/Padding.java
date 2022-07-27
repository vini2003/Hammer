package dev.vini2003.hammer.core.api.common.math.padding;

public class Padding {
	private final float left;
	private final float right;
	private final float top;
	private final float bottom;
	
	public Padding(float left, float right, float top, float bottom) {
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
	}
	
	public float getLeft() {
		return left;
	}
	
	public float getRight() {
		return right;
	}
	
	public float getTop() {
		return top;
	}
	
	public float getBottom() {
		return bottom;
	}
}
