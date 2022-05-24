package dev.vini2003.hammer.chat.registry.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.brigadier.Command;
import dev.vini2003.hammer.chat.registry.common.HCValues;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;

public class HCEvents {
	public static void init() {
		ClientCommandManager.DISPATCHER.register(
				ClientCommandManager.literal("toggle_chat").executes(context -> {
					HCValues.SHOW_CHAT = !HCValues.SHOW_CHAT;
					
					context.getSource().sendFeedback(new TranslatableText("command.hammer.toggle_chat", HCValues.SHOW_CHAT ? "enabled" : "disabled"));
					
					return Command.SINGLE_SUCCESS;
				})
		);
		
		ClientCommandManager.DISPATCHER.register(
				ClientCommandManager.literal("toggle_feedback").executes(context -> {
					HCValues.SHOW_FEEDBACK = !HCValues.SHOW_FEEDBACK;
					
					context.getSource().sendFeedback(new TranslatableText("command.hammer.toggle_feedback", HCValues.SHOW_FEEDBACK ? "enabled" : "disabled"));
					
					return Command.SINGLE_SUCCESS;
				})
		);
		
		ClientCommandManager.DISPATCHER.register(
				ClientCommandManager.literal("toggle_warnings").executes(context -> {
					HCValues.SHOW_WARNINGS = !HCValues.SHOW_WARNINGS;
					
					context.getSource().sendFeedback(new TranslatableText("command.hammer.toggle_warnings", HCValues.SHOW_WARNINGS ? "enabled" : "disabled"));
					
					return Command.SINGLE_SUCCESS;
				})
		);
		
		HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
			if (HCValues.SHOW_WARNINGS) {
				var client = InstanceUtil.getClient();
				
				var window = client.getWindow();
				var scaledWidth = window.getScaledWidth();
				var scaledHeight = window.getScaledHeight();
				
				if (HCValues.SHOW_FEEDBACK || !HCValues.SHOW_CHAT || !HCValues.SHOW_GLOBAL_CHAT) {
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
							
							if (HCValues.SHOW_FEEDBACK) {
								tooltips.add(new TranslatableText("text.hammer.feedback.warning"));
								tooltips.add(new TranslatableText("text.hammer.feedback.toggle"));
							}
							
							if (!HCValues.SHOW_CHAT) {
								if (!tooltips.isEmpty()) {
									tooltips.add(LiteralText.EMPTY);
								}
								
								tooltips.add(new TranslatableText("text.hammer.chat.warning"));
								tooltips.add(new TranslatableText("text.hammer.chat.toggle"));
							}
							
							if (!HCValues.SHOW_GLOBAL_CHAT) {
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
