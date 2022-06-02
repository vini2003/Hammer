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
