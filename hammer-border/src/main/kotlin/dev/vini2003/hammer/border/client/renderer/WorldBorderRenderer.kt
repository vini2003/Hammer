package dev.vini2003.hammer.border.client.renderer

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import dev.vini2003.hammer.border.common.border.CubicWorldBorder
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.*
import net.minecraft.util.Identifier
import net.minecraft.util.Util
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World

object WorldBorderRenderer {
	@JvmStatic
	fun render(client: MinecraftClient, world: World, camera: Camera, texture: Identifier?) {
		val bufferBuilder = Tessellator.getInstance().buffer
		val worldBorder = world.worldBorder
		val cubicBorder = worldBorder as CubicWorldBorder
		val viewDistance = client.options.getViewDistance() * 16.0
		if (camera.pos.x >= worldBorder.boundEast - viewDistance
			|| camera.pos.x <= worldBorder.boundWest + viewDistance
			|| camera.pos.y <= cubicBorder.boundUp - viewDistance
			|| camera.pos.y >= cubicBorder.boundDown + viewDistance
			|| camera.pos.z >= worldBorder.boundSouth - viewDistance
			|| camera.pos.z <= worldBorder.boundNorth + viewDistance
		) {
			var scaledDistanceToBorder = (1.0 - cubicBorder.getDistanceInsideBorder(camera.pos.x, camera.pos.y, camera.pos.z) / viewDistance).coerceIn(0.0, 1.0)
			
			scaledDistanceToBorder = Math.pow(scaledDistanceToBorder, 4.0)
			scaledDistanceToBorder = MathHelper.clamp(scaledDistanceToBorder, 0.0, 1.0)
			
			val cameraX = camera.pos.x
			val cameraY = camera.pos.y
			val cameraZ = camera.pos.z
			
			RenderSystem.enableBlend()
			RenderSystem.enableDepthTest()
			RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO)
			RenderSystem.setShaderTexture(0, texture)
			RenderSystem.depthMask(MinecraftClient.isFabulousGraphicsOrBetter())
			
			val matrices = RenderSystem.getModelViewStack()
			matrices.push()
			
			RenderSystem.applyModelViewMatrix()
			
			val color = worldBorder.stage.color
			val red = (color shr 16 and 0xFF).toFloat() / 255.0f
			val green = (color shr 8 and 0xFF).toFloat() / 255.0f
			val blue = (color and 0xFF).toFloat() / 255.0f
			
			RenderSystem.setShaderColor(red, green, blue, scaledDistanceToBorder.toFloat())
			RenderSystem.setShader { GameRenderer.getPositionTexShader() }
			RenderSystem.polygonOffset(-3.0f, -3.0f)
			RenderSystem.enablePolygonOffset()
			RenderSystem.disableCull()
			
			val fadeUv = (Util.getMeasuringTimeMs() % 3000L).toFloat() / 3000.0f
			
			bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE)
			
			val size = worldBorder.size.toFloat() / 2.0f
			
			// Draw South border.
			bufferBuilder.vertex(worldBorder.boundEast - cameraX, cubicBorder.boundUp - cameraY, worldBorder.boundSouth - cameraZ).texture(fadeUv - 0.0f, fadeUv + 0.0f).next()
			bufferBuilder.vertex(worldBorder.boundWest - cameraX, cubicBorder.boundUp - cameraY, worldBorder.boundSouth - cameraZ).texture(fadeUv - size, fadeUv + 0.0f).next()
			bufferBuilder.vertex(worldBorder.boundWest - cameraX, cubicBorder.boundDown - cameraY, worldBorder.boundSouth - cameraZ).texture(fadeUv - size, fadeUv + size).next()
			bufferBuilder.vertex(worldBorder.boundEast - cameraX, cubicBorder.boundDown - cameraY, worldBorder.boundSouth - cameraZ).texture(fadeUv - 0.0f, fadeUv + size).next()
			
			// Draw North border.
			bufferBuilder.vertex(worldBorder.boundEast - cameraX, cubicBorder.boundUp - cameraY, worldBorder.boundNorth - cameraZ).texture(fadeUv - 0.0f, fadeUv + 0.0f).next()
			bufferBuilder.vertex(worldBorder.boundWest - cameraX, cubicBorder.boundUp - cameraY, worldBorder.boundNorth - cameraZ).texture(fadeUv - size, fadeUv + 0.0f).next()
			bufferBuilder.vertex(worldBorder.boundWest - cameraX, cubicBorder.boundDown - cameraY, worldBorder.boundNorth - cameraZ).texture(fadeUv - size, fadeUv + size).next()
			bufferBuilder.vertex(worldBorder.boundEast - cameraX, cubicBorder.boundDown - cameraY, worldBorder.boundNorth - cameraZ).texture(fadeUv - 0.0f, fadeUv + size).next()
			
			// Draw East border.
			bufferBuilder.vertex(worldBorder.boundEast - cameraX, cubicBorder.boundUp - cameraY, worldBorder.boundNorth - cameraZ).texture(fadeUv - 0.0f, fadeUv + 0.0f).next()
			bufferBuilder.vertex(worldBorder.boundEast - cameraX, cubicBorder.boundUp - cameraY, worldBorder.boundSouth - cameraZ).texture(fadeUv - size, fadeUv + 0.0f).next()
			bufferBuilder.vertex(worldBorder.boundEast - cameraX, cubicBorder.boundDown - cameraY, worldBorder.boundSouth - cameraZ).texture(fadeUv - size, fadeUv + size).next()
			bufferBuilder.vertex(worldBorder.boundEast - cameraX, cubicBorder.boundDown - cameraY, worldBorder.boundNorth - cameraZ).texture(fadeUv - 0.0f, fadeUv + size).next()
			
			// Draw West border.
			bufferBuilder.vertex(worldBorder.boundWest - cameraX, cubicBorder.boundUp - cameraY, worldBorder.boundNorth - cameraZ).texture(fadeUv - 0.0f, fadeUv + 0.0f).next()
			bufferBuilder.vertex(worldBorder.boundWest - cameraX, cubicBorder.boundUp - cameraY, worldBorder.boundSouth - cameraZ).texture(fadeUv - size, fadeUv + 0.0f).next()
			bufferBuilder.vertex(worldBorder.boundWest - cameraX, cubicBorder.boundDown - cameraY, worldBorder.boundSouth - cameraZ).texture(fadeUv - size, fadeUv + size).next()
			bufferBuilder.vertex(worldBorder.boundWest - cameraX, cubicBorder.boundDown - cameraY, worldBorder.boundNorth - cameraZ).texture(fadeUv - 0.0f, fadeUv + size).next()
			
			// Draw Up border.
			bufferBuilder.vertex(worldBorder.boundEast - cameraX, cubicBorder.boundUp - cameraY, worldBorder.boundSouth - cameraZ).texture(fadeUv - 0.0f, fadeUv + 0.0f).next()
			bufferBuilder.vertex(worldBorder.boundWest - cameraX, cubicBorder.boundUp - cameraY, worldBorder.boundSouth - cameraZ).texture(fadeUv - size, fadeUv + 0.0f).next()
			bufferBuilder.vertex(worldBorder.boundWest - cameraX, cubicBorder.boundUp - cameraY, worldBorder.boundNorth - cameraZ).texture(fadeUv - size, fadeUv + size).next()
			bufferBuilder.vertex(worldBorder.boundEast - cameraX, cubicBorder.boundUp - cameraY, worldBorder.boundNorth - cameraZ).texture(fadeUv - 0.0f, fadeUv + size).next()
			
			// Draw Down border.
			bufferBuilder.vertex(worldBorder.boundEast - cameraX, cubicBorder.boundDown - cameraY, worldBorder.boundSouth - cameraZ).texture(fadeUv - 0.0f, fadeUv + 0.0f).next()
			bufferBuilder.vertex(worldBorder.boundWest - cameraX, cubicBorder.boundDown - cameraY, worldBorder.boundSouth - cameraZ).texture(fadeUv - size, fadeUv + 0.0f).next()
			bufferBuilder.vertex(worldBorder.boundWest - cameraX, cubicBorder.boundDown - cameraY, worldBorder.boundNorth - cameraZ).texture(fadeUv - size, fadeUv + size).next()
			bufferBuilder.vertex(worldBorder.boundEast - cameraX, cubicBorder.boundDown - cameraY, worldBorder.boundNorth - cameraZ).texture(fadeUv - 0.0f, fadeUv + size).next()
			
			bufferBuilder.end()
			
			BufferRenderer.draw(bufferBuilder)
			RenderSystem.enableCull()
			RenderSystem.polygonOffset(0.0f, 0.0f)
			RenderSystem.disablePolygonOffset()
			RenderSystem.disableBlend()
			
			matrices.pop()
			
			RenderSystem.applyModelViewMatrix()
			RenderSystem.depthMask(true)
		}
	}
}