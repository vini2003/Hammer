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

package dev.vini2003.hammer.component.registry.client;

import dev.vini2003.hammer.component.api.common.manager.ComponentManager;
import dev.vini2003.hammer.component.impl.common.component.holder.ComponentHolder;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import static dev.vini2003.hammer.component.registry.common.HCNetworking.SYNC_COMPONENT;
import static dev.vini2003.hammer.component.registry.common.HCNetworking.SYNC_COMPONENT_CONTAINER;

public class HCNetworking {
	public static void init() {
		ClientPlayNetworking.registerGlobalReceiver(SYNC_COMPONENT_CONTAINER, (client, handler, buf, responseSender) -> {
			var compHolder = buf.readInt();
			var containerNbt = buf.readNbt();
			
			switch (compHolder) {
				case ComponentHolder.ENTITY -> {
					var entityId = buf.readInt();
					
					client.execute(() -> {
						if (client.world.getEntityById(entityId) instanceof ComponentHolder holder) {
							holder.getComponentContainer().readFromNbt(containerNbt);
						}
					});
				}
				
				case ComponentHolder.WORLD -> {
					client.execute(() -> {
						if (client.world instanceof ComponentHolder holder) {
							holder.getComponentContainer().readFromNbt(containerNbt);
						}
					});
				}
			}
		});
		
		ClientPlayNetworking.registerGlobalReceiver(SYNC_COMPONENT, (client, handler, buf, responseSender) -> {
			var compId = buf.readIdentifier();
			var compKey = ComponentManager.getKey(compId);
			var compHolder = buf.readInt();
			var compNbt = buf.readNbt();
			
			switch (compHolder) {
				case ComponentHolder.ENTITY -> {
					var entityId = buf.readInt();
					
					client.execute(() -> {
						if (client.world.getEntityById(entityId) instanceof ComponentHolder holder) {
							var component = holder.getComponentContainer().get(compKey);
							component.readFromNbt(compNbt);
						}
					});
				}
				
				case ComponentHolder.WORLD -> {
					client.execute(() -> {
						if (client.world instanceof ComponentHolder holder) {
							var component = holder.getComponentContainer().get(compKey);
							component.readFromNbt(compNbt);
						}
					});
				}
			}
		});
	}
}
