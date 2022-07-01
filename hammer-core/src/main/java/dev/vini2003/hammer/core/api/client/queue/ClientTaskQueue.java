package dev.vini2003.hammer.core.api.client.queue;

import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

public class ClientTaskQueue {
	private static final ThreadLocal<Collection<Task>> TASKS = ThreadLocal.withInitial(() -> new ArrayList());
	
	public static void enqueue(Consumer<MinecraftClient> action, long delay) {
		var actions = getTasks();
		
		actions.add(new Task(action, delay));
	}
	
	public static Collection<Task> getTasks() {
		return TASKS.get();
	}
	
	public static class Task {
		private Consumer<MinecraftClient> action;
		
		private long delay;
		
		private long remaining;
		
		public Task(Consumer<MinecraftClient> action, long delay) {
			this.action = action;
			this.delay = delay;
			
			this.remaining = delay;
		}
		
		public Consumer<MinecraftClient> getAction() {
			return action;
		}
		
		public long getRemaining() {
			remaining -= 1;
			
			return remaining;
		}
	}
}
