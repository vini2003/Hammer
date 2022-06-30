package dev.vini2003.hammer.zone.api.common.zone;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class ZoneGroup implements Iterable<Zone> {
	private Collection<Zone> zones = new HashSet<>();
	
	private Identifier id;
	
	public ZoneGroup(Identifier id) {
		this.id = id;
	}
	
	public Identifier getId() {
		return id;
	}
	
	/**
	 * Removes a zone from this group.
	 *
	 * @param zone the zone.
	 */
	public void add(Zone zone) {
		zones.add(zone);
	}
	
	/**
	 * Adds a zone to this group.
	 *
	 * @param zone the zone.
	 */
	public void remove(Zone zone) {
		zones.remove(zone);
	}
	
	@NotNull
	@Override
	public Iterator<Zone> iterator() {
		return zones.iterator();
	}
}
