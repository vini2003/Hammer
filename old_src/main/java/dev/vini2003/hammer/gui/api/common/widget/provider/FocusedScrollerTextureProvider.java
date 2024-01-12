package dev.vini2003.hammer.gui.api.common.widget.provider;

import dev.vini2003.hammer.core.api.client.texture.base.Texture;

import java.util.function.Supplier;

public interface FocusedScrollerTextureProvider {
	Supplier<Texture> getFocusedScrollerTexture();
	
	void setFocusedScrollerTexture(Supplier<Texture> backgroundSprite);
	
	default void setFocusedScrollerTexture(Texture backgroundSprite) {
		setFocusedScrollerTexture(() -> backgroundSprite);
	}
}
