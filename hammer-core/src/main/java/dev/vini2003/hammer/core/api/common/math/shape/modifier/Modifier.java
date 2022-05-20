package dev.vini2003.hammer.core.api.common.math.shape.modifier;

import dev.vini2003.hammer.core.api.common.math.position.Position;

@FunctionalInterface
public interface Modifier {
	Position modify(Position pos);
}
