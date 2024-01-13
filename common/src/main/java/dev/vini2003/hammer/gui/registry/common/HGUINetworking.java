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

package dev.vini2003.hammer.gui.registry.common;

import dev.architectury.networking.NetworkManager;
import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.gui.api.common.event.base.Event;
import dev.vini2003.hammer.gui.api.common.screen.handler.BaseScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class HGUINetworking {
	public static Identifier SYNC_SCREEN_HANDLER = HC.id("sync_screen_handler");
	public static Identifier SYNC_WIDGET_EVENT = HC.id("sync_widget_event");
	
	public static void init() {
		NetworkManager.registerReceiver(NetworkManager.Side.C2S, SYNC_SCREEN_HANDLER, (buf, context) -> {
			var player = (ServerPlayerEntity) context.getPlayer();
			var server = player.getServer();
			var handler = player.networkHandler;
			
			try {
				var width = buf.readInt();
				var height = buf.readInt();
				
				server.execute(() -> {
					var screenHandler = (BaseScreenHandler) player.currentScreenHandler;
					
					screenHandler.getChildren().clear();
					screenHandler.getSlots().clear();
					
					screenHandler.init(width, height);
				});
			} catch (Exception e) {
				HC.LOGGER.error("Failed to synchronize screen handler!");
				e.printStackTrace();
			}
		});
		
		NetworkManager.registerReceiver(NetworkManager.Side.C2S, SYNC_WIDGET_EVENT, (buf, context) -> {
			var player = (ServerPlayerEntity) context.getPlayer();
			var server = player.getServer();
			var handler = player.networkHandler;
			
			try {
				var id = buf.readString();
				
				var event = Event.fromBuf(buf);
				
				server.execute(() -> {
					var screenHandler = (BaseScreenHandler) player.currentScreenHandler;
					
					for (var child : screenHandler.getAllChildren()) {
						if (child.getId() != null && child.getId().equals(id)) {
							child.dispatchEvent(event);
						}
					}
				});
			} catch (Exception e) {
				HC.LOGGER.error("Failed to synchronize widget event");
				e.printStackTrace();
			}
		});
	}
}
