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

package dev.vini2003.hammer.permission.api.common.util

import dev.vini2003.hammer.permission.HP
import net.minecraft.network.MessageType
import net.minecraft.scoreboard.Team
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import java.util.*

object LuckPermsUtils {
	@JvmStatic
	fun formatWithLuckPerms(player: ServerPlayerEntity, old: Text, type: MessageType, sender: UUID): Text {
		if (type != MessageType.CHAT) return old
		
		val senderPlayer = player.server.playerManager.getPlayer(sender)!!
		
		old as TranslatableText
		
		try {
			val user = HP.LUCK_PERMS.userManager.getUser(sender)!!
			
			val group = HP.LUCK_PERMS.groupManager.getGroup(user.primaryGroup)!!
			
			val groupPrefix = group.cachedData.metaData.prefix
			val groupSuffix = group.cachedData.metaData.suffix
			
			val prefix = user.cachedData.metaData.prefix
			val suffix = user.cachedData.metaData.suffix
			
			var result = ""
			
			val color = ((senderPlayer.scoreboardTeam) as Team?)?.color
			
			if (prefix != null) {
				result += "$prefix "
				
				if (color != null) {
					result += "$color${senderPlayer.name.asString()}"
				} else {
					result += senderPlayer.name.asString()
				}
				if (suffix == null && groupSuffix == null) result += "&7"
			} else if (groupPrefix != null) {
				result += "$groupPrefix "
				
				if (color != null) {
					result += "$color${senderPlayer.name.asString()}"
				} else {
					result += senderPlayer.name.asString()
				}
				if (suffix == null && groupSuffix == null) result += "&7"
			} else {
				if (color != null) {
					result += "$color${senderPlayer.name.asString()}"
				} else {
					result += senderPlayer.name.asString()
				}
				
				result += "&7"
			}
			
			if (suffix != null) {
				result += " $suffix"
				result += " » "
				result += old.args[1] as String
			} else if (groupSuffix != null) {
				result += " $groupSuffix"
				result += " » "
				result += old.args[1] as String
			} else {
				result += " » "
				result += old.args[1] as String
			}
			
			return AdventureUtils.formatWithColorValuesToText(result)
		} catch (ignored: Exception) {
		}
		
		return old
	}
}