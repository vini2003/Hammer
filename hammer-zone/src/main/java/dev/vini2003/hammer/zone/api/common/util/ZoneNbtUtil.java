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
