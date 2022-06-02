package dev.vini2003.hammer.permission;

import dev.vini2003.hammer.permission.registry.common.HPCommands;
import dev.vini2003.hammer.permission.registry.common.HPEvents;
import dev.vini2003.hammer.permission.registry.common.HPNetworking;
import net.fabricmc.api.ModInitializer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class HP implements ModInitializer {
	public static LuckPerms getLuckPerms() {
		return LuckPermsProvider.get();
	}
	
	@Override
	public void onInitialize() {
		HPEvents.init();
		HPNetworking.init();
		HPCommands.init();
	}
}
