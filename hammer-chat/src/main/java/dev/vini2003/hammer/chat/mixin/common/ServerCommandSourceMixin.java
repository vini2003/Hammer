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
