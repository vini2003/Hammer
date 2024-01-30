package dev.vini2003.hammer.core.impl.common.accessor;

import dev.vini2003.hammer.core.api.common.data.TrackedDataHandler;
import dev.vini2003.hammer.core.api.common.exception.NoMixinException;

public interface EntityAccessor {
	default <T> TrackedDataHandler<T> hammer$registerTrackedData(T defaultValue, String id) {
		throw new NoMixinException();
	}
}
