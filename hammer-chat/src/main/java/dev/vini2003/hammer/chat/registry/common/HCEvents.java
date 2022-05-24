package dev.vini2003.hammer.chat.registry.common;

import com.mojang.brigadier.Command;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.TranslatableText;

public class HCEvents {
	public static void init() {
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			dispatcher.register(
					CommandManager.literal("toggle_global_chat").executes(context -> {
						HCValues.SHOW_GLOBAL_CHAT = !HCValues.SHOW_GLOBAL_CHAT;
						
						context.getSource().sendFeedback(new TranslatableText("command.hammer.toggle_global_chat", HCValues.SHOW_GLOBAL_CHAT ? "enabled" : "disabled"), true);
						
						var buf = PacketByteBufs.create();
						buf.writeBoolean(HCValues.SHOW_GLOBAL_CHAT);
						
						for (var player : context.getSource().getServer().getPlayerManager().getPlayerList()) {
							ServerPlayNetworking.send(player, HCNetworking.TOGGLE_GLOBAL_CHAT, PacketByteBufs.duplicate(buf));
						}
						
						return Command.SINGLE_SUCCESS;
					})
			);
		});
		
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			var buf = PacketByteBufs.create();
			buf.writeBoolean(HCValues.SHOW_GLOBAL_CHAT);
			
			ServerPlayNetworking.send(handler.getPlayer(), HCNetworking.TOGGLE_GLOBAL_CHAT, buf);
		});
	}
}
