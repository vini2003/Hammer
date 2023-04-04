package dev.vini2003.hammer.core.api.common.util;

import net.minecraft.network.packet.s2c.play.ClearTitleS2CPacket;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class TitleUtil {
	public static void clearTitle(ServerPlayerEntity player) {
		var packet = new ClearTitleS2CPacket(false);
		player.networkHandler.sendPacket(packet);
	}
	
	public static void resetTitle(ServerPlayerEntity player) {
		var packet = new ClearTitleS2CPacket(true);
		player.networkHandler.sendPacket(packet);
	}
	
	public static void setTitle(ServerPlayerEntity player, Text title) {
		var packet = new TitleS2CPacket(title);
		player.networkHandler.sendPacket(packet);
	}
	
	public static void setSubtitle(ServerPlayerEntity player, Text subtitle) {
		var packet = new SubtitleS2CPacket(subtitle);
		player.networkHandler.sendPacket(packet);
	}
	
	public static void setOverlay(ServerPlayerEntity player, Text overlay) {
		var packet = new OverlayMessageS2CPacket(overlay);
		player.networkHandler.sendPacket(packet);
	}
}
