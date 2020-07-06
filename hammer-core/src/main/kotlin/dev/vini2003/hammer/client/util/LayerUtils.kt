package dev.vini2003.hammer.client.util

import net.minecraft.client.render.*
import net.minecraft.util.Identifier

object LayerUtils : RenderLayer(null, null, null, 0, false, false, null, null) {
	private val LAYERS = mutableMapOf<Identifier, RenderLayer>()
	
	@JvmStatic
	fun get(texture: Identifier): RenderLayer {
		if (!LAYERS.containsKey(texture)) {
			LAYERS[texture] = of(
				texture.toUnderscoreSeparatedString()!!,
				VertexFormats.POSITION_COLOR_TEXTURE_LIGHT, VertexFormat.DrawMode.QUADS, 256, true, true,
				MultiPhaseParameters.builder()
					.shader(Shader(GameRenderer::getPositionColorTexLightmapShader))
					.texture(Texture(texture, false, false))
					.transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
					.overlay(RenderPhase.DISABLE_OVERLAY_COLOR)
					.build(true)
			)
		}
		
		return LAYERS[texture]!!
	}
}