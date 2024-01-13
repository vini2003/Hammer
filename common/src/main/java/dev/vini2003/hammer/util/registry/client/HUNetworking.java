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

package dev.vini2003.hammer.util.registry.client;

import dev.architectury.networking.NetworkManager;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;

import static dev.vini2003.hammer.util.registry.common.HUNetworking.FLY_SPEED_UPDATE;

public class HUNetworking {
	public static void init() {
		NetworkManager.registerReceiver(NetworkManager.Side.S2C, FLY_SPEED_UPDATE, (buf, context) -> {
			var client = InstanceUtil.getClient();
			var handler = client.getNetworkHandler();
			
			var speed = buf.readInt();
			
			client.execute(() -> {
				client.player.getAbilities().setFlySpeed(speed / 20.0F);
			});
		});
		
		NetworkManager.registerReceiver(NetworkManager.Side.S2C, dev.vini2003.hammer.util.registry.common.HUNetworking.WALK_SPEED_UPDATE, (buf, context) -> {
			var client = InstanceUtil.getClient();
			var handler = client.getNetworkHandler();
			
			var speed = buf.readInt();
			
			client.execute(() -> {
				client.player.getAbilities().setWalkSpeed(speed / 20.0F);
			});
		});
	}
}
