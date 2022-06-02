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

import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class LayerUtil {
	private static final Map<Identifier, RenderLayer> LAYERS = new HashMap<>();
	
	private static final RenderLayer INTERFACE = RenderLayer.of(
			"interface",
			VertexFormats.POSITION_COLOR,
			VertexFormat.DrawMode.QUADS,
			256,
			false,
			true,
			RenderLayer.MultiPhaseParameters.builder()
											.shader(new RenderPhase.Shader(GameRenderer::getPositionColorShader))
											.transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
											.build(true)
	);
	
	public static RenderLayer getInterface() {
		return INTERFACE;
	}
	
	public static RenderLayer get(Identifier texture) {
		LAYERS.computeIfAbsent(texture, ($) ->
				RenderLayer.of(
						texture.toUnderscoreSeparatedString(),
						VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
						VertexFormat.DrawMode.QUADS,
						256,
						true,
						true,
						RenderLayer.MultiPhaseParameters.builder()
														.shader(new RenderPhase.Shader(GameRenderer::getPositionColorTexLightmapShader))
														.texture(new RenderPhase.Texture(texture, false, false))
														.transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
														.overlay(RenderPhase.DISABLE_OVERLAY_COLOR)
														.build(true)
				));
		
		return LAYERS.get(texture);
	}
}
