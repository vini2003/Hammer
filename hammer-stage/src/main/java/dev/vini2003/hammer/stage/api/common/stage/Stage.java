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

package dev.vini2003.hammer.stage.api.common.stage;

import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.zone.api.common.manager.ZoneManager;
import dev.vini2003.hammer.zone.api.common.zone.Zone;
import dev.vini2003.hammer.zone.api.common.zone.ZoneGroup;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;

public abstract class Stage {
	private final Identifier id;
	
	private Position position;
	private Size size;
	
	private Zone zone;
	
	private State state;
	private State prevState;
	
	private final Collection<Zone> zones = new ArrayList<>();
	private final Collection<ZoneGroup> zoneGroups = new ArrayList<>();
	
	public Stage(Identifier id, Position position, Size size) {
		this.id = id;
		
		this.position = position;
		this.size = size;
	}
	
	/**
	 * Returns the position of the stage.
	 * @return The position of the stage.
	 */
	public Position getPosition() {
		return position;
	}
	
	/**
	 * Sets the position of the stage.
	 * @param position The new position of the stage.
	 */
	public void setPosition(Position position) {
		this.position = position;
	}
	
	/**
	 * Returns the size of the stage.
	 * @return The size of the stage.
	 */
	public Size getSize() {
		return size;
	}
	
	/**
	 * Sets the size of the stage.
	 * @param size The new size of the stage.
	 */
	public void setSize(Size size) {
		this.size = size;
	}
	
	/**
	 * Returns the zone of the stage.
	 * @return The zone of the stage.
	 */
	public Zone getZone() {
		return zone;
	}
	
	/**
	 * Returns the state of the stage.
	 * @return The state of the stage.
	 */
	public State getState() {
		return state;
	}
	
	/**
	 * Sets the state of the stage.
	 * @param state The new state of the stage.
	 */
	public void setState(State state) {
		this.state = state;
	}
	
	/**
	 * Adds a zone to this stage.
	 * @param zone The zone to add.
	 */
	public void addZone(Zone zone) {
		this.zones.add(zone);
	}
	
	/**
	 * Removes a zone from this stage.
	 * @param zone The zone to remove.
	 */
	public void removeZone(Zone zone) {
		this.zones.remove(zone);
	}

	/**
	 * Returns the zones of this stage.
	 * @return The zones of this stage.
	 */
	public Collection<Zone> getZones() {
		return zones;
	}
	
	/**
	 * Adds a zone group to this stage.
	 * @param zoneGroup The zone group to add.
	 */
	public void addZoneGroup(ZoneGroup zoneGroup) {
		this.zoneGroups.add(zoneGroup);
	}
	
	/**
	 * Removes a zone group from this stage.
	 * @param zoneGroup The zone group to remove.
	 */
	public void removeZoneGroup(ZoneGroup zoneGroup) {
		this.zoneGroups.remove(zoneGroup);
	}
	
	/**
	 * Returns the zone groups of this stage.
	 * @return The zone groups of this stage.
	 */
	public Collection<ZoneGroup> getZoneGroups() {
		return zoneGroups;
	}
	
	/**
	 * Executed when the stage is loaded.
	 * @param world The world in which the stage was loaded.
	 */
	public void load(World world) {
		if (zone != null) {
			ZoneManager.remove(world, zone);
			
			this.zone = null;
		}
		
		this.zone = new Zone(
				world.getRegistryKey(),
				id,
				position.minus(new Position(size.getWidth() / 2, size.getHeight() / 2, size.getLength() / 2)),
				position.plus(new Position(size.getWidth() / 2, size.getHeight() / 2, size.getLength() / 2))
		);
		
		ZoneManager.add(world, zone);
	}
	
	/**
	 * Executed when the stage is unloaded.
	 * @param world The world in which the stage was unloaded.
	 */
	public void unload(World world) {
		if (zone != null) {
			ZoneManager.remove(world, zone);
			
			this.zone = null;
		}
	}
	
	/**
	 * Executed when the stage is prepared.
	 * @param world The world in which the stage was prepared.
	 */
	public void prepare(World world) {
		this.prevState = state;
		this.state = State.PREPARED;
	}
	
	/**
	 * Executed when the stage is started.
	 * @param world The world in which the stage was started.
	 */
	public void start(World world) {
		this.prevState = state;
		this.state = State.STARTED;
	}
	
	/**
	 * Executed when the stage is stopped.
	 * @param world The world in which the stage was stopped.
	 */
	public void stop(World world) {
		this.prevState = state;
		this.state = State.STOPPED;
	}
	
	/**
	 * Executed when the stage is paused.
	 * @param world The world in which the stage was paused.
	 */
	public void pause(World world) {
		if (this.state != State.PAUSED) {
			this.prevState = state;
			
			this.state = State.PAUSED;
		} else {
			var prevState = this.state;
			
			this.state = this.prevState;
			this.prevState = prevState;
		}
	}
	
	/**
	 * Executed when the stage is restarted.
	 * @param world The world in which the stage was restarted.
	 */
	public void restart(World world) {
		prepare(world);
		start(world);
	}
	
	/**
	 * Executed every tick.
	 * @param world The world in which the stage is running.
	 */
	public void tick(World world) {
	
	}
	
	public static enum State {
		LOADED,
		UNLOADED,
		PREPARED,
		STARTED,
		STOPPED,
		PAUSED
	}
}
