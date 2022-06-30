package dev.vini2003.hammer.zone.registry.client;

import net.minecraft.client.render.*;

public class HZRenderLayers {
	public static RenderLayer getZone() {
		return RenderLayer.of(
				"Hammer$Zone",
				VertexFormats.POSITION_COLOR,
				VertexFormat.DrawMode.QUADS,
				512,
				false,
				false,
				RenderLayer.MultiPhaseParameters.builder()
						.cull(RenderPhase.ENABLE_CULLING)
						.shader(new RenderPhase.Shader(GameRenderer::getPositionColorShader))
						.depthTest(RenderPhase.LEQUAL_DEPTH_TEST)
						.transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
						.lineWidth(RenderPhase.FULL_LINE_WIDTH)
						.build(false)
		);
	}
	
	public static RenderLayer getZoneOutline() {
		return RenderLayer.of(
			"Hammer$ZoneOutline",
				VertexFormats.POSITION_COLOR,
				VertexFormat.DrawMode.LINES,
				512,
				false,
				false,
				RenderLayer.MultiPhaseParameters.builder()
						.cull(RenderPhase.DISABLE_CULLING)
						.shader(RenderPhase.LINES_SHADER)
						.lineWidth(RenderPhase.FULL_LINE_WIDTH)
						.build(false)
		);
	}
}
