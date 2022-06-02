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
