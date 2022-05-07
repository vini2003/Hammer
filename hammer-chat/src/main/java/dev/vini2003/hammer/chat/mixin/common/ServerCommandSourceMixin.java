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

package dev.vini2003.hammer.chat.mixin.common;

import dev.vini2003.hammer.chat.registry.common.HCUuids;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Objects;
import java.util.UUID;

@Mixin(ServerCommandSource.class)
public abstract class ServerCommandSourceMixin {
	@Shadow
	@Final
	private CommandOutput output;
	
	@ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/command/CommandOutput;sendSystemMessage(Lnet/minecraft/text/Text;Ljava/util/UUID;)V"), method = "sendFeedback")
	private UUID hammer$chat$sendFeedback$sendSystemMessage(UUID uuid) {
		if (output instanceof ServerPlayerEntity && Objects.equals(uuid, Util.NIL_UUID)) {
			return HCUuids.COMMAND_FEEDBACK_UUID;
		} else {
			return uuid;
		}
	}
	
	@ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;sendSystemMessage(Lnet/minecraft/text/Text;Ljava/util/UUID;)V"), method = "sendToOps")
	private UUID hammer$chat$sendToOps$sendSystemMessage(UUID uuid) {
		return HCUuids.COMMAND_FEEDBACK_UUID;
	}
	
	@ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/command/CommandOutput;sendSystemMessage(Lnet/minecraft/text/Text;Ljava/util/UUID;)V"), method = "sendError")
	private UUID hammer$chat$sendError$sendSystemMessage(UUID uuid) {
		if (output instanceof ServerPlayerEntity && Objects.equals(uuid, Util.NIL_UUID)) {
			return HCUuids.COMMAND_FEEDBACK_UUID;
		} else {
			return uuid;
		}
	}
}
