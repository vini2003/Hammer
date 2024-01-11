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

package dev.vini2003.hammer.zone.registry.client;

import dev.vini2003.hammer.zone.api.common.zone.Zone;
import org.jetbrains.annotations.Nullable;

public class HZValues {
	public static boolean ZONE_EDITOR = false;
	
	@Nullable
	private static Zone MOUSE_SELECTED_ZONE = null;
	
	@Nullable
	private static Zone COMMAND_SELECTED_ZONE = null;
	
	public static Zone getSelectedZone() {
		if (COMMAND_SELECTED_ZONE != null) {
			return COMMAND_SELECTED_ZONE;
		} else {
			return MOUSE_SELECTED_ZONE;
		}
	}
	
	@Nullable
	public static Zone getMouseSelectedZone() {
		return MOUSE_SELECTED_ZONE;
	}
	
	@Nullable
	public static Zone getCommandSelectedZone() {
		return COMMAND_SELECTED_ZONE;
	}
	
	public static void setMouseSelectedZone(Zone zone) {
		MOUSE_SELECTED_ZONE = zone;
	}
	
	public static void setCommandSelectedZone(Zone zone) {
		COMMAND_SELECTED_ZONE = zone;
	}
}
