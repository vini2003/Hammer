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

package dev.vini2003.hammer.core;

import dev.vini2003.hammer.core.api.common.util.HammerUtil;
import dev.vini2003.hammer.core.registry.client.HCEvents;
import dev.vini2003.hammer.core.registry.client.HCNetworking;
import net.fabricmc.api.ClientModInitializer;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class HCClient {
	private static final String HAMMER_CHAT_CLIENT_MOD_INITIALIZER = "dev.vini2003.hammer.chat.HCClient";
	private static final String HAMMER_GRAVITY_CLIENT_MOD_INITIALIZER = "dev.vini2003.hammer.gravity.HGClient";
	private static final String HAMMER_GUI_CLIENT_MOD_INITIALIZER = "dev.vini2003.hammer.gui.HGUIClient";
	private static final String HAMMER_PERMISSION_CLIENT_MOD_INITIALIZER = "dev.vini2003.hammer.permission.HPClient";
	private static final String HAMMER_PRESET_CLIENT_MOD_INITIALIZER = "dev.vini2003.hammer.preset.HPClient";
	private static final String HAMMER_UTIL_CLIENT_MOD_INITIALIZER = "dev.vini2003.hammer.util.HUClient";
	private static final String HAMMER_ZONE_CLIENT_MOD_INITIALIZER = "dev.vini2003.hammer.zone.HZClient";
	
	public static void onInitializeClient() {
		HCEvents.init();
		HCNetworking.init();
		
		HC.LOGGER.info("Initialized '" + HC.CORE_MODULE_ID + "' client module.");
		
		HammerUtil.initializeClientIfModuleEnabled("chat", HAMMER_CHAT_CLIENT_MOD_INITIALIZER);
		HammerUtil.initializeClientIfModuleEnabled("gravity", HAMMER_GRAVITY_CLIENT_MOD_INITIALIZER);
		HammerUtil.initializeClientIfModuleEnabled("gui", HAMMER_GUI_CLIENT_MOD_INITIALIZER);
		HammerUtil.initializeClientIfModuleEnabled("permission", HAMMER_PERMISSION_CLIENT_MOD_INITIALIZER);
		HammerUtil.initializeClientIfModuleEnabled("preset", HAMMER_PRESET_CLIENT_MOD_INITIALIZER);
		HammerUtil.initializeClientIfModuleEnabled("util", HAMMER_UTIL_CLIENT_MOD_INITIALIZER);
		HammerUtil.initializeClientIfModuleEnabled("zone", HAMMER_ZONE_CLIENT_MOD_INITIALIZER);
	}
}
