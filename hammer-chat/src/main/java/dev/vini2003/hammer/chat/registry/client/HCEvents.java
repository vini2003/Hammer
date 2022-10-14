/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 vini2003
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.vini2003.hammer.chat.registry.client;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.vini2003.hammer.chat.api.common.util.ChatUtil;
import dev.vini2003.hammer.chat.registry.common.HCNetworking;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.core.api.common.util.PlayerUtil;
import dev.vini2003.hammer.core.registry.common.HCValues;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.util.InputUtil;

import net.minecraft.text.Text;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

public class HCEvents {
	private static long LAST_TAB_MS = System.currentTimeMillis();
	
	public static void init() {
		HCValues.HUD_ICONS += 2;
		
		HudRenderCallback.EVENT.register((matrices, tickDelta) -> {
			var client = InstanceUtil.getClient();
			
			var isShiftPressed = ChatScreen.hasShiftDown();
			
			var isPageUpPressed = isShiftPressed && InputUtil.isKeyPressed(client.getWindow().getHandle(), GLFW.GLFW_KEY_PAGE_UP);
			var isPageDownPressed = isShiftPressed && InputUtil.isKeyPressed(client.getWindow().getHandle(), GLFW.GLFW_KEY_PAGE_DOWN);
			
			if (isPageUpPressed || isPageDownPressed) {
				var currentTabMs = System.currentTimeMillis();
				
				if (currentTabMs - LAST_TAB_MS > 400) {
					var buf = PacketByteBufs.create();
					
					buf.writeBoolean(isPageUpPressed);
					
					ClientPlayNetworking.send(HCNetworking.SWITCH_SELECTED_CHANNEL, buf);
					
					LAST_TAB_MS = currentTabMs;
				}
			}
			
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
					
					var x1 = HCValues.HUD_ICON_X;
					var y1 = scaledHeight - 5 - size + yOffset;
					
					RenderSystem.setShaderTexture(0, HCTextures.WARNING);
					
					DrawableHelper.drawTexture(matrices, HCValues.HUD_ICON_X, scaledHeight - 5 - size + yOffset, size, size, 0.0F, 0.0F, 16, 16, 16, 16);
					
					if (!client.mouse.isCursorLocked() && client.currentScreen instanceof ChatScreen) {
						var screen = (ChatScreen) client.currentScreen;
						
						var mouseX = client.mouse.getX() * scaledWidth / window.getWidth();
						var mouseY = client.mouse.getY() * scaledHeight / window.getHeight();
						
						if (mouseX >= x1 && mouseY >= y1 && mouseX < x1 + size && mouseY < y1 + size) {
							var tooltips = new ArrayList<Text>();
							
							if (ChatUtil.shouldShowCommandFeedback(client.player)) {
								tooltips.add(Text.translatable("text.hammer.command_feedback.warning"));
								tooltips.add(Text.translatable("text.hammer.command_feedback.toggle"));
							}
							
							if (!ChatUtil.shouldShowChat(client.player)) {
								if (!tooltips.isEmpty()) {
									tooltips.add(Text.empty());
								}
								
								tooltips.add(Text.translatable("text.hammer.chat.warning"));
								tooltips.add(Text.translatable("text.hammer.chat.toggle"));
							}
							
							if (!ChatUtil.shouldShowGlobalChat(client.player)) {
								if (!tooltips.isEmpty()) {
									tooltips.add(Text.empty());
								}
								
								tooltips.add(Text.translatable("text.hammer.global_chat.warning"));
								tooltips.add(Text.translatable("text.hammer.global_chat.toggle"));
							}
							
							
							screen.renderTooltip(matrices, tooltips, (int) mouseX, (int) mouseY);
						}
					}
					
					HCValues.HUD_ICONS_DRAWN += 1;
					
					if (HCValues.HUD_ICONS_DRAWN >= HCValues.HUD_ICONS) {
						HCValues.HUD_ICON_X = 5;
						
						HCValues.HUD_ICONS_DRAWN = 0;
					} else {
						HCValues.HUD_ICON_X += 16 + 5;
					}
				}
				
				if (ChatUtil.isMuted(client.player)) {
					var size = 16;
					var yOffset = 0;
					
					if (client.currentScreen instanceof ChatScreen) {
						yOffset -= 14;
					}
					
					var x1 = HCValues.HUD_ICON_X;
					var y1 = scaledHeight - 5 - size + yOffset;
					
					RenderSystem.setShaderTexture(0, HCTextures.MUTED);
					
					// TODO: Make sending a message while muted bump this size!
					DrawableHelper.drawTexture(matrices, HCValues.HUD_ICON_X, scaledHeight - 5 - size + yOffset, size, size, 0.0F, 0.0F, 16, 16, 16, 16);
					
					HCValues.HUD_ICON_X += 16 + 5;
					
					if (!client.mouse.isCursorLocked() && client.currentScreen instanceof ChatScreen) {
						var screen = (ChatScreen) client.currentScreen;
						
						var mouseX = client.mouse.getX() * scaledWidth / window.getWidth();
						var mouseY = client.mouse.getY() * scaledHeight / window.getHeight();
						
						if (mouseX >= x1 && mouseY >= y1 && mouseX < x1 + size && mouseY < y1 + size) {
							var tooltips = new ArrayList<Text>();
							
							tooltips.add(Text.translatable("text.hammer.muted"));
							
							screen.renderTooltip(matrices, tooltips, (int) mouseX, (int) mouseY);
						}
					}
					
					HCValues.HUD_ICONS_DRAWN += 1;
					
					if (HCValues.HUD_ICONS_DRAWN >= HCValues.HUD_ICONS) {
						HCValues.HUD_ICON_X = 5;
						
						HCValues.HUD_ICONS_DRAWN = 0;
					} else {
						HCValues.HUD_ICON_X += 16 + 5;
					}
				}
				
				if (PlayerUtil.isFrozen(client.player)) {
					var size = 16;
					var yOffset = 0;
					
					if (client.currentScreen instanceof ChatScreen) {
						yOffset -= 14;
					}
					
					var x1 = HCValues.HUD_ICON_X;
					var y1 = scaledHeight - 5 - size + yOffset;
					
					RenderSystem.setShaderTexture(0, dev.vini2003.hammer.core.registry.client.HCTextures.FROZEN);
					
					// TODO: Make sending a message while muted bump this size!
					DrawableHelper.drawTexture(matrices, HCValues.HUD_ICON_X, scaledHeight - 5 - size + yOffset, size, size, 0.0F, 0.0F, 16, 16, 16, 16);
					
					if (!client.mouse.isCursorLocked() && client.currentScreen instanceof ChatScreen) {
						var screen = (ChatScreen) client.currentScreen;
						
						var mouseX = client.mouse.getX() * scaledWidth / window.getWidth();
						var mouseY = client.mouse.getY() * scaledHeight / window.getHeight();
						
						if (mouseX >= x1 && mouseY >= y1 && mouseX < x1 + size && mouseY < y1 + size) {
							var tooltips = new ArrayList<Text>();
							
							tooltips.add(Text.translatable("text.hammer.frozen"));
							
							screen.renderTooltip(matrices, tooltips, (int) mouseX, (int) mouseY);
						}
					}
					
					HCValues.HUD_ICONS_DRAWN += 1;
					
					if (HCValues.HUD_ICONS_DRAWN >= HCValues.HUD_ICONS) {
						HCValues.HUD_ICON_X = 5;
						
						HCValues.HUD_ICONS_DRAWN = 0;
					} else {
						HCValues.HUD_ICON_X += 16 + 5;
					}
				}
			}
		});
	}
}
