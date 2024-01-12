package dev.vini2003.hammer.gui.api.common.widget.provider;

import dev.vini2003.hammer.core.api.client.texture.base.Texture;

import java.util.function.Supplier;

public interface InactiveLeftTextureProvider {
	Supplier<Texture> getInactiveLeftTexture();
	
	void setInactiveLeftTexture(Supplier<Texture> backgroundTexture);
	
	default void setInactiveLeftTexture(Texture backgroundTexture) {
		setInactiveLeftTexture(() -> backgroundTexture);
	}
}
