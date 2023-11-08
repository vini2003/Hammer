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

import dev.vini2003.hammer.zone.api.common.zone.Zone;
import dev.vini2003.hammer.zone.registry.common.HZComponents;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.Collection;

public class ZoneManager {
	/**
	 * Returns all the zones in the given world.
	 * @param world The world to get the zones from.
	 * @return All the zones in the given world.
	 */
	public static Collection<Zone> getAll(World world) {
		return HZComponents.ZONES.get(world).getAll();
	}
	
	/**
	 * Returns the zone with the given ID, or null if no zone with the given ID exists, from the specified world.
	 * @param world The world to search in.
	 * @param id The ID of the zone to search for.
	 * @return The zone with the given ID, or null if no zone with the given ID exists.
	 */
	public static Zone get(World world, Identifier id) {
		return HZComponents.ZONES.get(world).getById(id);
	}
	
	/**
	 * Adds a zone to the given world.
	 * @param world The world to add the zone to.
	 * @param zone The zone to add.
	 */
	public static void add(World world, Zone zone) {
		HZComponents.ZONES.get(world).add(zone);
		sync(world);
	}
	
	/**
	 * Removes the zone from the given world.
	 * @param world The world to remove the zone from.
	 * @param zone The zone to remove.
	 */
	public static void remove(World world, Zone zone) {
		HZComponents.ZONES.get(world).remove(zone);
		sync(world);
	}
	
	/**
	 * Synchronizes the world's zones with the client.
	 * @param world	The world to sync.
	 */
	public static void sync(World world) {
		HZComponents.ZONES.sync(world);
	}
}
