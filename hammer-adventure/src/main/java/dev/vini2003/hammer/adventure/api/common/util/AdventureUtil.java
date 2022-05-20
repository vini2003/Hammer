package dev.vini2003.hammer.adventure.api.common.util;

import net.kyori.adventure.platform.fabric.FabricServerAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;

public class AdventureUtil {
	public static FabricServerAudiences createAudience(MinecraftServer server) {
		return FabricServerAudiences.of(server);
	}
	
	public static Component toComponent(Text text) {
		return GsonComponentSerializer.gson().deserializeFromTree(Text.Serializer.toJsonTree(text));
	}
	
	public static Text toText(Component component) {
		return Text.Serializer.fromJson(GsonComponentSerializer.gson().serializeToTree(component));
	}
	
	public static Component toComponent(String string) {
		return LegacyComponentSerializer.legacyAmpersand().deserialize(string).toBuilder().build();
	}
	
	public static Text toText(String string) {
		return toText(toComponent(string));
	}
}
