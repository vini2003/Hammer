package dev.vini2003.hammer.gui.api.common.widget.bar;

import dev.vini2003.hammer.core.api.client.scissor.Scissors;
import dev.vini2003.hammer.core.api.client.texture.base.Texture;
import dev.vini2003.hammer.core.api.common.supplier.TextureSupplier;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

public class ImageBarWidget extends BarWidget {
	protected DoubleSupplier maximumSupplier = () -> 1.0F;
	protected DoubleSupplier currentSupplier = () -> 0.5F;
	
	protected TextureSupplier foregroundTextureSupplier = () -> STANDARD_FOREGROUND_TEXTURE;
	protected TextureSupplier backgroundTextureSupplier = () -> STANDARD_BACKGROUND_TEXTURE;
	
	protected boolean smooth = false;
	protected boolean scissor = true;
	protected boolean invert = false;
	
	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider, float tickDelta) {
		var foregroundWidth = getWidth() / getMaximum() * getCurrent();
		var foregroundHeight = getHeight() / getMaximum() * getCurrent();
		
		if (!smooth) {
			foregroundWidth = (int) foregroundWidth;
			foregroundHeight = (int) foregroundHeight;
		}
		
		var foregroundTexture = getForegroundTexture();
		var backgroundTexture = getBackgroundTexture();
		
		if (vertical) {
			backgroundTexture.draw(matrices, provider, getX(), getY(), getWidth(), getHeight());
			
			var scissors = (Scissors) null;
			
			if (scissor) {
				if (!invert) {
					scissors = new Scissors(getX(), (float) (getY() + (getHeight() - foregroundHeight)), getWidth(), (float) foregroundHeight, provider);
				} else {
					scissors = new Scissors(getX(), getY(), getWidth(), (float) foregroundHeight, provider);
				}
			}
			
			foregroundTexture.draw(matrices, provider, getX(), getY(), getWidth(), getHeight());
			
			if (scissor) {
				scissors.destroy();
			}
		}
		
		if (horizontal) {
			backgroundTexture.draw(matrices, provider, getX(), getY(), getWidth(), getHeight());
			
			var scissors = (Scissors) null;
			
			if (scissor) {
				if (!invert) {
					scissors = new Scissors(getX(), getY(), (float) foregroundWidth, getHeight(), provider);
				} else {
					scissors = new Scissors((float) (getX() + (getWidth() - foregroundWidth)), getY(), (float) foregroundWidth, getHeight(), provider);
				}
			}
			
			foregroundTexture.draw(matrices, provider, getX(), getY(), getWidth(), getHeight());
			
			if (scissor) {
				scissors.destroy();
			}
		}
	}
	
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
	
	public void setForegroundTexture(TextureSupplier foregroundTextureSupplier) {
		this.foregroundTextureSupplier = foregroundTextureSupplier;
	}
	
	public void setForegroundTexture(Texture foregroundTexture) {
		setForegroundTexture(() -> foregroundTexture);
	}
	
	public void setBackgroundTexture(TextureSupplier backgroundTextureSupplier) {
		this.backgroundTextureSupplier = backgroundTextureSupplier;
	}
	
	public void setBackgroundTexture(Texture backgroundTexture) {
		setBackgroundTexture(() -> backgroundTexture);
	}
	
	public void setSmooth(boolean smooth) {
		this.smooth = smooth;
	}
	
	public void setScissor(boolean scissor) {
		this.scissor = scissor;
	}
	
	public void setInvert(boolean invert) {
		this.invert = invert;
	}
}
