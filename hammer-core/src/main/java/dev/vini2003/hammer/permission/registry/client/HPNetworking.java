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

package dev.vini2003.hammer.permission.registry.client;

import dev.vini2003.hammer.permission.api.common.manager.RoleManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import java.util.ArrayList;
import java.util.UUID;

import static dev.vini2003.hammer.permission.registry.common.HPNetworking.*;

public class HPNetworking {
	public static void init() {
		ClientPlayNetworking.registerGlobalReceiver(SYNC_ROLES, (client, handler, buf, responseSender) -> {
			var name = buf.readString();
			
			var holderSize = buf.readInt();
			var holders = new ArrayList<UUID>(holderSize);
			
			for (var i = 0; i < holderSize; ++i) {
				holders.add(buf.readUuid());
			}
			
			// TODO: Use List setter.
			client.execute(() -> {
				var role = RoleManager.getRoleByName(name);
				
				role.getHolders().clear();
				
				for (var holder : holders) {
					role.getHolders().add(holder);
				}
			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(ADD_ROLE, (client, handler, buf, responseSender) -> {
			var name = buf.readString();
			var uuid = buf.readUuid();
			
			client.execute(() -> {
				RoleManager.getRoleByName(name).getHolders().add(uuid);
			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(REMOVE_ROLE, (client, handler, buf, responseSender) -> {
			var name = buf.readString();
			var uuid = buf.readUuid();
			
			client.execute(() -> {
				RoleManager.getRoleByName(name).getHolders().remove(uuid);
			});
		});
	}
}
