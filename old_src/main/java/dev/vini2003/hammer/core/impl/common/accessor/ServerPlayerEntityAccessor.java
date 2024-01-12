package dev.vini2003.hammer.core.impl.common.accessor;

import dev.vini2003.hammer.core.api.common.exception.NoMixinException;
import net.minecraft.text.Text;

public interface ServerPlayerEntityAccessor {
	default void hammer$clearTitle() {
		throw new NoMixinException();
	}
	
	default void hammer$resetTitle() {
		throw new NoMixinException();
	}
	
	default void hammer$setTitle(Text title) {
		throw new NoMixinException();
	}
	
	default void hammer$setSubtitle(Text subtitle) {
		throw new NoMixinException();
	}
	
	default void hammer$setOverlay(Text overlay) {
		throw new NoMixinException();
	}
}
