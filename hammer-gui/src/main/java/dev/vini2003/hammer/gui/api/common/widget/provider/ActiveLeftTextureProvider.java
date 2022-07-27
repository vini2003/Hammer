package dev.vini2003.hammer.gui.api.common.widget.provider;

import dev.vini2003.hammer.core.api.client.texture.base.Texture;

import java.util.function.Supplier;

public interface ActiveLeftTextureProvider {
	Supplier<Texture> getActiveLeftTexture();
	
	void setActiveLeftTexture(Supplier<Texture> backgroundTexture);
	
	default void setActiveLeftTexture(Texture backgroundTexture) {
		setActiveLeftTexture(() -> backgroundTexture);
	}
}
