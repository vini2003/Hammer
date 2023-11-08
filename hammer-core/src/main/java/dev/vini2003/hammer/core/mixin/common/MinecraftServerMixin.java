package dev.vini2003.hammer.core.mixin.common;

import dev.vini2003.hammer.core.impl.common.accessor.MinecraftServerAccessor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements MinecraftServerAccessor {
	@Shadow
	public abstract PlayerManager getPlayerManager();
	
	@Override
	public Collection<ServerPlayerEntity> hammer$getPlayers() {
		return getPlayerManager().getPlayerList();
	}
}
