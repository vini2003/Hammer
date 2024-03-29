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

package dev.vini2003.hammer.zone.api.common.manager;

import dev.vini2003.hammer.zone.api.common.zone.ZoneGroup;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ZoneGroupManager {
	private static final ThreadLocal<Map<Identifier, ZoneGroup>> GROUPS = ThreadLocal.withInitial(HashMap::new);
	
	/**
	 * Returns the group with the given ID, or creates
	 * and returns a new one if it does not exist.
	 * @param id The ID of the group.
	 * @return The group.
	 */
	public static ZoneGroup getOrCreate(Identifier id) {
		var groups = GROUPS.get();
		
		if (groups.containsKey(id)) {
			return groups.get(id);
		} else {
			var group = new ZoneGroup(id);
			groups.put(id, group);
			return group;
		}
	}
	
	/**
	 * Removes the group with the given ID.
	 * @param id The ID of the group.
	 */
	public static void remove(Identifier id) {
		var groups = GROUPS.get();
		
		if (groups.containsKey(id)) {
			var group = groups.get(id);
			
			for (var zone : group) {
				zone.setGroup(null);
			}
			
			groups.remove(group);
		}
	}
	
	/**
	 * Returns all zone groups.
	 * @return All zone groups.
	 */
	public static Collection<ZoneGroup> getGroups() {
		var groups = GROUPS.get();
		
		return groups.values();
	}
}
