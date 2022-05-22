package dev.vini2003.hammer.core.api.client.texture;

import dev.vini2003.hammer.core.api.client.color.Color;
import dev.vini2003.hammer.core.api.client.texture.base.Texture;
import dev.vini2003.hammer.core.api.client.util.DrawingUtil;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;

public class SpriteTexture implements Texture {
	private Sprite sprite;
	
	public SpriteTexture(Sprite sprite) {
		this.sprite = sprite;
	}
	
	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider, float x, float y, float width, float height) {
		var client = InstanceUtil.getClient();
		
		var scaledX = x - (x % (client.getWindow().getScaledWidth() / (float) client.getWindow().getWidth()));
		var scaledY = y - (y % (client.getWindow().getScaledHeight() / (float) client.getWindow().getHeight()));
		
		DrawingUtil.drawTexturedQuad(
				matrices,
				provider,
				sprite.getId(),
				scaledX, scaledY, 0.0F,
				width, height,
				sprite.getMinU(), sprite.getMinV(),
				sprite.getMaxU(), sprite.getMaxV(),
				0.0F, 0.0F, 0.0F,
				DrawingUtil.DEFAULT_OVERLAY,
				DrawingUtil.DEFAULT_LIGHT,
				Color.WHITE,
				RenderLayer.getSolid()
		);
	}
}
