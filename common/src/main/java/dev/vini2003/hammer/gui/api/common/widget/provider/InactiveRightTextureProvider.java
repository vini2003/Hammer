package dev.vini2003.hammer.gui.api.common.widget.provider;

import dev.vini2003.hammer.core.api.client.texture.base.Texture;

import java.util.function.Supplier;

public interface InactiveRightTextureProvider {
	Supplier<Texture> getInactiveRightTexture();
	
	void setInactiveRightTexture(Supplier<Texture> backgroundTexture);
	
	default void setInactiveRightTexture(Texture backgroundTexture) {
		setInactiveRightTexture(() -> backgroundTexture);
	}
}
