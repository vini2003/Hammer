package dev.vini2003.hammer.core.api.common.math.shape.modifier;

import dev.vini2003.hammer.core.api.common.math.position.Position;

public class TranslateModifier implements Modifier {
	private final float x;
	private final float y;
	private final float z;
	
	public TranslateModifier(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public Position modify(Position pos) {
		return new Position(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
	}
}
