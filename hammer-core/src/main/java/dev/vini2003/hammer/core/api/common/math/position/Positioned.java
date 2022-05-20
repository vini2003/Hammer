package dev.vini2003.hammer.core.api.common.math.position;

public interface Positioned extends PositionHolder {
	Position getPosition();
	
	void setPosition(Position position);
	
	default void setPosition(float x, float y, float z) {
		setPosition(new Position(x, y, z));
	}
	
	default void setPosition(float x, float y) {
		setPosition(new Position(x, y));
	}
	
	@Override
	default float getX() {
		return getPosition().getX();
	}
	
	@Override
	default float getY() {
		return getPosition().getY();
	}
	
	@Override
	default float getZ() {
		return getPosition().getZ();
	}
	
	default void setX(float x) {
		setPosition(new Position(x, getX(), getZ()));
	}
	
	default void setY(float y) {
		setPosition(new Position(getX(), y, getZ()));
	}
	
	default void setZ(float z) {
		setPosition(new Position(getX(), getY(), z));
	}
}
