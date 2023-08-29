package dev.vini2003.hammer.gui.api.common.widget.image;

import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.texture.PartitionedTexture;
import dev.vini2003.hammer.core.api.client.texture.base.Texture;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import dev.vini2003.hammer.gui.api.common.widget.provider.TextureProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

import java.util.function.Supplier;

public class ImageWidget extends Widget implements TextureProvider {
	public static final Size STANDARD_SIZE = new Size(18.0F, 18.0F);
	
	public static final Texture STANDARD_TEXTURE = new PartitionedTexture(HC.id("textures/widget/slot.png"), 18.0F, 18.0F, 0.055F, 0.055F, 0.055F, 0.055F);
	
	protected Supplier<Texture> texture = () -> STANDARD_TEXTURE;
	
	@Override
	public Size getStandardSize() {
		return STANDARD_SIZE;
	}
	
	@Override
	public void draw(DrawContext context, float tickDelta) {
		var matrices = context.getMatrices();
		var provider = context.getVertexConsumers();
		
		texture.get().draw(matrices, provider, getX(), getY(), getWidth(), getHeight());
	}
	
	@Override
	public Supplier<Texture> getTexture() {
		return texture;
	}
	
	@Override
	public void setTexture(Supplier<Texture> texture) {
		this.texture = texture;
	}
}
