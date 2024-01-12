package dev.vini2003.hammer.gui.api.common.widget.provider;


import net.minecraft.client.texture.Sprite;

import java.util.function.Supplier;

public interface ForegroundSpriteProvider {
	Supplier<Sprite> getForegroundSprite();
	
	void setForegroundSprite(Supplier<Sprite> foregroundSprite);
	
	default void setForegroundSprite(Sprite foregroundSprite) {
		setForegroundSprite(() -> foregroundSprite);
	}
}
