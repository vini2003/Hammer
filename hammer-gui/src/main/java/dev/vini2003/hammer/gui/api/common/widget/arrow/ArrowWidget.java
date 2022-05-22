package dev.vini2003.hammer.gui.api.common.widget.arrow;

import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.texture.ImageTexture;
import dev.vini2003.hammer.core.api.client.texture.base.Texture;
import dev.vini2003.hammer.core.api.common.supplier.TextureSupplier;
import dev.vini2003.hammer.gui.api.common.widget.bar.ImageBarWidget;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

public class ArrowWidget extends ImageBarWidget {
	public static final Texture STANDARD_VERTICAL_FOREGROUND_TEXTURE = new ImageTexture(HC.id("textures/widget/vertical_arrow_foreground.png"));
	public static final Texture STANDARD_VERTICAL_BACKGROUND_TEXTURE = new ImageTexture(HC.id("textures/widget/vertical_arrow_background.png"));
	
	public static final Texture STANDARD_HORIZONTAL_FOREGROUND_TEXTURE = new ImageTexture(HC.id("textures/widget/horizontal_arrow_foreground.png"));
	public static final Texture STANDARD_HORIZONTAL_BACKGROUND_TEXTURE = new ImageTexture(HC.id("textures/widget/horizontal_arrow_background.png"));
	
	private static final TextureSupplier STANDARD_VERTICAL_FOREGROUND_TEXTURE_SUPPLIER = () -> STANDARD_VERTICAL_FOREGROUND_TEXTURE;
	private static final TextureSupplier STANDARD_VERTICAL_BACKGROUND_TEXTURE_SUPPLIER = () -> STANDARD_VERTICAL_BACKGROUND_TEXTURE;
	
	private static final TextureSupplier STANDARD_HORIZONTAL_FOREGROUND_TEXTURE_SUPPLIER = () -> STANDARD_HORIZONTAL_FOREGROUND_TEXTURE;
	private static final TextureSupplier STANDARD_HORIZONTAL_BACKGROUND_TEXTURE_SUPPLIER = () -> STANDARD_HORIZONTAL_BACKGROUND_TEXTURE;
	
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
		
		if (horizontal && foregroundTextureSupplier == STANDARD_HORIZONTAL_FOREGROUND_TEXTURE_SUPPLIER && backgroundTextureSupplier == STANDARD_HORIZONTAL_BACKGROUND_TEXTURE_SUPPLIER) {
			foregroundTextureSupplier = STANDARD_HORIZONTAL_FOREGROUND_TEXTURE_SUPPLIER;
			backgroundTextureSupplier = STANDARD_HORIZONTAL_BACKGROUND_TEXTURE_SUPPLIER;
		}
	}
	
	@Override
	public void setVertical(boolean vertical) {
		super.setVertical(vertical);
		
		if (vertical && foregroundTextureSupplier == STANDARD_VERTICAL_FOREGROUND_TEXTURE_SUPPLIER && backgroundTextureSupplier == STANDARD_VERTICAL_BACKGROUND_TEXTURE_SUPPLIER) {
			foregroundTextureSupplier = STANDARD_VERTICAL_FOREGROUND_TEXTURE_SUPPLIER;
			backgroundTextureSupplier = STANDARD_VERTICAL_BACKGROUND_TEXTURE_SUPPLIER;
		}
	}
}

