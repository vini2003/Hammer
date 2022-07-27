package dev.vini2003.hammer.gui.api.common.widget.provider;

import dev.vini2003.hammer.core.api.client.texture.base.Texture;

import java.util.function.Supplier;

public interface TextureProvider {
	Supplier<Texture> getTexture();
	
	void setTexture(Supplier<Texture> texture);
	
	default void setTexture(Texture texture) {
		setTexture(() -> texture);
	}
}
