package dev.vini2003.hammer.gui.api.common.widget.bar;

import dev.vini2003.hammer.core.api.client.scissor.Scissors;
import dev.vini2003.hammer.core.api.client.texture.TiledSpriteTexture;
import dev.vini2003.hammer.core.api.client.texture.base.Texture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

public class SpriteBarWidget extends BarWidget {
	protected DoubleSupplier maximumSupplier = () -> 1.0D;
	protected DoubleSupplier currentSupplier = () -> 0.5D;
	
	protected Supplier<Sprite> foregroundSpriteSupplier = () -> null;
	protected Supplier<Sprite> backgroundSpriteSupplier = () -> null;
	
	protected Supplier<Texture> foregroundTextureSupplier = () -> STANDARD_FOREGROUND_TEXTURE;
	protected Supplier<Texture> backgroundTextureSupplier = () -> STANDARD_BACKGROUND_TEXTURE;
	
	protected float stepWidth = -1.0F;
	protected float stepHeight = -1.0F;
	
	protected boolean smooth = false;
	protected boolean invert = false;
	
	@Override
	public double getMaximum() {
		return maximumSupplier.getAsDouble();
	}
	
	@Override
	public double getCurrent() {
		return currentSupplier.getAsDouble();
	}
	
	@Override
	public Texture getForegroundTexture() {
		return foregroundTextureSupplier.get();
	}
	
	@Override
	public Texture getBackgroundTexture() {
		return backgroundTextureSupplier.get();
	}
	
	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider, float tickDelta) {
		var foregroundWidth = getWidth() / getMaximum() * getCurrent();
		var foregroundHeight = getHeight() / getMaximum() * getCurrent();
		
		if (stepWidth != 1.0F) {
			foregroundWidth -= foregroundWidth % stepWidth;
		}
		
		if (stepHeight != 1.0F) {
			foregroundHeight -= foregroundHeight % stepHeight;
		}
		
		if (!smooth) {
			foregroundWidth = (int) foregroundWidth;
			foregroundHeight = (int) foregroundHeight;
		}
		
		var foregroundTexture = getForegroundTexture();
		var backgroundTexture = getBackgroundTexture();
		
		if (vertical) {
			backgroundTexture.draw(matrices, provider, getX(), getY(), getWidth(), getHeight());
			
			var scissors = (Scissors) null;
			
			if (stepHeight == -1.0F) {
				if (!invert) {
					scissors = new Scissors(getX(), (float) (getY() + (getHeight() - foregroundHeight)), getWidth(), (float) foregroundHeight, provider);
				} else {
					scissors = new Scissors(getX(), getY(), getWidth(), (float) foregroundHeight, provider);
				}
			}
			
			if (!invert) {
				foregroundTexture.draw(matrices, provider, getX(), (float) (getY() + (getHeight() - foregroundHeight)), getWidth(), (float) foregroundHeight);
			} else {
				foregroundTexture.draw(matrices, provider, getX(), getY(), getWidth(), (float) foregroundHeight);
			}
			
			if (stepHeight == -1.0F) {
				scissors.destroy();
			}
		}
		
		if (horizontal) {
			backgroundTexture.draw(matrices, provider, getX(), getY(), getWidth(), getHeight());
			
			var scissors = (Scissors) null;
			
			if (stepWidth == -1.0F) {
				if (!invert) {
					scissors = new Scissors(getX(), getY(), (float) foregroundWidth, getHeight(), provider);
				} else {
					scissors = new Scissors((float) (getX() + (getWidth() - foregroundWidth)), getY(), (float) foregroundWidth, getHeight(), provider);
				}
			}
			
			if (!invert) {
				foregroundTexture.draw(matrices, provider, getX(), getY(), (float) foregroundWidth, getHeight());
			} else {
				foregroundTexture.draw(matrices, provider, (float) (getX() + (getWidth() - foregroundWidth)), getY(), (float) foregroundWidth, getHeight());
			}
			
			if (stepWidth == -1.0F) {
				scissors.destroy();
			}
		}
	}
	
	public Sprite getForegroundSprite() {
		return foregroundSpriteSupplier.get();
	}
	
	public Sprite getBackgroundSprite() {
		return backgroundSpriteSupplier.get();
	}
	
	public void setMaximum(DoubleSupplier maximumSupplier) {
		this.maximumSupplier = maximumSupplier;
	}
	
	public void setMaximum(double maximum) {
		setMaximum(() -> maximum);
	}
	
	public void setCurrent(DoubleSupplier currentSupplier) {
		this.currentSupplier = currentSupplier;
	}
	
	public void setCurrent(double current) {
		setCurrent(() -> current);
	}
	
	public void setForegroundSprite(Supplier<Sprite> foregroundSpriteSupplier) {
		this.foregroundSpriteSupplier = foregroundSpriteSupplier;
		
		setForegroundTexture(() -> new TiledSpriteTexture(getForegroundSprite()));
	}
	
	public void setForegroundSprite(Sprite foregroundSprite) {
		setForegroundSprite(() -> foregroundSprite);
	}
	
	public void setBackgroundSprite(Supplier<Sprite> backgroundSpriteSupplier) {
		this.backgroundSpriteSupplier = backgroundSpriteSupplier;
		
		setBackgroundTexture(() -> new TiledSpriteTexture(getBackgroundSprite()));
	}
	
	public void setBackgroundSprite(Sprite backgroundSprite) {
		setBackgroundSprite(() -> backgroundSprite);
	}
	
	public void setForegroundTexture(Supplier<Texture> foregroundTextureSupplier) {
		this.foregroundTextureSupplier = foregroundTextureSupplier;
	}
	
	public void setForegroundTexture(Texture foregroundTexture) {
		setForegroundTexture(() -> foregroundTexture);
	}
	
	public void setBackgroundTexture(Supplier<Texture> backgroundTextureSupplier) {
		this.backgroundTextureSupplier = backgroundTextureSupplier;
	}
	
	public void setBackgroundtexture(Texture backgroundtexture) {
		setBackgroundTexture(() -> backgroundtexture);
	}
	
	public void setStepWidth(float stepWidth) {
		this.stepWidth = stepWidth;
	}
	
	public void setStepHeight(float stepHeight) {
		this.stepHeight = stepHeight;
	}
	
	public void setSmooth(boolean smooth) {
		this.smooth = smooth;
	}
	
	public void setInvert(boolean invert) {
		this.invert = invert;
	}
}
