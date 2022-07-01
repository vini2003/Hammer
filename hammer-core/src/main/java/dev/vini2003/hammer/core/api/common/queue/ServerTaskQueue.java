package dev.vini2003.hammer.core.api.common.queue;

import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

public class ServerTaskQueue {
	private static final ThreadLocal<Collection<Task>> TASKS = ThreadLocal.withInitial(() -> new ArrayList());
	
	public static void enqueue(Consumer<MinecraftServer> action, long delay) {
		var actions = getTasks();
		
		actions.add(new Task(action, delay));
	}
	
	public static Collection<Task> getTasks() {
		return TASKS.get();
	}
	
	public static class Task {
		private Consumer<MinecraftServer> action;
		
		private long delay;
		
		private long remaining;
		
		public Task(Consumer<MinecraftServer> action, long delay) {
			this.action = action;
			this.delay = delay;
			
			this.remaining = delay;
		}
		
		public Consumer<MinecraftServer> getAction() {
			return action;
		}
		
		public long getRemaining() {
			remaining -= 1;
			
			return remaining;
		}
	}
}
