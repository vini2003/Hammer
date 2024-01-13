package dev.vini2003.hammer.core.impl.common.accessor;

import dev.vini2003.hammer.core.api.common.exception.NoMixinException;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Collection;

public interface MinecraftServerAccessor {
	default Collection<ServerPlayerEntity> hammer$getPlayers() {
		throw new NoMixinException();
	}
}
