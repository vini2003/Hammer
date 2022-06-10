package dev.vini2003.hammer.core.registry.client;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.core.api.common.util.PlayerUtil;
import dev.vini2003.hammer.core.registry.common.HCValues;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;

public class HCEvents {
	public static void init() {
		HCValues.HUD_ICONS += 1;
		
		HudRenderCallback.EVENT.register((matrices, tickDelta) -> {
			var client = InstanceUtil.getClient();
			
			if (PlayerUtil.isFrozen(client.player)) {
				var window = client.getWindow();
				
				var scaledWidth = window.getScaledWidth();
				var scaledHeight = window.getScaledHeight();
				
				var size = 16;
				var yOffset = 0;
				
				if (client.currentScreen instanceof ChatScreen) {
					yOffset -= 14;
				}
				
				var x1 = HCValues.HUD_ICON_X;
				var y1 = scaledHeight - 5 - size + yOffset;
				
				RenderSystem.setShaderTexture(0, HCTextures.FROZEN);
				
				// TODO: Make sending a message while muted bump this size!
				DrawableHelper.drawTexture(matrices, HCValues.HUD_ICON_X, scaledHeight - 5 - size + yOffset, size, size, 0.0F, 0.0F, 16, 16, 16, 16);
				
				if (!client.mouse.isCursorLocked() && client.currentScreen instanceof ChatScreen) {
					var screen = (ChatScreen) client.currentScreen;
					
					var mouseX = client.mouse.getX() * scaledWidth / window.getWidth();
					var mouseY = client.mouse.getY() * scaledHeight / window.getHeight();
					
					if (mouseX >= x1 && mouseY >= y1 && mouseX < x1 + size && mouseY < y1 + size) {
						var tooltips = new ArrayList<Text>();
						
						tooltips.add(new TranslatableText("text.hammer.frozen"));
						
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
			} else {
				HCValues.HUD_ICONS_DRAWN += 1;
				
				if (HCValues.HUD_ICONS_DRAWN >= HCValues.HUD_ICONS) {
					HCValues.HUD_ICON_X = 5;
					
					HCValues.HUD_ICONS_DRAWN = 0;
				}
			}
		});
	}
}
