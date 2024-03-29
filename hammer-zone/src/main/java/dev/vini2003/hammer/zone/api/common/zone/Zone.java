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

package dev.vini2003.hammer.zone.api.common.zone;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.vini2003.hammer.core.api.client.color.Color;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.position.StaticPosition;
import dev.vini2003.hammer.zone.api.common.manager.ZoneGroupManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.Objects;

/**
 * <p>A {@link Zone} represents a three-dimensional space in a world,
 * which may be associated with a group.</p>
 *
 * <p>The following serialization methods are provided:</p>
 * <ul>
 *     <li>{@link #toJson(Zone)} - from {@link Zone} to {@link JsonElement}.</li>
 *     <li>{@link #toNbt(Zone)} - from {@link Zone} to {@link NbtCompound}.</li>
 *     <li>{@link #toBuf(Zone, PacketByteBuf)} - from {@link Zone} to {@link PacketByteBuf}.</li>
 * </ul>
 
 * <ul>
 *     <li>{@link #fromJson(JsonElement)} from {@link JsonElement} to {@link Zone}.</li>
 *     <li>{@link #fromNbt(NbtCompound)} from {@link NbtCompound} to {@link Zone}.</li>
 *     <li>{@link #fromBuf(PacketByteBuf)} from {@link PacketByteBuf} to {@link Zone}.</li>
 * </ul>
 */
public class Zone {
	private final RegistryKey<World> world;
	private final Identifier id;
	
	private StaticPosition minPos;
	private StaticPosition maxPos;
	
	private transient StaticPosition lerpedMinPos;
	private transient StaticPosition lerpedMaxPos;
	
	private Color color = new Color((long) 0x25ABFF7E);
	
	private ZoneGroup group;
	
	private transient boolean removed = false;
	private transient boolean locked = false;
	
	/**
	 * Serializes a zone to a {@link PacketByteBuf}.
	 * @param zone The zone to serialize.
	 * @param buf The buffer to serialize to.
	 * @return The buffer.
	 */
	public static PacketByteBuf toBuf(Zone zone, PacketByteBuf buf) {
		buf.hammer$writeRegistryKey(zone.getWorld());
		buf.writeIdentifier(zone.getId());
		StaticPosition.toBuf(zone.getMinPos(), buf);
		StaticPosition.toBuf(zone.getMaxPos(), buf);
		Color.toBuf(zone.getColor(), buf);
		
		if (zone.getGroup() != null) {
			buf.writeBoolean(true);
			buf.writeIdentifier(zone.getGroup().getId());
		} else {
			buf.writeBoolean(false);
		}
		
		return buf;
	}
	
	/**
	 * Deserializes a zone from a {@link PacketByteBuf}.
	 * @param buf The buffer to deserialize from.
	 * @return The zone.
	 */
	public static Zone fromBuf(PacketByteBuf buf) {
		var world = buf.<World>hammer$readRegistryKey();
		var id = buf.readIdentifier();
		var minPos = StaticPosition.fromBuf(buf);
		var maxPos = StaticPosition.fromBuf(buf);
		var color = Color.fromBuf(buf);
		
		if (buf.readBoolean()) {
			var groupId = buf.readIdentifier();
			
			var group = ZoneGroupManager.getOrCreate(groupId);
			
			var zone = new Zone(world, id, minPos, maxPos);
			
			zone.setColor(color);
			zone.setGroup(group);
			
			return zone;
		} else {
			var zone = new Zone(world, id, minPos, maxPos);
			
			zone.setColor(color);
			
			return zone;
		}
	}
	
	/**
	 * Serializes a zone to an {@link NbtCompound}.
	 * @param zone The zone.
	 * @return The serialized zone.
	 */
	public static NbtCompound toNbt(Zone zone) {
		var nbt = new NbtCompound();
		
		nbt.hammer$putRegistryKey("WorldId", zone.getWorld());
		
		if (zone.getGroup() != null) {
			nbt.hammer$putIdentifier("GroupId", zone.getGroup().getId());
		}
		
		nbt.hammer$putIdentifier("Id", zone.getId());
		
		nbt.put("MinPos", StaticPosition.toNbt(zone.getMinPos()));
		nbt.put("MaxPos", StaticPosition.toNbt(zone.getMaxPos()));
		
		nbt.put("Color", Color.toNbt(zone.getColor()));
		
		if (zone.getGroup() != null) {
			nbt.hammer$putIdentifier("Group", zone.getGroup().getId());
		}
		
		return nbt;
	}
	
	/**
	 * Deserializes a zone from an {@link NbtCompound}.
	 * @param nbt The serialized zone.
	 * @return The zone.
	 */
	public static Zone fromNbt(NbtCompound nbt) {
		Zone zone;
		
		if (nbt.contains("group_id")) {
			zone = new Zone(
					nbt.hammer$getRegistryKey("WorldId"),
					nbt.hammer$getIdentifier("Id"),
					ZoneGroupManager.getOrCreate(
							nbt.hammer$getIdentifier("GroupId")
					),
					StaticPosition.fromNbt(nbt.getCompound("MinPos")),
					StaticPosition.fromNbt(nbt.getCompound("MaxPos"))
			);
		} else {
			zone = new Zone(
					nbt.hammer$getRegistryKey("WorldId"),
					nbt.hammer$getIdentifier("Id"),
					StaticPosition.fromNbt(nbt.getCompound("MinPos")),
					StaticPosition.fromNbt(nbt.getCompound("MaxPos"))
			);
		}
		
		zone.setColor(Color.fromNbt(nbt.get("Color")));
		
		if (nbt.contains("Group")) {
			zone.setGroup(
					ZoneGroupManager.getOrCreate(
							nbt.hammer$getIdentifier("Group")
					)
			);
		}
		
		return zone;
	}
	
	/**
	 * Serializes a zone to a {@link JsonElement}.
	 * @param zone The zone to serialize.
	 * @return The serialized zone.
	 */
	public static JsonElement toJson(Zone zone) {
		var json = new JsonObject();
		
		json.addProperty("world_id", zone.getWorld().getValue().toString());
		
		if (zone.getGroup() != null) {
			json.addProperty("group_id", zone.getGroup().getId().toString());
		}
		
		json.addProperty("id", zone.getId().toString());
		
		json.add("min_pos", StaticPosition.toJson(zone.getMinPos()));
		json.add("max_pos", StaticPosition.toJson(zone.getMaxPos()));
		json.add("color", Color.toJson(zone.getColor()));
		
		return json;
	}
	
	/**
	 * Deserializes a zone from a {@link JsonElement}.
	 * @param json The serialized zone.
	 * @return The deserialized zone.
	 */
	public static Zone fromJson(JsonElement json) {
		var object = json.getAsJsonObject();
		
		Zone zone;
		
		if (object.has("group_id")) {
			zone = new Zone(
					RegistryKey.of(RegistryKeys.WORLD, new Identifier(object.get("world_id").getAsString())),
					new Identifier(object.get("id").getAsString()),
					ZoneGroupManager.getOrCreate(new Identifier(object.get("zone_id").getAsString())),
					StaticPosition.fromJson(object.get("min_pos")),
					StaticPosition.fromJson(object.get("max_pos"))
			);
		} else {
			zone = new Zone(
					RegistryKey.of(RegistryKeys.WORLD, new Identifier(object.get("world_id").getAsString())),
					new Identifier(object.get("id").getAsString()),
					StaticPosition.fromJson(object.get("min_pos")),
					StaticPosition.fromJson(object.get("max_pos"))
			);
		}
		
		zone.setColor(Color.fromJson(object.get("color")));
		
		return zone;
	}
	
	public Zone(RegistryKey<World> world, Identifier id, StaticPosition startPos, StaticPosition endPos) {
		this.world = world;
		
		this.id = id;
		
		this.minPos = StaticPosition.min(startPos, endPos);
		this.maxPos = StaticPosition.max(startPos, endPos);
		
		this.lerpedMinPos = getCenterPos();
		this.lerpedMaxPos = getCenterPos();
	}
	
	public Zone(RegistryKey<World> world, Identifier id, ZoneGroup group, StaticPosition startPos, StaticPosition endPos) {
		this.world = world;
		
		this.id = id;
		this.group = group;
		
		this.minPos = StaticPosition.min(startPos, endPos);
		this.maxPos = StaticPosition.max(startPos, endPos);
		
		this.lerpedMinPos = getCenterPos();
		this.lerpedMaxPos = getCenterPos();
	}
	
	
	/**
	 * Returns thiz zone's world key.
	 * @return The world key.
	 */
	public RegistryKey<World> getWorld() {
		return world;
	}
	
	/**
	 * Returns this zone's ID.
	 * @return This zone's ID.
	 */
	public Identifier getId() {
		return id;
	}
	
	/**
	 * Returns this zone's minimum positon.
	 * @return This zone's minimum position.
	 */
	public StaticPosition getMinPos() {
		return minPos;
	}
	
	/**
	 * Sets this zone's minimum position.
	 * @param minPos The new minimum position.
	 */
	public void setMinPos(StaticPosition minPos) {
		this.minPos = StaticPosition.min(maxPos, minPos);
		this.maxPos = StaticPosition.max(maxPos, minPos);
	}
	
	/**
	 * Returns this zone's maximum position.
	 * @return This zone's maximum position.
	 */
	public StaticPosition getMaxPos() {
		return maxPos;
	}
	
	/**
	 * Sets this zone's maximum position.
	 * @param maxPos The new maximum position.
	 */
	public void setMaxPos(StaticPosition maxPos) {
		this.minPos = StaticPosition.min(maxPos, minPos);
		this.maxPos = StaticPosition.max(maxPos, minPos);
	}
	
	/**
	 * Returns this zone's center position.
	 * @return This zone's center position.
	 */
	public StaticPosition getCenterPos() {
		return Position.of(
				(minPos.getX() + maxPos.getX()) / 2,
				(minPos.getY() + maxPos.getY()) / 2,
				(minPos.getZ() + maxPos.getZ()) / 2
		);
	}
	
	/**
	 * Returns this zone's linearly interpolated minimum position,
	 * for usage in rendering.
	 * @param tickDelta The tick delta.
	 * @return The interpolated minimum position.
	 */
	public StaticPosition getLerpedMinPos(float tickDelta) {
		lerpedMinPos = Position.of(
				MathHelper.lerp(tickDelta, lerpedMinPos.getX(), minPos.getX()),
				MathHelper.lerp(tickDelta, lerpedMinPos.getY(), minPos.getY()),
				MathHelper.lerp(tickDelta, lerpedMinPos.getZ(), minPos.getZ())
		);
		
		return lerpedMinPos;
	}
	
	/**
	 * Returns this zone's linearly interpolated maximum position,
	 * for usage in rendering.
	 * @param tickDelta The tick delta.
	 * @return The interpolated maximum position.
	 */
	public StaticPosition getLerpedMaxPos(float tickDelta) {
		lerpedMaxPos = Position.of(
				MathHelper.lerp(tickDelta, lerpedMaxPos.getX(), maxPos.getX()),
				MathHelper.lerp(tickDelta, lerpedMaxPos.getY(), maxPos.getY()),
				MathHelper.lerp(tickDelta, lerpedMaxPos.getZ(), maxPos.getZ())
		);
		
		return lerpedMaxPos;
	}
	
	/**
	 * Returns whether this zone contains a position or not.
	 * @param position The position.
	 * @return Whether this zone contains the position or not.
	 */
	public boolean isPositionWithin(StaticPosition position) {
		return position.getX() >= minPos.getX() && position.getX() <= maxPos.getX() &&
			   position.getY() >= minPos.getY() && position.getY() <= maxPos.getY() &&
			   position.getZ() >= minPos.getZ() && position.getZ() <= maxPos.getZ();
	}
	
	/**
	 * Returns whether this zone contains a  coordinate or not.
	 * @param x The X coordinate component.
	 * @param y The Y coordinate component.
	 * @param z The Z coordinate component.
	 * @return Whether this zone contains the coordinate or not.
	 */
	public boolean isPositionWithin(float x, float y, float z) {
		return x >= minPos.getX() && x <= maxPos.getX() &&
			   y >= minPos.getY() && y <= maxPos.getY() &&
			   z >= minPos.getZ() && z <= maxPos.getZ();
	}
	
	/**
	 * Returns whether this zone contains a  coordinate or not.
	 * @param x The X coordinate component.
	 * @param z The Z coordinate component.
	 * @return Whether this zone contains the coordinate or not.
	 */
	 public boolean isPositionWithin(float x, float z) {
		return x >= minPos.getX() && x <= maxPos.getX() &&
			   z >= minPos.getZ() && z <= maxPos.getZ();
	 }
	
	/**
	 * Returns the positions within this zone.
	 *
	 * @return the result.
	 */
	public Collection<StaticPosition> getPositions() {
		return StaticPosition.collect(minPos, maxPos);
	}
	
	/**
	 * Returns this zone's color.
	 * @return This zone's color.
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * Sets this zone's color.
	 * @param color The new color.
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	
	/**
	 * Sets this zone as removed.
	 */
	public void markRemoved() {
		this.removed = true;
		
		var centerPos = getCenterPos();
		
		this.setMinPos(centerPos);
		this.setMaxPos(centerPos);
		
		// TODO: Check if this works as expected.
		this.setGroup(null);
	}
	
	/**
	 * Returns whether the zone is removed or not.
	 * @return Whether the zone is removed or not.
	 */
	public boolean isRemoved() {
		return removed;
	}
	
	/**
	 * Sets whether the zone is locked or not.
	 * @param locked Whether the zone is locked or not.
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	/**
	 * Returns whether this zone is locked or not.
	 * @return Whether this zone is locked or not.
	 */
	public boolean isLocked() {
		return locked;
	}
	
	/**
	 * Returns this zone's group.
	 * @return This zone's group.
	 */
	public ZoneGroup getGroup() {
		return group;
	}
	
	/**
	 * Sets the zone's group.
	 * @param group The new group.
	 */
	public void setGroup(ZoneGroup group) {
		if (this.group != null && group == null) {
			this.group.remove(this);
		} else if (this.group == null && group != null) {
			group.add(this);
		}
		
		this.group = group;
	}
	
	/**
	 * Returns this zone's width.
	 * @return This zone's width.
	 */
	public float getWidth() {
		return maxPos.getX() - minPos.getX();
	}
	
	/**
	 * Returns this zone's height.
	 * @return This zone's height.
	 */
	public float getHeight() {
		return maxPos.getY() - minPos.getY();
	}
	
	/**
	 * Returns this zone's depth.
	 * @return This zone's depth.
	 */
	public float getDepth() {
		return maxPos.getZ() - minPos.getZ();
	}
	
	/**
	 * Scales this zone by the given amount in all axis.
	 *
	 * @param amount the amount.
	 */
	public void scale(int amount) {
		setMinPos(minPos.offset(-amount, -amount, -amount));
		setMaxPos(maxPos.offset(amount, amount, amount));
	}
	
	/**
	 * Stretches this zone by the given amount in the specified axis.
	 *
	 * @param axis the axis.
	 * @param amount the amount.
	 */
	public void stretch(Direction axis, int amount) {
		switch (axis) {
			case EAST -> setMaxPos(maxPos.offset(amount, 0, 0));
			case WEST -> setMinPos(minPos.offset(-amount, 0, 0));
			case SOUTH -> setMaxPos(maxPos.offset(0, 0, amount));
			case NORTH -> setMinPos(minPos.offset(0, 0, -amount));
			case UP -> setMaxPos(maxPos.offset(0, amount, 0));
			case DOWN -> setMaxPos(maxPos.offset(0, -amount, 0));
			
		}
	}
	
	/**
	 * Moves this zone by the given offset in the specified axis.
	 *
	 * @param axis the axis.
	 * @param amount the amount.
	 */
	public void move(Direction axis, int amount) {
		switch (axis) {
			case EAST -> {
				setMinPos(minPos.offset(amount, 0, 0));
				setMaxPos(maxPos.offset(amount, 0, 0));
			}
			
			case WEST -> {
				setMinPos(minPos.offset(-amount, 0, 0));
				setMaxPos(maxPos.offset(-amount, 0, 0));
			}
			
			case SOUTH -> {
				setMinPos(minPos.offset(0, 0, amount));
				setMaxPos(maxPos.offset(0, 0, amount));
			}
			
			case NORTH -> {
				setMinPos(minPos.offset(0, 0, -amount));
				setMaxPos(maxPos.offset(0, 0, -amount));
			}
			
			case UP -> {
				setMinPos(minPos.offset(0, amount, 0));
				setMaxPos(maxPos.offset(0, amount, 0));
			}
			
			case DOWN -> {
				setMinPos(minPos.offset(0, -amount, 0));
				setMaxPos(maxPos.offset(0, -amount, 0));
			}
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		
		if (!(o instanceof Zone)) {
			return false;
		}
		
		var zone = (Zone) o;
		
		return Objects.equals(id, zone.id);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(minPos, maxPos);
	}
}
