package dev.vini2003.hammer.zone.api.common.manager;

import dev.vini2003.hammer.zone.api.common.zone.Zone;
import dev.vini2003.hammer.zone.api.common.zone.ZoneGroup;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class ZoneGroupManager {
	private static final ThreadLocal<Map<Identifier, ZoneGroup>> GROUPS = ThreadLocal.withInitial(() -> new HashMap<>());
	
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
}
