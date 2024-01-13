package dev.vini2003.hammer.gui.api.common.widget.provider;

import dev.vini2003.hammer.core.api.client.texture.base.Texture;

import java.util.function.Supplier;

public interface EnabledTextureProvider {
	Supplier<Texture> getEnabledTexture();
	
	void setEnabledTexture(Supplier<Texture> backgroundTexture);
	
	default void setEnabledTexture(Texture backgroundTexture) {
		setEnabledTexture(() -> backgroundTexture);
	}
}
