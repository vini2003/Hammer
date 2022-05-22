package dev.vini2003.hammer.gui.api.common.widget.bar;

import com.google.common.collect.ImmutableList;
import dev.vini2003.hammer.core.api.client.scissor.Scissors;
import dev.vini2003.hammer.core.api.client.texture.TiledFluidTexture;
import dev.vini2003.hammer.core.api.client.texture.base.Texture;
import dev.vini2003.hammer.core.api.common.supplier.TextureSupplier;
import dev.vini2003.hammer.core.api.common.util.FluidTextUtil;
import dev.vini2003.hammer.core.api.common.util.TextUtil;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

public class FluidBarWidget extends BarWidget {
	protected DoubleSupplier maximumSupplier = () -> 1.0D;
	protected DoubleSupplier currentSupplier = () -> 0.5D;
	
	protected TextureSupplier foregroundTexture = () -> STANDARD_FOREGROUND_TEXTURE;
	protected TextureSupplier backgroundTexture = () -> STANDARD_BACKGROUND_TEXTURE;
	
	protected Supplier<StorageView<FluidVariant>> storageView = () -> null;
	
	protected boolean smooth = false;
	protected boolean tiled = true;
	
	public FluidBarWidget() {
		super();
		
		setTooltipSupplier(() -> {
			var storageView = this.storageView.get();
			
			if (storageView != null) {
				if (Screen.hasShiftDown()) {
					return FluidTextUtil.getDetailedStorageTooltips(storageView);
				} else {
					return FluidTextUtil.getShortenedStorageTooltips(storageView);
				}
			} else {
				return ImmutableList.of(TextUtil.getPercentage(getCurrent(), getMaximum()));
			}
		});
	}
	
	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider, float tickDelta) {
		var foregroundWidth = (getWidth() / getMaximum() * getCurrent());
		var foregroundHeight = (getHeight() / getMaximum() * getCurrent());
		
		if (!smooth) {
			foregroundWidth = (int) foregroundWidth;
			foregroundHeight = (int) foregroundHeight;
		}
		
		var foregroundTexture = getForegroundTexture();
		var backgroundTexture = getBackgroundTexture();
		
		if (vertical) {
			backgroundTexture.draw(matrices, provider, getX(), getY(), getWidth(), getHeight());
			
			var scissors = new Scissors(getX(), (float) (getY() + (getHeight() - foregroundHeight)), getWidth(), (float) foregroundHeight, provider);
			
			foregroundTexture.draw(matrices, provider, getX() + 1.0F, getY() + 1.0F, getWidth() - 2.0F, getHeight() - 2.0F);
			
			scissors.destroy();
		}
		
		if (horizontal) {
			backgroundTexture.draw(matrices, provider, getX(), getY(), getWidth(), getHeight());
			
			var scissors = new Scissors(getX(), getY(), (float) foregroundWidth, getHeight(), provider);
			
			foregroundTexture.draw(matrices, provider, getX() + 1.0F, getY() + 1.0F, getWidth() - 2.0F, getHeight() - 2.0F);
			
			scissors.destroy();
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
		return foregroundTexture.get();
	}
	
	@Override
	public Texture getBackgroundTexture() {
		return backgroundTexture.get();
	}
	
	public void setStorageView(Supplier<StorageView<FluidVariant>> storageViewSupplier) {
		if (storageViewSupplier.get() != null) {
			this.storageView = storageViewSupplier;
			
			this.maximumSupplier = () -> (float) storageViewSupplier.get().getCapacity();
			this.currentSupplier = () -> (float) storageViewSupplier.get().getAmount();
			
			this.foregroundTexture = () -> new TiledFluidTexture(storageViewSupplier.get().getResource());
			this.backgroundTexture = () -> STANDARD_BACKGROUND_TEXTURE;
		}
	}
	
	public void setStorageView(StorageView<FluidVariant> storageView) {
		setStorageView(() -> storageView);
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
		this.foregroundTexture = foregroundTextureSupplier;
	}
	
	public void setForegroundTexture(Texture foregroundTexture) {
		setForegroundTexture(() -> foregroundTexture);
	}

	public void setBackgroundTexture(TextureSupplier backgroundTextureSupplier) {
		this.backgroundTexture = backgroundTextureSupplier;
	}
	
	public void setBackgroundTexture(Texture backgroundTexture) {
		setBackgroundTexture(() -> backgroundTexture);
	}
	
	public boolean isSmooth() {
		return smooth;
	}
	
	public void setSmooth(boolean smooth) {
		this.smooth = smooth;
	}
	
	public boolean isTiled() {
		return tiled;
	}
	
	public void setTiled(boolean tiled) {
		this.tiled = tiled;
	}
}
