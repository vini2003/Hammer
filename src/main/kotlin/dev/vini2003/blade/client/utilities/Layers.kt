package dev.vini2003.blade.client.utilities

import net.minecraft.client.render.*
import net.minecraft.util.Identifier

class Layers(
	name: String,
	vertexFormat: VertexFormat,
	drawMode: VertexFormat.DrawMode,
	expectedBufferSize: Int,
	hasCrumbling: Boolean,
	translucent: Boolean,
	startAction: Runnable,
	endAction: Runnable
) : RenderLayer(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction) {
	companion object {
		private val Layers = mutableMapOf<Identifier, RenderLayer>()
		
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
}