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
