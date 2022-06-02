package dev.vini2003.hammer.chat.registry.client;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.vini2003.hammer.chat.api.common.util.ChatUtil;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;

public class HCEvents {
	public static void init() {
		HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
			var client = InstanceUtil.getClient();
			
			if (ChatUtil.shouldShowWarnings(client.player)) {
				var window = client.getWindow();
				var scaledWidth = window.getScaledWidth();
				var scaledHeight = window.getScaledHeight();
				
				if (ChatUtil.shouldShowCommandFeedback(client.player) || !ChatUtil.shouldShowChat(client.player) || !ChatUtil.shouldShowGlobalChat(client.player)) {
					var size = 16;
					var yOffset = 0;
					
					if (client.currentScreen instanceof ChatScreen) {
						yOffset -= 14;
					}
					
					var x1 = 5;
					var y1 = scaledHeight - 5 - size + yOffset;
					
					RenderSystem.setShaderTexture(0, HCTextures.WARNING);
					
					DrawableHelper.drawTexture(matrixStack, 5, scaledHeight - 5 - size + yOffset, size, size, 0.0F, 0.0F, 16, 16, 16, 16);
					
					if (!client.mouse.isCursorLocked() && client.currentScreen instanceof ChatScreen) {
						var screen = (ChatScreen) client.currentScreen;
						
						var mouseX = client.mouse.getX() * scaledWidth / window.getWidth();
						var mouseY = client.mouse.getY() * scaledHeight / window.getHeight();
						
						if (mouseX >= x1 && mouseY >= y1 && mouseX < x1 + size && mouseY < y1 + size) {
							var tooltips = new ArrayList<Text>();
							
							if (ChatUtil.shouldShowCommandFeedback(client.player)) {
								tooltips.add(new TranslatableText("text.hammer.feedback.warning"));
								tooltips.add(new TranslatableText("text.hammer.feedback.toggle"));
							}
							
							if (!ChatUtil.shouldShowChat(client.player)) {
								if (!tooltips.isEmpty()) {
									tooltips.add(LiteralText.EMPTY);
								}
								
								tooltips.add(new TranslatableText("text.hammer.chat.warning"));
								tooltips.add(new TranslatableText("text.hammer.chat.toggle"));
							}
							
							if (!ChatUtil.shouldShowGlobalChat(client.player)) {
								if (tooltips.isEmpty()) {
									tooltips.add(LiteralText.EMPTY);
								}
								
								tooltips.add(new TranslatableText("text.hammer.global_chat.warning"));
								tooltips.add(new TranslatableText("text.hammer.global_chat.toggle"));
							}
							
							
							screen.renderTooltip(matrixStack, tooltips, (int) mouseX, (int) mouseY);
						}
					}
				}
			}
		});
	}
}
