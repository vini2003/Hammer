/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 vini2003
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.vini2003.hammer.zone.registry.common;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.vini2003.hammer.core.api.common.queue.ServerTaskQueue;
import dev.vini2003.hammer.zone.api.common.manager.ZoneManager;
import dev.vini2003.hammer.zone.api.common.resource.ZoneReloadListener;
import net.minecraft.resource.ResourceType;

public class HZEvents {
	public static void init() {
		// Load zones upon server starting.
		LifecycleEvent.SERVER_STARTED.register((server) -> {
			new ZoneReloadListener().reload(server.getResourceManager());
		});
		
		// Update zones upon joining.
		PlayerEvent.PLAYER_JOIN.register((player) -> {
			// The player isn't loaded yet at this stage.
			// However, they will be loaded in the next tick.
			ServerTaskQueue.enqueue(($) -> {
				ZoneManager.sync(player.getWorld());
			}, 1L);
		});
		
		// Update zones upon changing dimensions.
		PlayerEvent.PLAYER_CLONE.register((oldPlayer, newPlayer, alive) -> {
			ZoneManager.sync(newPlayer.getWorld());
		});
		
		ReloadListenerRegistry.register(ResourceType.SERVER_DATA, new ZoneReloadListener());
	}
}
