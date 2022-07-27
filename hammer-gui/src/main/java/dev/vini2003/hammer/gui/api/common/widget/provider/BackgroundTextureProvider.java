package dev.vini2003.hammer.gui.api.common.widget.provider;

import dev.vini2003.hammer.core.api.client.texture.base.Texture;

import java.util.function.Supplier;

public interface BackgroundTextureProvider {
	Supplier<Texture> getBackgroundTexture();
	
	void setBackgroundTexture(Supplier<Texture> backgroundTexture);
	
	default void setBackgroundTexture(Texture backgroundTexture) {
		setBackgroundTexture(() -> backgroundTexture);
	}
}
