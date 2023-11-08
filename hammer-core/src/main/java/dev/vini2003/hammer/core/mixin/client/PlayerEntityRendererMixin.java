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
		
		
		model.head.visible = PlayerUtil.hasHead(player);
		model.hat.visible = PlayerUtil.hasHead(player) && InstanceUtil.getClient().options.isPlayerModelPartEnabled(PlayerModelPart.HAT);
		model.ear.visible = PlayerUtil.hasHead(player);
		
		model.cloak.visible = PlayerUtil.hasTorso(player) && InstanceUtil.getClient().options.isPlayerModelPartEnabled(PlayerModelPart.CAPE);
		model.jacket.visible = PlayerUtil.hasTorso(player) && InstanceUtil.getClient().options.isPlayerModelPartEnabled(PlayerModelPart.JACKET);
		model.body.visible = PlayerUtil.hasTorso(player);
		
		model.rightArm.visible = PlayerUtil.hasRightArm(player);
		model.rightSleeve.visible = PlayerUtil.hasRightArm(player) && InstanceUtil.getClient().options.isPlayerModelPartEnabled(PlayerModelPart.RIGHT_SLEEVE);
		
		model.leftArm.visible = PlayerUtil.hasLeftArm(player);
		model.leftSleeve.visible = PlayerUtil.hasLeftArm(player) && InstanceUtil.getClient().options.isPlayerModelPartEnabled(PlayerModelPart.LEFT_SLEEVE);
		
		model.rightLeg.visible = PlayerUtil.hasRightLeg(player);
		model.rightPants.visible = PlayerUtil.hasRightLeg(player) && InstanceUtil.getClient().options.isPlayerModelPartEnabled(PlayerModelPart.RIGHT_PANTS_LEG);
		
		model.leftLeg.visible = PlayerUtil.hasLeftLeg(player);
		model.leftPants.visible = PlayerUtil.hasLeftLeg(player) && InstanceUtil.getClient().options.isPlayerModelPartEnabled(PlayerModelPart.LEFT_PANTS_LEG);
	}
}
