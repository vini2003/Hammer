package dev.vini2003.hammer.core.mixin.client;

import dev.vini2003.hammer.core.api.common.util.PlayerUtil;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
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
		model.hat.visible = PlayerUtil.hasHead(player);
		
		model.jacket.visible = PlayerUtil.hasTorso(player);
		model.body.visible = PlayerUtil.hasTorso(player);
		
		model.rightArm.visible = PlayerUtil.hasRightArm(player);
		model.rightSleeve.visible = PlayerUtil.hasRightArm(player);
		
		model.leftArm.visible = PlayerUtil.hasLeftArm(player);
		model.leftSleeve.visible = PlayerUtil.hasLeftArm(player);
		
		model.rightLeg.visible = PlayerUtil.hasRightLeg(player);
		model.rightPants.visible = PlayerUtil.hasRightLeg(player);
		
		model.leftLeg.visible = PlayerUtil.hasLeftLeg(player);
		model.leftPants.visible = PlayerUtil.hasLeftLeg(player);
	}
}
