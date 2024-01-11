package dev.vini2003.hammer.gui.api.common.widget.provider;

import net.minecraft.client.texture.Sprite;

import java.util.function.Supplier;

public interface BackgroundSpriteProvider {
	Supplier<Sprite> getBackgroundSprite();
	
	void setBackgroundSprite(Supplier<Sprite> backgroundSprite);
	
	default void setBackgroundSprite(Sprite backgroundSprite) {
		setBackgroundSprite(() -> backgroundSprite);
	}
}
