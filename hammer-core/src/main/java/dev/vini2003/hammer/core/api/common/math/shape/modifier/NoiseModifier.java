package dev.vini2003.hammer.core.api.common.math.shape.modifier;

import dev.vini2003.hammer.core.api.common.math.position.Position;

import java.util.Random;

public class NoiseModifier implements Modifier {
	private final Random random;
	
	private final float magnitude;
	
	public NoiseModifier(Random random, float magnitude) {
		this.random = random;
		this.magnitude = magnitude;
	}
	
	@Override
	public Position modify(Position pos) {
		return new Position(
				pos.getX() + (random.nextFloat() * magnitude) * (random.nextFloat() * magnitude),
				pos.getY() + (random.nextFloat() * magnitude) * (random.nextFloat() * magnitude),
				pos.getZ() + (random.nextFloat() * magnitude) * (random.nextFloat() * magnitude)
		);
	}
}
