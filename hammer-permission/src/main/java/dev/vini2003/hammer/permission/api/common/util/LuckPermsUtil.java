package dev.vini2003.hammer.permission.api.common.util;

import dev.vini2003.hammer.adventure.api.common.util.AdventureUtil;
import dev.vini2003.hammer.permission.HP;
import net.minecraft.network.MessageType;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.UUID;

public class LuckPermsUtil {
	public static Text formatWithLuckPerms(ServerPlayerEntity player, Text oldText, MessageType type, UUID sender) {
		if (type != MessageType.CHAT) {
			return oldText;
		}
		
		var old = (TranslatableText) oldText;
		
		var senderPlayer = player.getServer().getPlayerManager().getPlayer(sender);
		
		try {
			var user = HP.getLuckPerms().getUserManager().getUser(sender);
			
			var group = HP.getLuckPerms().getGroupManager().getGroup(user.getPrimaryGroup());
			
			var groupPrefix = group.getCachedData().getMetaData().getPrefix();
			var groupSuffix = group.getCachedData().getMetaData().getSuffix();
			
			var prefix = user.getCachedData().getMetaData().getPrefix();
			var suffix = user.getCachedData().getMetaData().getSuffix();
			
			var result = "";
			
			var color = ((Team) senderPlayer.getScoreboardTeam()).getColor();
			
			if (prefix != null) {
				result += "$prefix ";
				
				if (color != null) {
					result += color + "" + senderPlayer.getName().asString() + "";
				} else {
					result += senderPlayer.getName().asString();
				}
				if (suffix == null && groupSuffix == null) {
					result += "&7";
				}
			} else if (groupPrefix != null) {
				result += "$groupPrefix ";
				
				if (color != null) {
					result += color + "" + senderPlayer.getName().asString() + "";
				} else {
					result += senderPlayer.getName().asString();
				}
				if (suffix == null && groupSuffix == null) {
					result += "&7";
				}
			} else {
				if (color != null) {
					result += color + "" + senderPlayer.getName().asString() + "";
				} else {
					result += senderPlayer.getName().asString();
				}
				
				result += "&7";
			}
			
			if (suffix != null) {
				result += " " + suffix;
				result += " » ";
				result += old.getArgs()[1];
			} else if (groupSuffix != null) {
				result += " " + groupSuffix;
				result += " » ";
				result += old.getArgs()[1];
			} else {
				result += " » ";
				result += old.getArgs()[1];
			}
			
			// TODO: Improve this!
			return AdventureUtil.toText(AdventureUtil.toComponent(result));
		} catch (Exception ig) {
		}
		
		return old;
	}
}
