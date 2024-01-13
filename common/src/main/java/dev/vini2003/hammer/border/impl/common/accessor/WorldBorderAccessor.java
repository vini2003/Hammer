package dev.vini2003.hammer.border.impl.common.accessor;

import dev.vini2003.hammer.core.api.common.exception.NoMixinException;

public interface WorldBorderAccessor {
	default void hammer$setCenter(float x, float y, float z) {
		throw new NoMixinException();
	}
}
