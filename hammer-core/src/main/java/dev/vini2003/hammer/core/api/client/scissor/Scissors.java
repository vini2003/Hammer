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

package dev.vini2003.hammer.core.api.client.scissor;

import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class Scissors {
	private static final Scissors[] SCISSORS = new Scissors[512];
	
	private static int LAST_SCISSOR = -1;
	
	private final float x;
	private final float y;
	
	private final float width;
	private final float height;
	
	private int index;
	
	private int left;
	private int right;
	private int top;
	private int bottom;
	
	private final VertexConsumerProvider provider;
	
	public Scissors(float x, float y, float width, float height, VertexConsumerProvider provider) {
		this.x = x;
		this.y = y;
		
		this.width = width;
		this.height = height;
		
		this.provider = provider;
		
		if (provider instanceof VertexConsumerProvider.Immediate immediate) {
			immediate.draw();
		}
		
		LAST_SCISSOR += 1;
		
		if (LAST_SCISSOR < 512) {
			index = LAST_SCISSOR;
			
			SCISSORS[index] = this;
			
			var client = InstanceUtil.getClient();
			
			if (client != null) {
				var windowHeight = (float) client.getWindow().getHeight();
				var windowScale = (float) client.getWindow().getScaleFactor();
				
				var scaledX = (int) (x * windowScale);
				var scaledY = (int) (windowHeight - (y + height) * windowScale);
				
				var scaledWidth = (int) (width * windowScale);
				var scaledHeight = (int) (height * windowScale);
				
				left = scaledX;
				right = scaledX + scaledWidth - 1;
				
				top = scaledY;
				bottom = scaledY + scaledHeight - 1;
				
				if (index > 0) {
					var parent = SCISSORS[index - 1];
					
					if (left < parent.left) {
						left = parent.left;
					}
					
					if (right > parent.right) {
						right = parent.right;
					}
					
					if (top < parent.top) {
						top = parent.top;
					}
					
					if (bottom > parent.bottom) {
						bottom = parent.bottom;
					}
				}
			}
			
			create();
		}
	}
	
	public void create() {
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		
		GL11.glScissor(left, top, (right - left + 1 < 0) ? 0 : right - left + 1, (bottom - top + 1 < 0) ? 0 : bottom - top + 1);
	}
	
	public void destroy() {
		if (provider instanceof VertexConsumerProvider.Immediate immediate) {
			immediate.draw();
		}
		
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		
		SCISSORS[index] = null;
		
		LAST_SCISSOR -= 1;
		
		if (LAST_SCISSOR > -1) {
			SCISSORS[LAST_SCISSOR].create();
		}
	}
}
