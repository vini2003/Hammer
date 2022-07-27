package dev.vini2003.hammer.gui.api.common.widget.provider;

import dev.vini2003.hammer.core.api.client.texture.base.Texture;

import java.util.function.Supplier;

public interface ActiveRightTextureProvider {
	Supplier<Texture> getActiveRightTexture();
	
	void setActiveRightTexture(Supplier<Texture> backgroundTexture);
	
	default void setActiveRightTexture(Texture backgroundTexture) {
		setActiveRightTexture(() -> backgroundTexture);
	}
}
