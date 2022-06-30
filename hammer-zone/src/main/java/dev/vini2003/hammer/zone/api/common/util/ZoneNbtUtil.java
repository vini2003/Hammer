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

package dev.vini2003.hammer.zone.api.common.util;

import dev.vini2003.hammer.core.api.client.color.Color;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.util.NbtUtil;
import dev.vini2003.hammer.zone.api.common.manager.ZoneGroupManager;
import dev.vini2003.hammer.zone.api.common.zone.Zone;
import dev.vini2003.hammer.zone.api.common.zone.ZoneGroup;
import net.minecraft.nbt.NbtCompound;

public class ZoneNbtUtil {
	public static void putZone(NbtCompound nbt, String key, Zone value) {
		NbtUtil.putIdentifier(nbt, key + "Id", value.getId());
		NbtUtil.putPosition(nbt, key + "MinPos", value.getMinPos());
		NbtUtil.putPosition(nbt, key + "MaxPos", value.getMaxPos());
		
		nbt.putLong(key + "Color", value.getColor().toRgba());
		nbt.putBoolean(key + "Removed", value.isRemoved());
		
		var group = value.getGroup();
		
		if (group != null) {
			NbtUtil.putIdentifier(nbt, key + "GroupId", group.getId());
		}
	}
	
	public static Zone getZone(NbtCompound nbt, String key) {
		ZoneGroup group = null;
		
		if (nbt.contains(key + "GroupId")) {
			var groupId = NbtUtil.getIdentifier(nbt, key + "GroupId");
			group = ZoneGroupManager.getOrCreate(groupId);
		}
		
		var zone = new Zone(
				group,
				NbtUtil.getIdentifier(nbt, key + "Id"),
				NbtUtil.getPosition(nbt, key + "MinPos"),
				NbtUtil.getPosition(nbt, key + "MaxPos"),
				new Color(nbt.getLong(key + "Color"))
		);
		
		if (nbt.getBoolean(key + "Removed")) {
			zone.markRemoved();
		}
		
		return zone;
	}
}
