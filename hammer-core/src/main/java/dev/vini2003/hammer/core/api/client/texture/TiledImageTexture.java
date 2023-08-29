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
import dev.vini2003.hammer.core.api.client.util.LayerUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class TiledImageTexture implements Texture {
	private final Identifier textureId;
	
	private final float tileWidth;
	private final float tileHeight;
	
	private final float maxTilesX;
	private final float maxTilesY;
	
	private final float stepTilesX;
	private final float stepTilesY;
	
	public TiledImageTexture(Identifier textureId, float tileWidth, float tileHeight) {
		this.textureId = textureId;
		
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		
		this.maxTilesX = Integer.MAX_VALUE;
		this.maxTilesY = Integer.MAX_VALUE;
		
		this.stepTilesX = 0.0F;
		this.stepTilesY = 0.0F;
	}
	
	public TiledImageTexture(Identifier textureId, float tileWidth, float tileHeight, float maxTilesX, float maxTilesY, float stepTilesX, float stepTilesY) {
		this.textureId = textureId;
		
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		
		this.maxTilesX = maxTilesX;
		this.maxTilesY = maxTilesY;
		
		this.stepTilesX = stepTilesX;
		this.stepTilesY = stepTilesY;
	}
	
	@Override 
	@Environment(EnvType.CLIENT)
	public void draw(MatrixStack matrices, VertexConsumerProvider provider, float x, float y, float width, float height) {
		var client = InstanceUtil.getClient();
		
		var scaledX = x - ((int) x) == 0 ? (int) x : x - (x % (client.getWindow().getScaledWidth() / (float) client.getWindow().getWidth()));
		var scaledY = y - ((int) y) == 0 ? (int) y : y - (y % (client.getWindow().getScaledHeight() / (float) client.getWindow().getHeight()));
		
		var layer = LayerUtil.get(textureId);
		
		DrawingUtil.drawTiledTexturedQuad(
				matrices,
				provider,
				textureId,
				scaledX, scaledY, 0.0F,
				width, height,
				tileWidth, tileHeight,
				Integer.MAX_VALUE, Integer.MAX_VALUE,
				0.0F, 0.0F,
				0.0F, 0.0F,
				1.0F, 1.0F,
				0.0F, 0.0F, 0.0F,
				DrawingUtil.DEFAULT_OVERLAY,
				DrawingUtil.DEFAULT_LIGHT,
				Color.WHITE,
				layer
		);
	}
}
