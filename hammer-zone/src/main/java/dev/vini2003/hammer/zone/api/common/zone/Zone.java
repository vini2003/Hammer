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
import dev.vini2003.hammer.core.api.common.util.BufUtil;
import dev.vini2003.hammer.core.api.common.util.NbtUtil;
import dev.vini2003.hammer.zone.api.common.manager.ZoneGroupManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

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
	private RegistryKey<World> world;
	private Identifier id;
	
	private Position minPos;
	private Position maxPos;
	
	private transient Position lerpedMinPos;
	private transient Position lerpedMaxPos;
	
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
		BufUtil.writeRegistryKey(buf, zone.getWorld());
		buf.writeIdentifier(zone.getId());
		Position.toBuf(zone.getMinPos(), buf);
		Position.toBuf(zone.getMaxPos(), buf);
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
		var world = BufUtil.<World>readRegistryKey(buf);
		var id = buf.readIdentifier();
		var minPos = Position.fromBuf(buf);
		var maxPos = Position.fromBuf(buf);
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
		
		NbtUtil.putRegistryKey(nbt, "world", zone.getWorld());
		NbtUtil.putIdentifier(nbt, "id", zone.getId());
		
		nbt.put("minPos", Position.toNbt(zone.getMinPos()));
		nbt.put("maxPos", Position.toNbt(zone.getMaxPos()));
		
		nbt.put("color", Color.toNbt(zone.getColor()));
		
		if (zone.getGroup() != null) {
			NbtUtil.putIdentifier(nbt, "group", zone.getGroup().getId());
		}
		
		return nbt;
	}
	
	/**
	 * Deserializes a zone from an {@link NbtCompound}.
	 * @param nbt The serialized zone.
	 * @return The zone.
	 */
	public static Zone fromNbt(NbtCompound nbt) {
		var zone = new Zone(
				NbtUtil.getRegistryKey(nbt, "world"),
				NbtUtil.getIdentifier(nbt, "id"),
				Position.fromNbt(nbt.getCompound("minPos")),
				Position.fromNbt(nbt.getCompound("maxPos"))
		);
		
		zone.setColor(Color.fromNbt(nbt.getCompound("color")));
		
		if (nbt.contains("group")) {
			zone.setGroup(ZoneGroupManager.getOrCreate(NbtUtil.getIdentifier(nbt, "group")));
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
		
		json.addProperty("id", zone.getWorld().getValue().toString());
		json.addProperty("id", zone.getId().toString());
		
		json.add("minPos", Position.toJson(zone.getMinPos()));
		json.add("maxPos", Position.toJson(zone.getMaxPos()));
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
		
		var zone = new Zone(
				RegistryKey.of(Registry.WORLD_KEY, new Identifier(object.get("world").getAsString())),
				new Identifier(object.get("id").getAsString()),
				Position.fromJson(object.get("minPos")),
				Position.fromJson(object.get("maxPos"))
		);
		
		zone.setColor(Color.fromJson(object.get("color")));
		
		return zone;
	}
	
	public Zone(RegistryKey<World> world, Identifier id, Position startPos, Position endPos) {
		this.world = world;
		
		this.id = id;
		
		this.minPos = Position.min(startPos, endPos);
		this.maxPos = Position.max(startPos, endPos);
		
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
	public Position getMinPos() {
		return minPos;
	}
	
	/**
	 * Sets this zone's minimum position.
	 * @param minPos The new minimum position.
	 */
	public void setMinPos(Position minPos) {
		this.minPos = Position.min(maxPos, minPos);
		this.maxPos = Position.max(maxPos, minPos);
	}
	
	/**
	 * Returns this zone's maximum position.
	 * @return This zone's maximum position.
	 */
	public Position getMaxPos() {
		return maxPos;
	}
	
	/**
	 * Sets this zone's maximum position.
	 * @param maxPos The new maximum position.
	 */
	public void setMaxPos(Position maxPos) {
		this.minPos = Position.min(maxPos, minPos);
		this.maxPos = Position.max(maxPos, minPos);
	}
	
	/**
	 * Returns this zone's center position.
	 * @return This zone's center position.
	 */
	public Position getCenterPos() {
		return new Position(
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
	public Position getLerpedMinPos(float tickDelta) {
		lerpedMinPos = new Position(
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
	public Position getLerpedMaxPos(float tickDelta) {
		lerpedMaxPos = new Position(
				MathHelper.lerp(tickDelta, lerpedMaxPos.getX(), maxPos.getX()),
				MathHelper.lerp(tickDelta, lerpedMaxPos.getY(), maxPos.getY()),
				MathHelper.lerp(tickDelta, lerpedMaxPos.getZ(), maxPos.getZ())
		);
		
		return lerpedMaxPos;
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
	}
	
	/**
	 * Returns whether or not the zone is removed.
	 * @return Whether or not the zone is removed.
	 */
	public boolean isRemoved() {
		return removed;
	}
	
	/**
	 * Sets whether the zone is locked.
	 * @param locked Whether the zone is locked.
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	/**
	 * Returns whether or not this zone is locked.
	 * @return Whether or not this zone is locked.
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
		
		return Objects.equals(minPos, zone.minPos) && Objects.equals(maxPos, zone.maxPos);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(minPos, maxPos);
	}
}
