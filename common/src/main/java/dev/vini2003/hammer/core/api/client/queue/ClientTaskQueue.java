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

package dev.vini2003.hammer.core.api.client.queue;

import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public class ClientTaskQueue {
	private static final ThreadLocal<Collection<Task>> TASKS = ThreadLocal.withInitial(ConcurrentLinkedQueue::new);
	
	@Deprecated
	public static void enqueue(Consumer<MinecraftClient> action, long ticksRemaining) {
		execute(action, ticksRemaining);
	}
	
	public static void execute(Consumer<MinecraftClient> action, long ticksRemaining) {
		var actions = getTasks();
		
		actions.add(new Task(action, ticksRemaining));
	}
	
	public static Collection<Task> getTasks() {
		return TASKS.get();
	}
	
	public static class Task {
		private final Consumer<MinecraftClient> action;
		
		private long ticksRemaining;
		
		public Task(Consumer<MinecraftClient> action, long ticksRemaining) {
			this.action = action;
			
			this.ticksRemaining = ticksRemaining;
		}
		
		public Consumer<MinecraftClient> getAction() {
			return action;
		}
		
		public long getTicksRemaining() {
			ticksRemaining -= 1;
			
			return ticksRemaining;
		}
	}
}
