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

package dev.vini2003.hammer.preset.registry.common;

import dev.vini2003.hammer.permission.api.common.manager.RoleManager;
import dev.vini2003.hammer.permission.api.common.role.Role;
import dev.vini2003.hammer.preset.HP;

public class HPRoles {
	public static final Role PARTICIPANT = new Role("participant", "§#83cbff\uD83C\uDF10", 1, 0x83CBFF);
	public static final Role SPECTATOR = new Role("spectator", "§#7e7e7e\uD83D\uDCF9", 1, 0x7E7E7E);
	public static final Role STAFF = new Role("staff", "§#fb00c4\uD83D\uDD2E", 2, 0xFB00C4);
	
	public static void init() {
		if (HP.CONFIG.enableRoles) {
			RoleManager.createRole(PARTICIPANT);
			RoleManager.createRole(SPECTATOR);
			RoleManager.createRole(STAFF);
		}
	}
}
