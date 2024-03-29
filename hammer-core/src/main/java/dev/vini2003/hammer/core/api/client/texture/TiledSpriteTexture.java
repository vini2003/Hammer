/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 vini2003
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.vini2003.hammer.core.api.client.texture;

import dev.vini2003.hammer.core.api.client.color.Color;
import dev.vini2003.hammer.core.api.client.texture.base.Texture;
import dev.vini2003.hammer.core.api.client.util.DrawingUtil;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;

public class TiledSpriteTexture implements Texture {
	
	private final Object spriteObject;
	
	private final float maxTilesX;
	private final float maxTilesY;
	
	private final float stepTilesX;
	private final float stepTilesY;
	
	public TiledSpriteTexture(Object sprite) {
		this.spriteObject = sprite;
		
		this.maxTilesX = Integer.MAX_VALUE;
		this.maxTilesY = Integer.MAX_VALUE;
		
		this.stepTilesX = Integer.MAX_VALUE;
		this.stepTilesY = Integer.MAX_VALUE;
	}
	
	public TiledSpriteTexture(Object sprite, float maxTilesX, float maxTilesY, float stepTilesX, float stepTilesY) {
		this.spriteObject = sprite;
		
		this.maxTilesX = maxTilesX;
		this.maxTilesY = maxTilesY;
		
		this.stepTilesX = stepTilesX;
		this.stepTilesY = stepTilesY;
	}
	
	@Override 
	@Environment(EnvType.CLIENT)
	public void draw(MatrixStack matrices, VertexConsumerProvider provider, float x, float y, float width, float height) {
		var sprite = (Sprite) spriteObject;
		
		var client = InstanceUtil.getClient();
		
		var scaledX = x - ((int) x) == 0 ? (int) x : x - (x % (client.getWindow().getScaledWidth() / (float) client.getWindow().getWidth()));
		var scaledY = y - ((int) y) == 0 ? (int) y : y - (y % (client.getWindow().getScaledHeight() / (float) client.getWindow().getHeight()));
		
		DrawingUtil.drawTiledTexturedQuad(
				matrices,
				provider,
				sprite.getAtlasId(),
				scaledX, scaledY, 0.0F,
				width, height,
				// TODO: Check if this is equivalent to the 1.19.2 code. This assume the sprite must be 16x16 for tiling;
				// TODO: but it may have a different size! How to circumvent?
				16, 16,
				Integer.MAX_VALUE, Integer.MAX_VALUE,
				0.0F, 0.0F,
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
