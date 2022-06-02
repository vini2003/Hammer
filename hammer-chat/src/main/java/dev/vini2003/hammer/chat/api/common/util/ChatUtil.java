package dev.vini2003.hammer.chat.api.common.util;

import dev.vini2003.hammer.chat.impl.common.accessor.PlayerEntityAccessor;
import net.minecraft.entity.player.PlayerEntity;

public class ChatUtil {
	public static void setShowChat(PlayerEntity player, boolean showChat) {
		((PlayerEntityAccessor) player).hammer$setShowChat(showChat);
	}
	
	public static boolean shouldShowChat(PlayerEntity player) {
		return ((PlayerEntityAccessor) player).hammer$shouldShowChat();
	}
	
	public static void setShowGlobalChat(PlayerEntity player, boolean showGlobalChat) {
		((PlayerEntityAccessor) player).hammer$setShowGlobalChat(showGlobalChat);
	}
	
	public static boolean shouldShowGlobalChat(PlayerEntity player) {
		return ((PlayerEntityAccessor) player).hammer$shouldShowGlobalChat();
	}
	
	public static void setShowCommandFeedback(PlayerEntity player, boolean showFeedback) {
		((PlayerEntityAccessor) player).hammer$setShowCommandFeedback(showFeedback);
	}
	
	public static boolean shouldShowCommandFeedback(PlayerEntity player) {
		return ((PlayerEntityAccessor) player).hammer$shouldShowCommandFeedback();
	}
	
	public static void setShowWarnings(PlayerEntity player, boolean showWarnings) {
		((PlayerEntityAccessor) player).hammer$setShowWarnings(showWarnings);
	}
	
	public static boolean shouldShowWarnings(PlayerEntity player) {
		return ((PlayerEntityAccessor) player).hammer$shouldShowWarnings();
	}
	
	public static void setMuted(PlayerEntity player, boolean muted) {
		((PlayerEntityAccessor) player).hammer$setMuted(muted);
	}
	
	public static boolean isMuted(PlayerEntity player) {
		return ((PlayerEntityAccessor) player).hammer$isMuted();
	}
}
