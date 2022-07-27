package dev.vini2003.hammer.gui.api.common.widget.provider;

import dev.vini2003.hammer.core.api.client.texture.base.Texture;

import java.util.function.Supplier;

public interface FocusedTextureProvider {
	Supplier<Texture> getFocusedTexture();
	
	void setFocusedTexture(Supplier<Texture> backgroundTexture);
	
	default void setFocusedTexture(Texture backgroundTexture) {
		setFocusedTexture(() -> backgroundTexture);
	}
}
