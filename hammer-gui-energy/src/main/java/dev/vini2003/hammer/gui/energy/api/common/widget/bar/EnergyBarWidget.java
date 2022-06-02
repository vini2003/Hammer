package dev.vini2003.hammer.gui.energy.api.common.widget.bar;

import com.google.common.collect.ImmutableList;
import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.texture.ImageTexture;
import dev.vini2003.hammer.core.api.client.texture.base.Texture;
import dev.vini2003.hammer.core.api.common.util.TextUtil;
import dev.vini2003.hammer.gui.api.common.widget.bar.ImageBarWidget;
import dev.vini2003.hammer.gui.energy.api.common.util.EnergyTextUtil;
import net.minecraft.client.gui.screen.Screen;
import team.reborn.energy.api.EnergyStorage;

import java.util.function.Supplier;

public class EnergyBarWidget extends ImageBarWidget {
	public static final Texture STANDARD_FOREGROUND_TEXTURE = new ImageTexture(HC.id("textures/widget/energy_bar_filled.png"));
	public static final Texture STANDARD_BACKGROUND_TEXTURE = new ImageTexture(HC.id("textures/widget/energy_bar_background.png"));
	
	protected Supplier<EnergyStorage> storage = () -> null;
	
	public EnergyBarWidget() {
		foregroundTexture = () -> STANDARD_FOREGROUND_TEXTURE;
		backgroundTexture = () -> STANDARD_BACKGROUND_TEXTURE;
		
		setTooltipSupplier(() -> {
			if (storage.get() == null) {
				return ImmutableList.of(TextUtil.getEmpty());
			}
			
			if (Screen.hasShiftDown()) {
				return EnergyTextUtil.getDetailedTooltips(storage.get());
			} else {
				return EnergyTextUtil.getShortenedTooltips(storage.get());
			}
		});
	}
	
	public EnergyBarWidget(EnergyStorage storage) {
		this();
		
		current = () -> storage.getAmount();
		maximum = () -> storage.getCapacity();
	}
	
	public void setStorage(Supplier<EnergyStorage> storage) {
		this.storage = storage;
	}
}
