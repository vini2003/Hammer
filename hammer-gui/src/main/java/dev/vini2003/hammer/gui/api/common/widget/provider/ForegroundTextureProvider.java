package dev.vini2003.hammer.gui.api.common.widget.provider;

import dev.vini2003.hammer.core.api.client.texture.base.Texture;

import java.util.function.Supplier;

public interface ForegroundTextureProvider {
	Supplier<Texture> getForegroundTexture();
	
	void setForegroundTexture(Supplier<Texture> foregroundTexture);
	
	default void setForegroundTexture(Texture foregroundTexture) {
		setForegroundTexture(() -> foregroundTexture);
	}
}
