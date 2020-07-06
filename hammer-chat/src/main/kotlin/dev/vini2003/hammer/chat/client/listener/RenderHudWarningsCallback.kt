package dev.vini2003.hammer.chat.client.listener

import com.mojang.blaze3d.systems.RenderSystem
import dev.vini2003.hammer.chat.registry.client.HCTextures
import dev.vini2003.hammer.chat.registry.common.HCValues
import dev.vini2003.hammer.client.util.InstanceUtils
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.gui.screen.ChatScreen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText

object RenderHudWarningsCallback : HudRenderCallback {
	override fun onHudRender(matrixStack: MatrixStack?, tickDelta: Float) {
			if (HCValues.SHOW_WARNINGS) {
				val client = InstanceUtils.CLIENT
				
				val window = client.window
				val scaledWidth = window.scaledWidth
				val scaledHeight = window.scaledHeight
				
				if (HCValues.SHOW_FEEDBACK || !HCValues.SHOW_CHAT || !HCValues.SHOW_GLOBAL_CHAT) {
					val size = 16
					var yOffset = 0
					
					if (client.currentScreen is ChatScreen) {
						yOffset -= 14;
					}
					
					val x1 = 5;
					val y1 = scaledHeight - 5 - size + yOffset;
					
					RenderSystem.setShaderTexture(0, HCTextures.WARNING);
					
					DrawableHelper.drawTexture(matrixStack, 5, scaledHeight - 5 - size + yOffset, size, size, 0.0F, 0.0F, 16, 16, 16, 16);
					
					if (!client.mouse.isCursorLocked && client.currentScreen is ChatScreen) {
						val screen = client.currentScreen as ChatScreen
						
						val mouseX = client.mouse.x * scaledWidth / window.width;
						val mouseY = client.mouse.y * scaledHeight / window.height;
						
						if (mouseX >= x1 && mouseY >= y1 && mouseX < x1 + size && mouseY < y1 + size) {
							val tooltips = mutableListOf<Text>()
							
							if (HCValues.SHOW_FEEDBACK) {
								tooltips.add(TranslatableText("text.hammer.feedback.warning"))
								tooltips.add(TranslatableText("text.hammer.feedback.toggle"))
							}
							
							if (!HCValues.SHOW_CHAT) {
								if (tooltips.isNotEmpty()) {
									tooltips.add(LiteralText.EMPTY);
								}
								
								tooltips.add(TranslatableText("text.hammer.chat.warning"))
								tooltips.add(TranslatableText("text.hammer.chat.toggle"))
							}
							
							if (!HCValues.SHOW_GLOBAL_CHAT) {
								if (tooltips.isNotEmpty()) {
									tooltips.add(LiteralText.EMPTY);
								}
								
								tooltips.add(TranslatableText("text.hammer.global_chat.warning"))
								tooltips.add(TranslatableText("text.hammer.global_chat.toggle"))
							}
							
							
							screen.renderTooltip(matrixStack, tooltips, mouseX.toInt(), mouseY.toInt())
						}
					}
				}
			}
	}
}