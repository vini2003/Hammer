package dev.vini2003.hammer.border.mixin.client;

import dev.vini2003.hammer.border.common.border.CubicWorldBorder;
import dev.vini2003.hammer.border.common.packet.border.CubicWorldBorderCenterChangedS2CPacket;
import dev.vini2003.hammer.border.common.packet.border.CubicWorldBorderInitializeS2CPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.packet.s2c.play.WorldBorderCenterChangedS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldBorderInitializeS2CPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
	@Shadow
	private ClientWorld world;
	@Shadow
	@Final
	private MinecraftClient client;
	
	@Inject(at = @At("HEAD"), method = "onWorldBorderCenterChanged", cancellable = true)
	private void hammer$onWorldBorderCenterChanged(WorldBorderCenterChangedS2CPacket packet, CallbackInfo ci) {
		NetworkThreadUtils.forceMainThread(packet, ((ClientPlayNetworkHandler) (Object) this), client);
		
		var worldBorder = world.getWorldBorder();
		var cubicWorldBorder = (CubicWorldBorder) worldBorder;
		
		var cubicPacket = (CubicWorldBorderCenterChangedS2CPacket) packet;
		
		cubicWorldBorder.setCenter(packet.getCenterX(), cubicPacket.getCenterY(), packet.getCenterZ());
		
		ci.cancel();
	}
	
	@Inject(at = @At("HEAD"), method = "onWorldBorderInitialize")
	private void hammer$onWorldBorderInitialize(WorldBorderInitializeS2CPacket packet, CallbackInfo ci) {
		NetworkThreadUtils.forceMainThread(packet, ((ClientPlayNetworkHandler) (Object) this), this.client);
		
		var worldBorder = this.world.getWorldBorder();
		var cubicWorldBorder = (CubicWorldBorder) worldBorder;
		
		var cubicPacket = (CubicWorldBorderInitializeS2CPacket) packet;
		
		cubicWorldBorder.setCenter(packet.getCenterX(), cubicPacket.getCenterY(), packet.getCenterZ());
		
		var lerpTime = packet.getSizeLerpTime();
		
		if (lerpTime > 0L) {
			worldBorder.interpolateSize(packet.getSize(), packet.getSizeLerpTarget(), lerpTime);
		} else {
			worldBorder.setSize(packet.getSizeLerpTarget());
		}
		
		worldBorder.setMaxRadius(packet.getMaxRadius());
		worldBorder.setWarningBlocks(packet.getWarningBlocks());
		worldBorder.setWarningTime(packet.getWarningTime());
	}
}
