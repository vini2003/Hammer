package dev.vini2003.hammer.core.registry.client;

import dev.vini2003.hammer.core.api.client.queue.ClientTaskQueue;
import dev.vini2003.hammer.core.api.common.queue.ServerTaskQueue;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import java.util.ArrayList;

public class HCEvents {
	public static void init() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			var tasks = ClientTaskQueue.getTasks();
			var tasksToRemove = new ArrayList<ClientTaskQueue.Task>();
			
			for (var task : tasks) {
				if (task.getRemaining() <= 0) {
					tasksToRemove.add(task);
					
					task.getAction().accept(client);
				}
			}
			
			tasks.removeAll(tasksToRemove);
		});
	}
}
