package dev.vini2003.hammer.preset.registry.common;

import dev.vini2003.hammer.chat.api.common.util.ChatUtil;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.text.TranslatableText;

public class HPEvents {
	public static void init() {
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			var player = handler.getPlayer();
			
			ChatUtil.setShowChat(player, false);
			ChatUtil.setShowGlobalChat(player, false);
			ChatUtil.setShowWarnings(player, false);
			ChatUtil.setShowCommandFeedback(player, false);
			ChatUtil.setFastChatFade(player, true);
			
			for (var i = 0; i < 5; ++i) {
				player.sendMessage(new TranslatableText("text.hammer.welcome_" + i), false);
			}
		});
	}
}
