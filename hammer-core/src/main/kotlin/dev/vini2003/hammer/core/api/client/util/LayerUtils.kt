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

package dev.vini2003.hammer.core.api.client.util

import net.minecraft.client.render.*
import net.minecraft.util.Identifier

object LayerUtils {
	private val LAYERS = mutableMapOf<Identifier, RenderLayer>()
	
	private val INTERFACE = RenderLayer.of(
		"interface",
		VertexFormats.POSITION_COLOR,
		VertexFormat.DrawMode.QUADS,
		256,
		false,
		true,
		RenderLayer.MultiPhaseParameters.builder()
			.shader(RenderPhase.Shader(GameRenderer::getPositionColorShader))
			.transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
			.build(true)
	)
	
	@JvmStatic
	fun getInterface(): RenderLayer {
		return INTERFACE
	}
	
	@JvmStatic
	fun get(texture: Identifier): RenderLayer {
		if (!LAYERS.containsKey(texture)) {
			LAYERS[texture] = RenderLayer.of(
				texture.toUnderscoreSeparatedString()!!,
				VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
				VertexFormat.DrawMode.QUADS,
				256,
				true,
				true,
				RenderLayer.MultiPhaseParameters.builder()
					.shader(RenderPhase.Shader(GameRenderer::getPositionColorTexLightmapShader))
					.texture(RenderPhase.Texture(texture, false, false))
					.transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
					.overlay(RenderPhase.DISABLE_OVERLAY_COLOR)
					.build(true)
			)
		}
		
		return LAYERS[texture]!!
	}
}