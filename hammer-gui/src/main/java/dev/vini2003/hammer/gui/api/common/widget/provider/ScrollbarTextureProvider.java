package dev.vini2003.hammer.gui.api.common.widget.provider;

import dev.vini2003.hammer.core.api.client.texture.base.Texture;

import java.util.function.Supplier;

public interface ScrollbarTextureProvider {
	Supplier<Texture> getScrollbarTexture();
	
	void setScrollbarTexture(Supplier<Texture> backgroundSprite);
	
	default void setScrollbarTexture(Texture backgroundSprite) {
		setScrollbarTexture(() -> backgroundSprite);
	}
}
