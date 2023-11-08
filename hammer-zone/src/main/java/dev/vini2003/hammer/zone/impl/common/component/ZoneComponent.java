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

import dev.vini2003.hammer.core.api.common.component.base.Component;
import dev.vini2003.hammer.zone.api.common.zone.Zone;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ZoneComponent implements Component {
	private static final String HAMMER$ZONE_KEY = "Hammer$Zone";
	private static final String HAMMER$ZONES_KEY = "Hammer$Zones";
	
	private Set<Zone> zones = new HashSet<>();
	
	private World world;
	
	public ZoneComponent(World world) {
		this.world = world;
	}
	
	public Collection<Zone> getAll() {
		return zones;
	}
	
	@Nullable
	public Zone getById(Identifier id) {
		return zones.stream()
					.filter(it -> it.getId().equals(id))
					.findFirst()
					.orElse(null);
	}
	
	public void add(Zone zone) {
		zones.add(zone);
	}
	
	public void remove(Zone zone) {
		zones.remove(zone);
	}
	
	@Override
	public void writeToNbt(NbtCompound nbt) {
		var zoneNbtList = new NbtList();
		
		for (var zone : zones) {
			zoneNbtList.add(Zone.toNbt(zone));
		}
		
		nbt.put(HAMMER$ZONES_KEY, zoneNbtList);
	}
	
	@Override
	public void readFromNbt(NbtCompound nbt) {
		var zoneNbtList = nbt.getList(HAMMER$ZONES_KEY, NbtElement.COMPOUND_TYPE);
		
		for (var zoneElement : zoneNbtList) {
			var zoneNbt = (NbtCompound) zoneElement;
			var zone = Zone.fromNbt(zoneNbt);
			
			if (!zones.contains(zone)) {
				zones.add(zone);
			} else {
				var existingZone = getById(zone.getId());
				
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
