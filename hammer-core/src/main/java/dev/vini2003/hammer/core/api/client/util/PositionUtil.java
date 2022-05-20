package dev.vini2003.hammer.core.api.client.util;

import dev.vini2003.hammer.core.api.common.math.position.Position;

public class PositionUtil {
	public static Position getMousePosition() {
		return new Position(getMouseX(), getMouseY());
	}
	
	public static float getMouseX() {
		var client = InstanceUtil.getClient();
		
		if (client == null) {
			return 0.0F;
		}
		
		return (float) (client.mouse.getX() * (client.getWindow().getScaledWidth() / (Math.max(1.0F, client.getWindow().getWidth()))));
	}
	
	public static float getMouseY() {
		var client = InstanceUtil.getClient();
		
		if (client == null) {
			return 0.0F;
		}
		
		return (float) (client.mouse.getY() * (client.getWindow().getScaledHeight() / (Math.max(1.0F, client.getWindow().getHeight()))));
	}
}
