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

package dev.vini2003.hammer.core.api.client.util;

import dev.vini2003.hammer.core.api.common.math.position.Position;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class PositionUtil {
	@Environment(EnvType.CLIENT)
	public static Position getMousePosition() {
		return new Position(getMouseX(), getMouseY());
	}
	
	@Environment(EnvType.CLIENT)
	public static float getMouseX() {
		var client = InstanceUtil.getClient();
		
		if (client == null || client.mouse == null) {
			return 0.0F;
		}
		
		return (float) (client.mouse.getX() * (client.getWindow().getScaledWidth() / (Math.max(1.0F, client.getWindow().getWidth()))));
	}
	
	@Environment(EnvType.CLIENT)
	public static float getMouseY() {
		var client = InstanceUtil.getClient();
		
		if (client == null || client.mouse == null) {
			return 0.0F;
		}
		
		return (float) (client.mouse.getY() * (client.getWindow().getScaledHeight() / (Math.max(1.0F, client.getWindow().getHeight()))));
	}
}
