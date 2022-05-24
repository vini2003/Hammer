package dev.vini2003.hammer.chat.api.common.util;

import dev.vini2003.hammer.chat.registry.common.HCNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public class ChatUtil {
	public static void toggleChat(ServerPlayerEntity player, Boolean state) {
		var buf = PacketByteBufs.create();
		buf.writeBoolean(state);
		
		ServerPlayNetworking.send(player, HCNetworking.TOGGLE_CHAT, buf);
	}
	
	public static void toggleGlobalChat(ServerPlayerEntity player, Boolean state) {
		var buf = PacketByteBufs.create();
		buf.writeBoolean(state);
		
		ServerPlayNetworking.send(player, HCNetworking.TOGGLE_GLOBAL_CHAT, buf);
	}
	
	public static void toggleFeedback(ServerPlayerEntity player, Boolean state) {
		var buf = PacketByteBufs.create();
		buf.writeBoolean(state);
		
		ServerPlayNetworking.send(player, HCNetworking.TOGGLE_FEEDBACK, buf);
	}
	
	public static void toggleWarnings(ServerPlayerEntity player, Boolean state) {
		var buf = PacketByteBufs.create();
		buf.writeBoolean(state);
		
		ServerPlayNetworking.send(player, HCNetworking.TOGGLE_WARNINGS, buf);
	}
}
