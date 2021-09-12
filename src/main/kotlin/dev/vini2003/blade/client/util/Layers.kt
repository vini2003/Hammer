package dev.vini2003.blade.client.util

import net.minecraft.client.render.*
import net.minecraft.util.Identifier

object Layers : RenderLayer(null, null, null, 0, false, false, null, null) {
	private val Layers = mutableMapOf<Identifier, RenderLayer>()
	
	val Interface = of(
		"interface",
		VertexFormats.POSITION_COLOR_LIGHT,
		VertexFormat.DrawMode.QUADS,
		256,
		false,
		true,
		MultiPhaseParameters.builder()
			.shader(Shader(GameRenderer::getPositionColorTexLightmapShader))
			.transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
			.overlay(RenderPhase.DISABLE_OVERLAY_COLOR)
			.build(true)
	) as RenderLayer
	
	@JvmStatic
	fun get(texture: Identifier): RenderLayer {
		if (!Layers.containsKey(texture)) {
			Layers[texture] = of(
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
		
		return Layers[texture]!!
	}
}