package dev.vini2003.hammer.core.api.common.math.position;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.*;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;

import java.util.function.Function;

/**
 * <p>A {@link StaticPosition} represents a fixed three-dimensional position.
 *
 * <p>The following serialization methods are provided:</p>
 * <ul>
 *     <li>{@link #toJson(StaticPosition)} - from {@link StaticPosition} to {@link JsonElement}.</li>
 *     <li>{@link #toNbt(StaticPosition)} - from {@link StaticPosition} to {@link NbtCompound}.</li>
 *     <li>{@link #toBuf(StaticPosition, PacketByteBuf)} - from {@link StaticPosition} to {@link PacketByteBuf}.</li>
 * </ul>
 
 * <ul>
 *     <li>{@link #fromJson(JsonElement)} - from {@link JsonElement} to {@link StaticPosition}.</li>
 *     <li>{@link #fromNbt(NbtCompound)} - from {@link NbtCompound} to {@link StaticPosition}.</li>
 *     <li>{@link #fromBuf(PacketByteBuf)} - from {@link PacketByteBuf} to {@link StaticPosition}.</li>
 * </ul>
 */
public class StaticPosition extends Position {
	public static final Codec<StaticPosition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.FLOAT.optionalFieldOf("x", 0.0F).forGetter(StaticPosition::getX),
			Codec.FLOAT.optionalFieldOf("y", 0.0F).forGetter(StaticPosition::getY),
			Codec.FLOAT.optionalFieldOf("z", 0.0F).forGetter(StaticPosition::getZ)
	).apply(instance, StaticPosition::new));
	
	private final float x;
	private final float y;
	private final float z;
	
	
	/**
	 * Constructs a static position.
	 *
	 * @param x the X static position component.
	 * @param y the Y static position component.
	 * @param z the Z static position component.
	 */
	public StaticPosition(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Constructs a static position.
	 *
	 * @param x the X static position component.
	 * @param y the Y static position component.
	 */
	public StaticPosition(float x, float y) {
		this.x = x;
		this.y = y;
		this.z = 0.0F;
	}
	
	/**
	 * Constructs an anchored static position.
	 *
	 * @param anchor           the anchor.
	 * @param relativePosition the relative position.
	 */
	public StaticPosition(PositionHolder anchor, PositionHolder relativePosition) {
		this(anchor.getX() + relativePosition.getX(), anchor.getY() + relativePosition.getY(), anchor.getZ() + relativePosition.getZ());
	}
	
	/**
	 * Constructs an anchored static position.
	 *
	 * @param anchor    the anchor.
	 * @param relativeX the relative X component.
	 * @param relativeY the relative Y component.
	 * @param relativeZ the relative Z component.
	 */
	public StaticPosition(PositionHolder anchor, float relativeX, float relativeY, float relativeZ) {
		this(anchor.getX() + relativeX, anchor.getY() + relativeY, anchor.getZ() + relativeZ);
	}
	
	/**
	 * Constructs an anchored static position.
	 *
	 * @param anchor    the anchor.
	 * @param relativeX the relative X component.
	 * @param relativeY the relative Y component.
	 */
	public StaticPosition(PositionHolder anchor, float relativeX, float relativeY) {
		this(anchor.getX() + relativeX, anchor.getY() + relativeY, anchor.getZ());
	}
	
	/**
	 * Constructs an anchor's static position.
	 *
	 * @param anchor the anchor.
	 */
	public StaticPosition(PositionHolder anchor) {
		this(anchor.getX(), anchor.getY(), anchor.getZ());
	}
	
	/**
	 * Converts a {@link BlockPos} to a static position.
	 *
	 * @param blockPos the block pos.
	 */
	public StaticPosition(BlockPos blockPos) {
		this(blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}
	
	/**
	 * Converts a {@link ChunkPos} to a static position.
	 *
	 * @param chunkPos the chunk pos.
	 *
	 * @return the position.
	 */
	public StaticPosition(ChunkPos chunkPos) {
		this(chunkPos.x, 0.0F, chunkPos.z);
	}
	
	/**
	 * Converts a {@link Vec2f} to a static position.
	 * @param vec2f The vector.
	 * @return The position.
	 */
	public StaticPosition(Vec2f vec2f) {
		this(vec2f.x, vec2f.y);
	}
	
	/**
	 * Converts a {@link Vec3d} to a static position.
	 * @param vec3d The vector.
	 * @return The position.
	 */
	public StaticPosition(Vec3d vec3d) {
		this((float) vec3d.x, (float) vec3d.y, (float) vec3d.z);
	}
	
	/**
	 * Converts a {@link Vector3f} to a static position.
	 * @param vec3f The vector.
	 * @return The position.
	 */
	public StaticPosition(Vector3f vec3f) {
		this(vec3f.x(), vec3f.y(), vec3f.z());
	}
	
	/**
	 * Converts a {@link Vec3i} to a static position.
	 * @param vec3i The vector.
	 * @return The position.
	 */
	public StaticPosition(Vec3i vec3i) {
		this(vec3i.getX(), vec3i.getY(), vec3i.getZ());
	}
	
	/**
	 * Converts a {@link Vector3d} to a static position.
	 * @param vector3d The vector.
	 * @return The position.
	 */
	public StaticPosition(Vector3d vector3d) {
		this((float) vector3d.x(), (float) vector3d.y(), (float) vector3d.z());
	}
	
	/**
	 * Serializes a static position to a {@link PacketByteBuf}.
	 * @param position The position to serialize.
	 * @param buf The buffer to serialize to.
	 * @return The buffer.
	 */
	public static PacketByteBuf toBuf(StaticPosition position, PacketByteBuf buf) {
		buf.writeFloat(position.getX());
		buf.writeFloat(position.getY());
		buf.writeFloat(position.getZ());
		
		return buf;
	}
	
	/**
	 * Deserializes a static position from a {@link PacketByteBuf}.
	 * @param buf The buffer to deserialize from.
	 * @return The position.
	 */
	public static StaticPosition fromBuf(PacketByteBuf buf) {
		return new StaticPosition(buf.readFloat(), buf.readFloat(), buf.readFloat());
	}
	
	/**
	 * Serializes a static position to an {@link NbtCompound}.
	 * @param position The position.
	 * @return The serialized position.
	 */
	public static NbtCompound toNbt(StaticPosition position) {
		var nbt = new NbtCompound();
		
		nbt.putFloat("x", position.getX());
		nbt.putFloat("y", position.getY());
		nbt.putFloat("z", position.getZ());
		
		return nbt;
	}
	
	/**
	 * Deserializes a static position from an {@link NbtCompound}.
	 * @param nbt The serialized position.
	 * @return The position.
	 */
	public static StaticPosition fromNbt(NbtCompound nbt) {
		return new StaticPosition(nbt.getFloat("x"), nbt.getFloat("y"), nbt.getFloat("z"));
	}
	
	/**
	 * Serializes a static position to a {@link JsonElement}.
	 * @param position The position.
	 * @return The serialized position.
	 */
	public static JsonElement toJson(StaticPosition position) {
		var json = new JsonObject();
		
		json.addProperty("x", position.getX());
		json.addProperty("y", position.getY());
		json.addProperty("z", position.getZ());
		
		return json;
	}
	
	/**
	 * Deserializes a static position from a {@link JsonElement}.
	 * @param json The serialized position.
	 * @return The position.
	 */
	public static StaticPosition fromJson(JsonElement json) {
		var object = json.getAsJsonObject();
		
		return new StaticPosition(object.get("x").getAsFloat(), object.get("y").getAsFloat(), object.get("z").getAsFloat());
	}
	
	/**
	 * Returns this static position, adding another position to it.
	 *
	 * @param position the position.
	 *
	 * @return the resulting position.
	 */
	public StaticPosition plus(PositionHolder position) {
		return new StaticPosition(getX() + position.getX(), getY() + position.getY(), getZ() + position.getZ());
	}
	
	/**
	 * Returns this static position, subtracting another position from it.
	 *
	 * @param position the position.
	 *
	 * @return the resulting position.
	 */
	public StaticPosition minus(PositionHolder position) {
		return new StaticPosition(getX() + position.getX(), getY() + position.getY(), getZ() + position.getZ());
	}
	
	/**
	 * Returns this static position, multiplying its components by a given number.
	 *
	 * @param number the number.
	 *
	 * @return the resulting position.
	 */
	public StaticPosition times(float number) {
		return new StaticPosition(getX() * number, getY() * number, getZ() * number);
	}
	
	/**
	 * Returns this static position, dividing its components by a given number.
	 *
	 * @param number the number.
	 *
	 * @return the resulting position.
	 */
	public StaticPosition div(float number) {
		return new StaticPosition(getX() / number, getY() / number, getZ() / number);
	}
	
	/**
	 * Returns this static position, rotated by the given quaternion.
	 *
	 * @return the resulting position.
	 */
	public StaticPosition rotate(Quaternionf quaternion) {
		var q1 = new Quaternionf(quaternion);
		q1.mul(new Quaternionf(this.getX(), this.getY(), this.getZ(), 0.0F));
		var q2 = new Quaternionf(quaternion);
		q2.conjugate();
		q1.mul(q2);
		
		return new StaticPosition(q1.x(), q1.y(), q1.z());
	}
	
	/**
	 * Returns the maximum position by the given function.
	 *
	 * @return the maximum position.
	 */
	public static StaticPosition maxBy(StaticPosition a, StaticPosition b, Function<StaticPosition, Float> function) {
		var resA = function.apply(a);
		var resB = function.apply(b);
		
		if (resA >= resB) {
			return a;
		} else {
			return b;
		}
	}
	
	/**
	 * Returns the minimum position by the given function.
	 *
	 * @return the minimum position.
	 */
	public static StaticPosition minBy(StaticPosition a, StaticPosition b, Function<StaticPosition, Float> function) {
		var resA = function.apply(a);
		var resB = function.apply(b);
		
		if (resA <= resB) {
			return a;
		} else {
			return b;
		}
	}
	
	/**
	 * Returns the maximum position.
	 *
	 * @return the maximum position.
	 */
	public static StaticPosition max(StaticPosition a, StaticPosition b) {
		return new StaticPosition(Math.max(a.getX(), b.getX()), Math.max(a.getY(), b.getY()), Math.max(a.getZ(), b.getZ()));
	}
	
	/**
	 * Returns the minimum position.
	 *
	 * @return the minimum position.
	 */
	public static StaticPosition min(StaticPosition a, StaticPosition b) {
		return new StaticPosition(Math.min(a.getX(), b.getX()), Math.min(a.getY(), b.getY()), Math.min(a.getZ(), b.getZ()));
	}
	
	@Override
	public float getX() {
		return x;
	}
	
	@Override
	public float getY() {
		return y;
	}
	
	@Override
	public float getZ() {
		return z;
	}
}
