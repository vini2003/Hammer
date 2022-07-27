package dev.vini2003.hammer.gui.api.common.widget.provider;

import dev.vini2003.hammer.core.api.client.texture.base.Texture;

import java.util.function.Supplier;

public interface ScrollerTextureProvider {
	Supplier<Texture> getScrollerTexture();
	
	void setScrollerTexture(Supplier<Texture> backgroundSprite);
	
	default void setScrollerTexture(Texture backgroundSprite) {
		setScrollerTexture(() -> backgroundSprite);
	}
}
