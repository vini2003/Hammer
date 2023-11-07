package dev.vini2003.hammer.preset.mixin.client;

import dev.vini2003.hammer.core.HC;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.HashSet;
import java.util.Set;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;tick()V"), method = "tickEntity")
	private void hammer$tickEntity$tick(Entity instance) {
		try {
			instance.tick();
		} catch (Exception e) {
			HC.LOGGER.error("An entity failed to tick!");
			e.printStackTrace();
			
			instance.getWorld().getPlayers().forEach(player -> {
				player.sendMessage(Text.literal("An entity failed to tick!").formatted(Formatting.RED, Formatting.BOLD), false);
				player.sendMessage(Text.literal(Registries.ENTITY_TYPE.getId(instance.getType()) + " at " + instance.getBlockPos().toString() + " with UUID " + instance.getUuidAsString()).formatted(Formatting.RED), false);
			});
			
			instance.remove(Entity.RemovalReason.DISCARDED);
		}
	}
}
