package dev.vini2003.hammer.chat.registry.common;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import dev.vini2003.hammer.chat.api.common.manager.ChannelManager;
import dev.vini2003.hammer.chat.api.common.util.ChatUtil;
import dev.vini2003.hammer.core.api.common.event.ChatEvents;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.TypedActionResult;

public class HCEvents {
	public static void init() {
		ChatEvents.SEND_MESSAGE.register((receiver, message, type, sender) -> {
			var us = receiver;
			var them = receiver.getServer().getPlayerManager().getPlayer(sender);
			
			if (them == null) {
				return TypedActionResult.pass(message);
			}
			
			var ourChannels = 0;
			var theirChannels = 0;
			
			var shared = false;
			
			for (var channel : ChannelManager.channels()) {
				var o = ourChannels;
				var t = theirChannels;
				
				if (channel.isIn(us)) {
					++ourChannels;
				}
				
				if (channel.isIn(them)) {
					++theirChannels;
				}
				
				if (ourChannels != o && theirChannels != t) {
					shared = true;
				}
			}
			
			if ((ourChannels != 0 || theirChannels != 0) && !shared) {
				return TypedActionResult.fail(message);
			}
			
			return TypedActionResult.success(message);
		});
		
		ChatEvents.SEND_MESSAGE.register((receiver, message, type, sender) -> {
			var us = receiver;
			
			if (ChatUtil.isMuted(us)) {
				us.sendMessage(new TranslatableText("text.hammer.muted").formatted(Formatting.RED), false);
				
				return TypedActionResult.fail(message);
			}
			
			var them = receiver.getServer().getPlayerManager().getPlayer(sender);
			
			if (them == null) {
				return TypedActionResult.pass(message);
			}
			
			return TypedActionResult.success(message);
		});
	}
}
