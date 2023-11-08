package dev.vini2003.hammer.core.mixin.common;

import dev.vini2003.hammer.core.impl.common.accessor.ServerPlayerEntityAccessor;
import net.minecraft.network.packet.s2c.play.ClearTitleS2CPacket;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements ServerPlayerEntityAccessor {
	@Shadow
	public ServerPlayNetworkHandler networkHandler;
	
	@Override
	public void hammer$clearTitle() {
		var packet = new ClearTitleS2CPacket(false);
		networkHandler.sendPacket(packet);
	}
	
	@Override
	public void hammer$resetTitle() {
		var packet = new ClearTitleS2CPacket(true);
		networkHandler.sendPacket(packet);
	}
	
	@Override
	public void hammer$setTitle(Text title) {
		var packet = new TitleS2CPacket(title);
		networkHandler.sendPacket(packet);
	}
	
	@Override
	public void hammer$setSubtitle(Text subtitle) {
		var packet = new SubtitleS2CPacket(subtitle);
		networkHandler.sendPacket(packet);
	}
	
	@Override
	public void hammer$setOverlay(Text overlay) {
		var packet = new OverlayMessageS2CPacket(overlay);
		networkHandler.sendPacket(packet);
	}
}
