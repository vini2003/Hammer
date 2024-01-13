package dev.vini2003.hammer.core.mixin.client;

import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
	@Inject(at = @At("HEAD"), method = "isCurrentlyBreaking", cancellable = true)
	private void hammer$isCurrentlyBreaking(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		var client = InstanceUtil.getClient();
		var player = client.player;
		
		if (!player.hammer$hasRightArm()) {
			cir.cancel();
		}
		
		if (!player.hammer$allowInteraction()) {
			cir.setReturnValue(false);
		}
	}
	
	@Inject(at = @At("HEAD"), method = "interactItem", cancellable = true)
	private void hammer$interactItem(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
		if (!player.hammer$hasLeftArm() && hand == Hand.OFF_HAND) {
			cir.setReturnValue(ActionResult.FAIL);
		}
		
		if (!player.hammer$hasRightArm() && hand == Hand.MAIN_HAND) {
			cir.setReturnValue(ActionResult.FAIL);
		}
		
		if (!player.hammer$allowInteraction()) {
			cir.setReturnValue(ActionResult.FAIL);
		}
	}
	
	@Inject(at = @At("HEAD"), method = "interactBlock", cancellable = true)
	private void hammer$interactBlock(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
		if (!player.hammer$hasLeftArm() && hand == Hand.OFF_HAND) {
			cir.setReturnValue(ActionResult.FAIL);
		}
		
		if (!player.hammer$hasRightArm() && hand == Hand.MAIN_HAND) {
			cir.setReturnValue(ActionResult.FAIL);
		}
		
		if (!player.hammer$allowInteraction()) {
			cir.setReturnValue(ActionResult.FAIL);
		}
	}
	
	@Inject(at = @At("HEAD"), method = "interactEntity", cancellable = true)
	private void hammer$interactEntity(PlayerEntity player, Entity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
		if (!player.hammer$hasLeftArm() && !player.hammer$hasRightArm()) {
			cir.setReturnValue(ActionResult.FAIL);
		}
		
		if (!player.hammer$allowInteraction()) {
			cir.setReturnValue(ActionResult.FAIL);
		}
	}
}
