package dev.vini2003.hammer.chat.registry.common;

import dev.vini2003.hammer.chat.api.common.util.ChatUtil;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.Option;

public class HCOptions {
	public static final Option SHOW_CHAT = CyclingOption.create("options.show_chat", gameOptions -> {
		var client = InstanceUtil.getClient();
		
		if (client == null || client.player == null) {
			return false;
		}
		
		return ChatUtil.shouldShowChat(client.player);
	}, ((gameOptions, option, showChat) -> {
		var client = InstanceUtil.getClient();
		
		if (client == null || client.player == null) {
			return;
		}
		
		ChatUtil.setShowChat(client.player, showChat);
	}));
	
	public static final Option SHOW_COMMAND_FEEDBACK = CyclingOption.create("options.show_command_feedback", gameOptions -> {
		var client = InstanceUtil.getClient();
		
		if (client == null || client.player == null) {
			return false;
		}
		
		return ChatUtil.shouldShowCommandFeedback(client.player);
	}, ((gameOptions, option, showChat) -> {
		var client = InstanceUtil.getClient();
		
		if (client == null || client.player == null) {
			return;
		}
		
		ChatUtil.setShowCommandFeedback(client.player, showChat);
	}));
	
	public static final Option SHOW_WARNINGS = CyclingOption.create("options.show_warnings", gameOptions -> {
		var client = InstanceUtil.getClient();
		
		if (client == null || client.player == null) {
			return false;
		}
		
		return ChatUtil.shouldShowWarnings(client.player);
	}, ((gameOptions, option, showChat) -> {
		var client = InstanceUtil.getClient();
		
		if (client == null || client.player == null) {
			return;
		}
		
		ChatUtil.setShowWarnings(client.player, showChat);
	}));
	
	public static final Option SHOW_DIRECT_MESSAGES = CyclingOption.create("options.show_direct_messages", gameOptions -> {
		var client = InstanceUtil.getClient();
		
		if (client == null || client.player == null) {
			return false;
		}
		
		return ChatUtil.shouldShowDirectMessages(client.player);
	}, ((gameOptions, option, showChat) -> {
		var client = InstanceUtil.getClient();
		
		if (client == null || client.player == null) {
			return;
		}
		
		ChatUtil.setShowDirectMessages(client.player, showChat);
	}));
	
	public static final Option FAST_CHAT_FADE = CyclingOption.create("options.fast_chat_fade", gameOptions -> {
		var client = InstanceUtil.getClient();
		
		if (client == null || client.player == null) {
			return false;
		}
		
		return ChatUtil.hasFastChatFade(client.player);
	}, ((gameOptions, option, fastChatFade) -> {
		var client = InstanceUtil.getClient();
		
		if (client == null || client.player == null) {
			return;
		}
		
		ChatUtil.setFastChatFade(client.player, fastChatFade);
	}));
}
