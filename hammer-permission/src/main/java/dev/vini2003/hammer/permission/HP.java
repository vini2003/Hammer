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

package dev.vini2003.hammer.permission;

import dev.vini2003.hammer.permission.api.common.manager.RoleManager;
import dev.vini2003.hammer.permission.api.common.role.Role;
import dev.vini2003.hammer.permission.registry.common.HPCommands;
import dev.vini2003.hammer.permission.registry.common.HPEvents;
import dev.vini2003.hammer.permission.registry.common.HPNetworking;
import net.fabricmc.api.ModInitializer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class HP implements ModInitializer {
	public static LuckPerms getLuckPerms() {
		return LuckPermsProvider.get();
	}
	
	public static final Role NEIN = new Role("nein", "N", 1, 0xFF0000);
	
	@Override
	public void onInitialize() {
		HPEvents.init();
		HPNetworking.init();
		HPCommands.init();
	}
}
