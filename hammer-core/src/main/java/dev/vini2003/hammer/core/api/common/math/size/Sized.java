package dev.vini2003.hammer.core.api.common.math.size;

public interface Sized extends SizeHolder {
	Size getSize();
	
	void setSize(Size size);
	
	default void setSize(float width, float height, float length) {
		setSize(new Size(width, height, length));
	}
	
	default void setSize(float width, float height) {
		setSize(new Size(width, height));
	}
	
	@Override
	default float getWidth() {
		return getSize().getWidth();
	}
	
	@Override
	default float getHeight() {
		return getSize().getHeight();
	}
	
	@Override
	default float getLength() {
		return getSize().getLength();
	}
	
	default void setWidth(float width) {
		setSize(new Size(width, getHeight(), getLength()));
	}
	
	default void setHeight(float height) {
		setSize(new Size(getWidth(), height, getLength()));
	}
	
	default void setLength(float length) {
		setSize(new Size(getWidth(), getHeight(), length));
	}
}
