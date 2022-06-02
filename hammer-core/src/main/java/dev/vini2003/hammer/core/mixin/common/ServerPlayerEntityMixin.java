package dev.vini2003.hammer.core.mixin.common;

import dev.vini2003.hammer.core.api.common.event.ChatEvents;
import io.netty.util.concurrent.Future;
import net.minecraft.network.MessageType;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {
	@Shadow
	public ServerPlayNetworkHandler networkHandler;
	
	@Shadow
	protected abstract boolean acceptsMessage(MessageType type);
	
	@Shadow
	@Final
	public MinecraftServer server;
	
	@Inject(at = @At("HEAD"), method = "sendMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/MessageType;Ljava/util/UUID;)V", cancellable = true)
	private void hammer$sendMessage(Text message, MessageType type, UUID sender, CallbackInfo ci) {
		if (type == MessageType.CHAT && server.getPlayerManager().getPlayer(sender) != null && acceptsMessage(type)) {
			try {
				var result = ChatEvents.SEND_MESSAGE.invoker().send((ServerPlayerEntity) (Object) this, message, type, sender);

				if (result.getResult().isAccepted()) {
					networkHandler.sendPacket(new GameMessageS2CPacket(result.getValue(), type, sender), Future::isSuccess);
				}
				
				ci.cancel();
			} catch (Exception exception) {
			}
		}
	}
}
