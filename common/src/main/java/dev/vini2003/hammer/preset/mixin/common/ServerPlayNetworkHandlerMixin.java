package dev.vini2003.hammer.preset.mixin.common;

import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.preset.HP;
import net.minecraft.util.registry.Registry;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
	@Shadow
	public abstract void disconnect(Text reason);
	
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;broadcast(Lnet/minecraft/text/Text;Z)V"), method = "onDisconnected")
	private void hammer$onDisconnected$broadcast(PlayerManager instance, Text message, boolean overlay) {
		if (!HP.CONFIG.disableLeaveMessages) {
			instance.broadcast(message, overlay);
		}
	}
	
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;playerTick()V"), method = "tick")
	private void hammer$tickEntity$tick(ServerPlayerEntity playerEntity) {
		try {
			playerEntity.playerTick();
		} catch (Exception e) {
			HC.LOGGER.error("A player failed to tick!");
			e.printStackTrace();
			
			playerEntity.getWorld().getPlayers().forEach(player -> {
				player.sendMessage(Text.translatable("warning.hammer.player_failed_to_tick").formatted(Formatting.RED, Formatting.BOLD), false);
				player.sendMessage(player.getDisplayName().copy().append(Text.literal(Registry.ENTITY_TYPE.getId(playerEntity.getType()) + " at " + playerEntity.getBlockPos().toString() + " with UUID " + playerEntity.getUuidAsString()).formatted(Formatting.RED)), false);
			});

			disconnect(Text.translatable("warning.hammer.own_player_failed_to_tick"));
		}
	}
}
