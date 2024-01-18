package dev.vini2003.hammer.preset.mixin.client;

import dev.vini2003.hammer.core.HC;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityRenderDispatcher.class)
public class BlockEntityRenderDispatcherMixin {
	private static boolean hammer$cancelRender = false;
	
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/entity/BlockEntityRenderer;render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V"), method = "render(Lnet/minecraft/client/render/block/entity/BlockEntityRenderer;Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V")
	private static <T extends BlockEntity> void hammer$render$render$0(BlockEntityRenderer instance, T blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider provider, int light, int overlay) {
		try {
			instance.render(blockEntity, tickDelta, matrices, provider, light, overlay);
		} catch (Exception e) {
			HC.LOGGER.error("A block entity failed to render!");
			e.printStackTrace();
			
			blockEntity.getWorld().getPlayers().forEach(player -> {
				player.sendMessage(Text.translatable("warning.hammer.block_entity_failed_to_render").formatted(Formatting.RED, Formatting.BOLD), false);
				player.sendMessage(Text.literal(Registries.BLOCK_ENTITY_TYPE.getId(blockEntity.getType()) + " at " + blockEntity.getPos().toString()).formatted(Formatting.RED), false);
			});
			
			blockEntity.markRemoved();
			
			matrices.pop();
			hammer$cancelRender = true;
		}
	}
	
	@Inject(at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/render/block/entity/BlockEntityRenderer;render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V"), method = "render(Lnet/minecraft/client/render/block/entity/BlockEntityRenderer;Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V", cancellable = true)
	private static  <T extends BlockEntity> void hammer$render$render$1(BlockEntityRenderer<T> renderer, T blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
		if (hammer$cancelRender) {
			hammer$cancelRender = false;
			
			while (!matrices.isEmpty()) {
				matrices.pop();
			}
			
			ci.cancel();
		}
	}
}
