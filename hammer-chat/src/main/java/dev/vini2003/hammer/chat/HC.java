package dev.vini2003.hammer.chat;

import dev.vini2003.hammer.chat.api.common.channel.Channel;
import dev.vini2003.hammer.chat.api.common.manager.ChannelManager;
import dev.vini2003.hammer.chat.registry.common.HCCommands;
import dev.vini2003.hammer.chat.registry.common.HCEvents;
import dev.vini2003.hammer.chat.registry.common.HCNetworking;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.permission.api.common.manager.RoleManager;
import dev.vini2003.hammer.permission.api.common.role.Role;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.jetbrains.annotations.ApiStatus;

import java.util.concurrent.atomic.AtomicBoolean;

@ApiStatus.Internal
public class HC implements ModInitializer {
	@Override
	public void onInitialize() {
		HCCommands.init();
		HCEvents.init();
		HCNetworking.init();
	}
}
