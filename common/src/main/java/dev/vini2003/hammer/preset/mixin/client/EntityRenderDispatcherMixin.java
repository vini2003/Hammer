package dev.vini2003.hammer.preset.mixin.client;

import dev.vini2003.hammer.core.HC;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
	private boolean hammer$cancelRender = false;
	
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderer;render(Lnet/minecraft/entity/Entity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"), method = "render")
	private <T extends Entity>  void incorproated$render$render$0(EntityRenderer instance, T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		try {
			instance.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
		} catch (Exception e) {
			HC.LOGGER.error("An entity failed to render!");
			e.printStackTrace();
			
			entity.getWorld().getPlayers().forEach(player -> {
				player.sendMessage(Text.translatable("warning.hammer.entity_failed_to_render").formatted(Formatting.RED, Formatting.BOLD), false);
				player.sendMessage(Text.literal(Registry.ENTITY_TYPE.getId(entity.getType()) + " at " + entity.getBlockPos().toString() + " with UUID " + entity.getUuidAsString()).formatted(Formatting.RED), false);
			});
			
			entity.remove(Entity.RemovalReason.DISCARDED);
			
			matrices.pop();
			hammer$cancelRender = true;
		}
	}
	
	@Inject(at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/render/entity/EntityRenderer;render(Lnet/minecraft/entity/Entity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"), method = "render", cancellable = true)
	private <E extends Entity> void hammer$render$render$1(E entity, double x, double y, double z, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
		if (hammer$cancelRender) {
			hammer$cancelRender = false;
			
			while (!matrices.isEmpty()) {
				matrices.pop();
			}
			
			ci.cancel();
		}
	}
}
