package com.github.vini2003.blade.client.utilities

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
		@JvmStatic
		fun get(texture: Identifier): RenderLayer {
			return of(
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

		@JvmStatic
		private val Interface: RenderLayer = of(
				"blade_flat",
				VertexFormats.POSITION_COLOR_LIGHT, VertexFormat.DrawMode.QUADS, 256, false, false,
				MultiPhaseParameters.builder()
						.texture(NO_TEXTURE)
						.cull(ENABLE_CULLING)
						.lightmap(ENABLE_LIGHTMAP)
						.depthTest(ALWAYS_DEPTH_TEST)
						.transparency(TRANSLUCENT_TRANSPARENCY)
						.layering(VIEW_OFFSET_Z_LAYERING)
						.build(false)
		)

		@JvmStatic
		private val Tooltip: RenderLayer = of(
				"blade_tooltip",
				VertexFormats.POSITION_COLOR_LIGHT, VertexFormat.DrawMode.QUADS, 256, false, false,
				MultiPhaseParameters.builder()
						.shader(Shader(GameRenderer::getPositionColorLightmapShader))
						.texture(NO_TEXTURE)
						.cull(ENABLE_CULLING)
						.lightmap(ENABLE_LIGHTMAP)
						.depthTest(ALWAYS_DEPTH_TEST)
						.transparency(NO_TRANSPARENCY)
						.layering(VIEW_OFFSET_Z_LAYERING)
						.build(false)
		)
	}
}