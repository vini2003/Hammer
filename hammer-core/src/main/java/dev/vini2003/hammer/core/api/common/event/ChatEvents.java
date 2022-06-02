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

package dev.vini2003.hammer.core.api.common.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.MessageType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;

import java.util.UUID;

public interface ChatEvents {
	Event<SendMessage> SEND_MESSAGE = EventFactory.createArrayBacked(SendMessage.class, (events) -> (receiver, message, type, sender) -> {
		var result = TypedActionResult.success(message);
		
		var cancel = false;
		
		for (var event : events) {
			var newResult = event.send(receiver, result.getValue(), type, sender);
			
			if (newResult.getResult().isAccepted()) {
				result = newResult;
				
				continue;
			}
			
			if (newResult.getResult() == ActionResult.FAIL) {
				cancel = true;
				
				continue;
			}
		}
		
		if (cancel) {
			return TypedActionResult.fail(result.getValue());
		} else {
			return result;
		}
	});
	
	@FunctionalInterface
	interface SendMessage {
		TypedActionResult<Text> send(ServerPlayerEntity receiver, Text message, MessageType type, UUID sender);
	}
}
