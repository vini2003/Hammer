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

package dev.vini2003.hammer.core.registry.common;

import dev.vini2003.hammer.core.api.common.component.base.PlayerComponent;
import dev.vini2003.hammer.core.api.common.queue.ServerTaskQueue;
import dev.vini2003.hammer.core.impl.common.component.holder.ComponentHolder;
import dev.vini2003.hammer.core.impl.common.state.ComponentPersistentState;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;

import java.util.ArrayList;

public class HCEvents {
	private static final String HAMMER$COMPONENTS_KEY = "Hammer$Components";
	
	public static void init() {
		ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
			PlayerUtil.setFrozen(newPlayer, PlayerUtil.isFrozen(oldPlayer));
		});
		
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			var tasks = ServerTaskQueue.getTasks();
			var tasksToRemove = new ArrayList<ServerTaskQueue.Task>();
			
			for (var task : tasks) {
				if (task.getRemaining() <= 0) {
					tasksToRemove.add(task);
					
					task.getAction().accept(server);
				}
			}
			
			tasks.removeAll(tasksToRemove);
		});
		
		ServerWorldEvents.LOAD.register((server, world) -> {
			world.getPersistentStateManager().getOrCreate(nbt -> new ComponentPersistentState(world, nbt), () -> new ComponentPersistentState(world), HAMMER$COMPONENTS_KEY);
		});
		
		ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
			if (oldPlayer instanceof ComponentHolder oldHolder) {
				if (newPlayer instanceof ComponentHolder newHolder) {
					for (var entry : oldHolder.getComponentContainer().entrySet()) {
						var key = entry.getKey();
						var component = entry.getValue();
						
						if (component instanceof PlayerComponent playerComp) {
							if (alive || (!alive && playerComp.shouldCopyOnDeath())) {
								newHolder.getComponentContainer().put(key, component);
							}
						} else {
							if (alive) {
								newHolder.getComponentContainer().put(key, component);
							}
						}
					}
				}
			}
		});
	}
}
