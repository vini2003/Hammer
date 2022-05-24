package dev.vini2003.hammer.gui.api.common.widget.arrow;

import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.texture.ImageTexture;
import dev.vini2003.hammer.core.api.client.texture.base.Texture;
import dev.vini2003.hammer.gui.api.common.widget.bar.ImageBarWidget;

public class ArrowWidget extends ImageBarWidget {
	public static final Texture STANDARD_VERTICAL_FOREGROUND_TEXTURE = new ImageTexture(HC.id("textures/widget/vertical_arrow_foreground.png"));
	public static final Texture STANDARD_VERTICAL_BACKGROUND_TEXTURE = new ImageTexture(HC.id("textures/widget/vertical_arrow_background.png"));
	
	public static final Texture STANDARD_HORIZONTAL_FOREGROUND_TEXTURE = new ImageTexture(HC.id("textures/widget/horizontal_arrow_foreground.png"));
	public static final Texture STANDARD_HORIZONTAL_BACKGROUND_TEXTURE = new ImageTexture(HC.id("textures/widget/horizontal_arrow_background.png"));
	
	public ArrowWidget() {
		super();
		
		setHorizontal(true);
		setVertical(false);
		
		setSmooth(false);
		setScissor(true);
	}
	
	@Override
	public void setHorizontal(boolean horizontal) {
		super.setHorizontal(horizontal);
		
		if (horizontal && foregroundTexture.get() == STANDARD_HORIZONTAL_FOREGROUND_TEXTURE && backgroundTexture.get() == STANDARD_HORIZONTAL_BACKGROUND_TEXTURE) {
			foregroundTexture = () -> STANDARD_HORIZONTAL_FOREGROUND_TEXTURE;
			backgroundTexture = () -> STANDARD_HORIZONTAL_BACKGROUND_TEXTURE;
		}
	}
	
	@Override
	public void setVertical(boolean vertical) {
		super.setVertical(vertical);
		
		if (vertical && foregroundTexture.get() == STANDARD_VERTICAL_FOREGROUND_TEXTURE && backgroundTexture.get() == STANDARD_VERTICAL_BACKGROUND_TEXTURE) {
			foregroundTexture = () -> STANDARD_VERTICAL_FOREGROUND_TEXTURE;
			backgroundTexture = () -> STANDARD_VERTICAL_BACKGROUND_TEXTURE;
		}
	}
}

