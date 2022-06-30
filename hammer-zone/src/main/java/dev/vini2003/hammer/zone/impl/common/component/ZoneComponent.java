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

package dev.vini2003.hammer.zone.impl.common.component;

import dev.vini2003.hammer.component.api.common.component.Component;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.zone.api.common.util.ZoneNbtUtil;
import dev.vini2003.hammer.zone.api.common.zone.Zone;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ZoneComponent implements Component {
	private static final String HAMMER$ZONE_KEY = "Hammer$Zone";
	private static final String HAMMER$ZONES_KEY = "Hammer$Zones";
	
	private Collection<Zone> zones = new ArrayList<>();
	private Map<Identifier, Zone> zonesById = new HashMap<>();
	
	private World world;
	
	public ZoneComponent(World world) {
		this.world = world;
	}
	
	public Collection<Zone> getZones() {
		return zones;
	}
	
	@Nullable
	public Zone getZoneById(Identifier id) {
		return zonesById.get(id);
	}
	
	public void add(Zone zone) {
		zones.add(zone);
		zonesById.put(zone.getId(), zone);
	}
	
	public void remove(Zone zone) {
		zones.remove(zone);
		zonesById.remove(zone.getId());
	}
	
	@Override
	public void writeToNbt(NbtCompound nbt) {
		var zoneNbtList = new NbtList();
		
		for (var zone : zones) {
			var zoneNbt = new NbtCompound();
			ZoneNbtUtil.putZone(zoneNbt, HAMMER$ZONE_KEY, zone);
			
			zoneNbtList.add(zoneNbt);
		}
		
		nbt.put(HAMMER$ZONES_KEY, zoneNbtList);
	}
	
	@Override
	public void readFromNbt(NbtCompound nbt) {
		var zoneNbtList = nbt.getList(HAMMER$ZONES_KEY, NbtElement.COMPOUND_TYPE);
		
		for (var zoneElement : zoneNbtList) {
			var zoneNbt = (NbtCompound) zoneElement;
			var zone = ZoneNbtUtil.getZone(zoneNbt, HAMMER$ZONE_KEY);
			
			if (!zonesById.containsKey(zone.getId())) {
				zones.add(zone);
				
				zonesById.put(zone.getId(), zone);
			} else {
				var existingZone = zonesById.get(zone.getId());
				
				existingZone.setMinPos(zone.getMinPos());
				existingZone.setMaxPos(zone.getMaxPos());
				
				existingZone.setColor(zone.getColor());
				
				if (zone.isRemoved()) {
					existingZone.markRemoved();
				}
			}
		}
	}
}
