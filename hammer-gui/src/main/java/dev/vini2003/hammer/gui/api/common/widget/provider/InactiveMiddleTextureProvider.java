package dev.vini2003.hammer.gui.api.common.widget.provider;

import dev.vini2003.hammer.core.api.client.texture.base.Texture;

import java.util.function.Supplier;

public interface InactiveMiddleTextureProvider {
	Supplier<Texture> getInactiveMiddleTexture();
	
	void setInactiveMiddleTexture(Supplier<Texture> backgroundTexture);
	
	default void setInactiveMiddleTexture(Texture backgroundTexture) {
		setInactiveMiddleTexture(() -> backgroundTexture);
	}
}
