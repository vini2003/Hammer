package dev.vini2003.hammer.core.api.common.util;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.vini2003.hammer.core.api.common.exception.NoPlatformImplementationException;

public class FluidUtil {
	@ExpectPlatform
	public static long getBucketCapacity() {
		throw new NoPlatformImplementationException();
	}
}
