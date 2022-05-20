package dev.vini2003.hammer.core.api.client.texture.base;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

public interface BaseTexture {
	void draw(MatrixStack matrices, VertexConsumerProvider provider, float x, float y, float width, float height);
}

