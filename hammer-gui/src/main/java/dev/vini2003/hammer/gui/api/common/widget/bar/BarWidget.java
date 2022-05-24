package dev.vini2003.hammer.gui.api.common.widget.bar;

import com.google.common.collect.ImmutableList;
import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.texture.PartitionedTexture;
import dev.vini2003.hammer.core.api.client.texture.base.Texture;
import dev.vini2003.hammer.core.api.common.util.TextUtil;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import org.w3c.dom.Text;

import java.util.function.Supplier;

public abstract class BarWidget extends Widget {
	public static final Texture STANDARD_FOREGROUND_TEXTURE = new PartitionedTexture(HC.id("textures/widget/bar_foreground.png"), 18.0F, 18.0F, 0.055F, 0.055F, 0.055F, 0.055F);
	public static final Texture STANDARD_BACKGROUND_TEXTURE = new PartitionedTexture(HC.id("textures/widget/bar_background.png"), 18.0F, 18.0F, 0.055F, 0.055F, 0.055F, 0.055F);
	
	protected boolean horizontal = false;
	protected boolean vertical = true;
	
	protected BarWidget() {
		super();
		
		setTooltipSupplier(() -> {
			return ImmutableList.of(TextUtil.getPercentage(getCurrent(), getMaximum()));
		});
	}
	
	protected abstract double getMaximum();
	
	protected abstract double getCurrent();
	
	protected abstract Texture getForegroundTexture();
	
	protected abstract Texture getBackgroundTexture();
	
	public void setHorizontal(boolean horizontal) {
		this.horizontal = horizontal;
		
		if (horizontal) {
			setVertical(false);
		}
	}
	
	public void setVertical(boolean vertical) {
		this.vertical = vertical;
		
		if (vertical) {
			setHorizontal(false);
		}
	}
}
