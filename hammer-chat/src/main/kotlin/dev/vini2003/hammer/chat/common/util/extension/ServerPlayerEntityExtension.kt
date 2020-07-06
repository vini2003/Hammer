package dev.vini2003.hammer.chat.common.util.extension

import dev.vini2003.hammer.chat.common.util.ChatUtils
import net.minecraft.server.network.ServerPlayerEntity

fun ServerPlayerEntity.toggleChat(state: Boolean) = ChatUtils.toggleChat(this, state)

fun ServerPlayerEntity.toggleGlobalChat(state: Boolean) = ChatUtils.toggleGlobalChat(this, state)

fun ServerPlayerEntity.toggleFeedback(state: Boolean) = ChatUtils.toggleFeedback(this, state)

fun ServerPlayerEntity.toggleWarnings(state: Boolean) = ChatUtils.toggleWarnings(this, state)