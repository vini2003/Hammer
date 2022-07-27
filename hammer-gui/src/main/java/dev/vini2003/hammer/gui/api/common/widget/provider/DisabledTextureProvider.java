package dev.vini2003.hammer.gui.api.common.widget.provider;

import dev.vini2003.hammer.core.api.client.texture.base.Texture;

import java.util.function.Supplier;

public interface DisabledTextureProvider {
	Supplier<Texture> getDisabledTexture();
	
	void setDisabledTexture(Supplier<Texture> backgroundTexture);
	
	default void setDisabledTexture(Texture backgroundTexture) {
		setDisabledTexture(() -> backgroundTexture);
	}
}
