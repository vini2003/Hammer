package dev.vini2003.hammer.chat.registry.client;

import dev.vini2003.hammer.chat.registry.common.HCValues;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import static dev.vini2003.hammer.chat.registry.common.HCNetworking.*;

public class HCNetworking {
	public static void init() {
		ClientPlayNetworking.registerGlobalReceiver(TOGGLE_CHAT, (client, handler, buf, responseSender) -> {
			HCValues.SHOW_CHAT = buf.readBoolean();
		});
		
		ClientPlayNetworking.registerGlobalReceiver(TOGGLE_FEEDBACK, (client, handler, buf, responseSender) -> {
			HCValues.SHOW_FEEDBACK = buf.readBoolean();
		});
		
		ClientPlayNetworking.registerGlobalReceiver(TOGGLE_GLOBAL_CHAT, (client, handler, buf, responseSender) -> {
			HCValues.SHOW_GLOBAL_CHAT = buf.readBoolean();
		});
		
		ClientPlayNetworking.registerGlobalReceiver(TOGGLE_WARNINGS, (client, handler, buf, responseSender) -> {
			HCValues.SHOW_WARNINGS = buf.readBoolean();
		});
	}
}
