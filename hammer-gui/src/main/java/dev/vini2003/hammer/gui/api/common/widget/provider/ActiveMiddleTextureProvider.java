package dev.vini2003.hammer.gui.api.common.widget.provider;

import dev.vini2003.hammer.core.api.client.texture.base.Texture;

import java.util.function.Supplier;

public interface ActiveMiddleTextureProvider {
	Supplier<Texture> getActiveMiddleTexture();
	
	void setActiveMiddleTexture(Supplier<Texture> backgroundTexture);
	
	default void setActiveMiddleTexture(Texture backgroundTexture) {
		setActiveMiddleTexture(() -> backgroundTexture);
	}
}
