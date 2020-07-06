package dev.vini2003.hammer.border.mixin.common;

import dev.vini2003.hammer.border.common.border.CubicWorldBorder;
import dev.vini2003.hammer.border.common.packet.border.CubicWorldBorderCenterChangedS2CPacket;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.WorldBorderCenterChangedS2CPacket;
import net.minecraft.world.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldBorderCenterChangedS2CPacket.class)
public class WorldBorderCenterChangedS2CPacketMixin implements CubicWorldBorderCenterChangedS2CPacket {
	private double centerY;
	
	@Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/world/border/WorldBorder;)V")
	private void hammer$init_0(WorldBorder border, CallbackInfo ci) {
		var extendedBorder = (CubicWorldBorder) border;
			
		centerY = extendedBorder.getCenterY();
	}
	
	@Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/network/PacketByteBuf;)V")
	private void hammer$init_1(PacketByteBuf buf, CallbackInfo ci) {
		centerY = buf.readDouble();
	}
	
	@Inject(at = @At("RETURN"), method = "write")
	private void hammer$write(PacketByteBuf buf, CallbackInfo ci) {
		buf.writeDouble(centerY);
	}
	
	@Override
	public double getCenterY() {
		return centerY;
	}
}
