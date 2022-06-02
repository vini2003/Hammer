package dev.vini2003.hammer.adventure.api.common.util;

import dev.vini2003.hammer.adventure.HA;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.fabric.FabricServerAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

import java.util.List;

public class AdventureUtil {
	public static FabricServerAudiences createAudience(MinecraftServer server) {
		return FabricServerAudiences.of(server);
	}
	
	public static Audience createAudience(CommandOutput source) {
		return HA.getAudiences().audience(source);
	}
	
	public static Audience createAudience(ServerCommandSource source) {
		return HA.getAudiences().audience(source);
	}
	
	public static Audience createAudience(List<ServerPlayerEntity> players) {
		return HA.getAudiences().audience(players);
	}
	
	public static Audience createAudience(ServerWorld world) {
		return HA.getAudiences().audience(world.getPlayers());
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
