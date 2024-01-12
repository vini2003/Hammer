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

package dev.vini2003.hammer.zone.registry.client;

import net.minecraft.client.render.*;

public class HZRenderLayers {
	private static final RenderLayer ZONE = RenderLayer.of(
			"Hammer$Zone",
			VertexFormats.POSITION_COLOR,
			VertexFormat.DrawMode.QUADS,
			512,
			false,
			false,
			RenderLayer.MultiPhaseParameters.builder()
											.cull(RenderPhase.ENABLE_CULLING)
											.program(new RenderPhase.ShaderProgram(GameRenderer::getPositionColorProgram))
											.depthTest(RenderPhase.LEQUAL_DEPTH_TEST)
											.transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
											.lineWidth(RenderPhase.FULL_LINE_WIDTH)
											.build(false)
	);
	
	private static final RenderLayer ZONE_OUTLINE = RenderLayer.of(
			"Hammer$ZoneOutline",
			VertexFormats.POSITION_COLOR,
			VertexFormat.DrawMode.LINES,
			512,
			false,
			false,
			RenderLayer.MultiPhaseParameters.builder()
											.cull(RenderPhase.DISABLE_CULLING)
											.program(RenderPhase.LINES_PROGRAM)
											.lineWidth(RenderPhase.FULL_LINE_WIDTH)
											.build(false)
	);
	
	public static RenderLayer getZone() {
		return ZONE;
	}
	
	public static RenderLayer getZoneOutline() {
		return ZONE_OUTLINE;
	}
}
