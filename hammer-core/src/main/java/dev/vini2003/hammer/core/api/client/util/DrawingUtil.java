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

package dev.vini2003.hammer.core.api.client.util;

import dev.vini2003.hammer.core.api.client.color.Color;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class DrawingUtil {
	public static final int DEFAULT_OVERLAY = OverlayTexture.DEFAULT_UV;
	public static final int DEFAULT_LIGHT = LightmapTextureManager.MAX_LIGHT_COORDINATE;
	
	public static BlockRenderManager getBlockRenderManager() {
		var client = InstanceUtil.getClient();
		
		return client.getBlockRenderManager();
	}
	
	public static TextureManager getTextureManager() {
		var client = InstanceUtil.getClient();
		
		return client.getTextureManager();
	}
	
	public static BakedModelManager getBakedModelManager() {
		var client = InstanceUtil.getClient();
		
		return client.getBakedModelManager();
	}
	
	public static ItemRenderer getItemRenderer() {
		var client = InstanceUtil.getClient();
		
		return client.getItemRenderer();
	}
	
	public static TextRenderer getTextRenderer() {
		var client = InstanceUtil.getClient();
		
		return client.textRenderer;
	}
	
	public static void drawQuad(
			MatrixStack matrices,
			VertexConsumerProvider provider,
			float x, float y, float z,
			float width, float height,
			Color color,
			RenderLayer layer
	) {
		var consumer = provider.getBuffer(layer);
		
		var peek = matrices.peek();
		
		consumer.vertex(peek.getPositionMatrix(), x, y, z).color(color.getR(), color.getG(), color.getB(), color.getA()).next();
		consumer.vertex(peek.getPositionMatrix(), x, y + height, z).color(color.getR(), color.getG(), color.getB(), color.getA()).next();
		consumer.vertex(peek.getPositionMatrix(), x + width, y + height, z).color(color.getR(), color.getG(), color.getB(), color.getA()).next();
		consumer.vertex(peek.getPositionMatrix(), x + width, y, z).color(color.getR(), color.getG(), color.getB(), color.getA()).next();
	}
	
	public static void drawTexturedQuad(
			MatrixStack matrices,
			VertexConsumerProvider provider,
			Identifier textureId,
			float x, float y, float z,
			float width, float height,
			float uStart, float vStart,
			float uEnd, float vEnd,
			float normalX, float normalY, float normalZ,
			int overlay,
			int light,
			Color color,
			RenderLayer layer
	) {
		var consumer = provider.getBuffer(layer);
		
		var peek = matrices.peek();
		
		consumer.vertex(peek.getPositionMatrix(), x, y + height, z).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(uStart, vEnd).overlay(overlay).light(light).normal(peek.getNormalMatrix(), normalX, normalY, normalZ).next();
		consumer.vertex(peek.getPositionMatrix(), x + width, y + height, z).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(uEnd, vEnd).overlay(overlay).light(light).normal(peek.getNormalMatrix(), normalX, normalY, normalZ).next();
		consumer.vertex(peek.getPositionMatrix(), x + width, y, z).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(uEnd, vStart).overlay(overlay).light(light).normal(peek.getNormalMatrix(), normalX, normalY, normalZ).next();
		consumer.vertex(peek.getPositionMatrix(), x, y, z).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(uStart, vStart).overlay(overlay).light(light).normal(peek.getNormalMatrix(), normalX, normalY, normalZ).next();
	}
	
	public static void drawTiledTexturedQuad(
			MatrixStack matrices,
			VertexConsumerProvider provider,
			Identifier textureId,
			float x, float y, float z,
			float width, float height,
			float tileWidth, float tileHeight,
			float maxTilesX, float maxTilesY,
			float stepTilesX, float stepTilesY,
			float uStart, float vStart,
			float uEnd, float vEnd,
			float normalX, float normalY, float normalZ,
			int overlay,
			int light,
			Color color,
			RenderLayer layer
	) {
		var consumer = provider.getBuffer(layer);
		
		var peek = matrices.peek();
		
		var endX = x + width;
		var endY = y + height;
		
		var currentX = x;
		var currentY = y;
		
		var tilesX = 0;
		var tilesY = 0;
		
		while (currentY < endY && tilesY < maxTilesY) {
			currentX = x;
			
			while (currentX < endX && tilesX < maxTilesX) {
				var diffX = Math.min(endX - currentX, tileWidth);
				var diffY = Math.min(endY - currentY, tileHeight);
				
				var deltaX = diffX < tileWidth ? (uEnd - uStart) * (1.0F - (diffX / tileWidth)) : 0.0F;
				var deltaY = diffY < tileHeight ? (vEnd - vStart) * (1.0F - (diffY / tileHeight)) : 0.0F;
				
				
				consumer.vertex(peek.getPositionMatrix(), currentX, currentY + diffY, z).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(uStart, vEnd - deltaY).overlay(overlay).light(light).normal(peek.getNormalMatrix(), normalX, normalY, normalZ).next();
				consumer.vertex(peek.getPositionMatrix(), currentX + diffX, currentY + diffY, z).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(uEnd - deltaX, vEnd - deltaY).overlay(overlay).light(light).normal(peek.getNormalMatrix(), normalX, normalY, normalZ).next();
				consumer.vertex(peek.getPositionMatrix(), currentX + diffX, currentY, z).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(uEnd - deltaX, vStart).overlay(overlay).light(light).normal(peek.getNormalMatrix(), normalX, normalY, normalZ).next();
				consumer.vertex(peek.getPositionMatrix(), currentX, currentY, z).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(uStart, vStart).overlay(overlay).light(light).normal(peek.getNormalMatrix(), normalX, normalY, normalZ).next();
				
				currentX += Math.min(Math.abs(endX - currentX - (tilesX * stepTilesX)), tileWidth);
				
				if (currentX < (endX + stepTilesX)) {
					currentX += stepTilesX;
				}
				
				tilesX += 1;
			}
			
			currentY += Math.min(Math.abs(endY - currentY - (tilesY * stepTilesY)), tileHeight);
			
			if (currentY < (endY + stepTilesY)) {
				currentY += stepTilesY;
			}
			
			tilesX = 0;
			
			tilesY += 1;
		}
	}
}
