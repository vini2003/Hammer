package dev.vini2003.hammer.core.api.client.texture;

import dev.vini2003.hammer.core.api.client.color.Color;
import dev.vini2003.hammer.core.api.client.texture.base.BaseTexture;
import dev.vini2003.hammer.core.api.client.util.DrawingUtil;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;

public class FluidTexture implements BaseTexture {
	private final FluidVariant variant;
	
	private final Sprite sprite;
	private final Color color;
	
	public FluidTexture(FluidVariant variant) {
		this.variant = variant;
		
		this.sprite = FluidVariantRendering.getSprite(variant);
		this.color = new Color(FluidVariantRendering.getColor(variant));
	}
	
	public FluidTexture(FluidVariant variant, Sprite variantSprite, Color variantColor) {
		this.variant = variant;
		
		this.sprite = variantSprite;
		this.color = variantColor;
	}
	
	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider, float x, float y, float width, float height) {
		if (sprite == null) {
			return;
		}
		
		var client = InstanceUtil.getClient();
		
		var scaledX = x - (x % (client.getWindow().getScaledWidth() / (float) client.getWindow().getWidth()));
		var scaledY = y - (y % (client.getWindow().getScaledHeight() / (float) client.getWindow().getHeight()));
		
		DrawingUtil.drawTexturedQuad(
				matrices,
				provider,
				sprite.getId(),
				x, y, 0.0F,
				width, height,
				sprite.getMinU(), sprite.getMinV(),
				sprite.getMaxU(), sprite.getMaxV(),
				0.0F, 0.0F, 0.0F,
				DrawingUtil.DEFAULT_OVERLAY,
				DrawingUtil.DEFAULT_LIGHT,
				color,
				RenderLayer.getSolid()
		);
	}
}
