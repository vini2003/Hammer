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

package dev.vini2003.hammer.chat;

import dev.vini2003.hammer.chat.impl.common.config.HCConfig;
import dev.vini2003.hammer.chat.registry.common.HCCommands;
import dev.vini2003.hammer.chat.registry.common.HCEvents;
import dev.vini2003.hammer.chat.registry.common.HCNetworking;
import dev.vini2003.hammer.persistence.api.common.config.Config;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class HC {
	public static final String ID = "hammer-chat";
	
	public static final HCConfig CONFIG = Config.getOrCreate(ID, HCConfig.class);
	
	public static void onInitialize() {
		HCCommands.init();
		HCEvents.init();
		HCNetworking.init();
		
		dev.vini2003.hammer.core.HC.LOGGER.info("Initialized '" + dev.vini2003.hammer.core.HC.CHAT_MODULE_ID + "] module.");
	}
}
