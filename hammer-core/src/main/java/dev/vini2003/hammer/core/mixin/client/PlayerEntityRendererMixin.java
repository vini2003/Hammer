package dev.vini2003.hammer.core.mixin.client;

import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.PlayerModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {
	private PlayerEntityRenderer hammer$self() {
		return (PlayerEntityRenderer) (Object) this;
	}
	
	@Inject(at = @At("RETURN"), method = "setModelPose(Lnet/minecraft/client/network/AbstractClientPlayerEntity;)V")
	void hammer$setModelPose(AbstractClientPlayerEntity player, CallbackInfo ci) {
		var model = hammer$self().getModel();
		
		
		model.head.visible = player.hammer$hasHead();
		model.hat.visible = player.hammer$hasHead() && InstanceUtil.getClient().options.isPlayerModelPartEnabled(PlayerModelPart.HAT);
		model.ear.visible = player.hammer$hasHead();
		
		model.cloak.visible = player.hammer$hasTorso() && InstanceUtil.getClient().options.isPlayerModelPartEnabled(PlayerModelPart.CAPE);
		model.jacket.visible = player.hammer$hasTorso() && InstanceUtil.getClient().options.isPlayerModelPartEnabled(PlayerModelPart.JACKET);
		model.body.visible = player.hammer$hasTorso();
		
		model.rightArm.visible = player.hammer$hasRightArm();
		model.rightSleeve.visible = player.hammer$hasRightArm() && InstanceUtil.getClient().options.isPlayerModelPartEnabled(PlayerModelPart.RIGHT_SLEEVE);
		
		model.leftArm.visible = player.hammer$hasLeftArm();
		model.leftSleeve.visible = player.hammer$hasLeftArm() && InstanceUtil.getClient().options.isPlayerModelPartEnabled(PlayerModelPart.LEFT_SLEEVE);
		
		model.rightLeg.visible = player.hammer$hasRightLeg();
		model.rightPants.visible = player.hammer$hasRightLeg() && InstanceUtil.getClient().options.isPlayerModelPartEnabled(PlayerModelPart.RIGHT_PANTS_LEG);
		
		model.leftLeg.visible = player.hammer$hasLeftLeg();
		model.leftPants.visible = player.hammer$hasLeftLeg() && InstanceUtil.getClient().options.isPlayerModelPartEnabled(PlayerModelPart.LEFT_PANTS_LEG);
	}
}
