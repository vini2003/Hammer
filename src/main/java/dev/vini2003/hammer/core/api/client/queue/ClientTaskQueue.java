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
