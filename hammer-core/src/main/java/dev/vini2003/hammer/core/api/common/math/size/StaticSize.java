package dev.vini2003.hammer.core.api.common.math.size;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

/**
 * <p>A {@link Size} represents a fixed three-dimensional size.
 *
 * <p>The following serialization methods are provided:</p>
 * <ul>
 *     <li>{@link #toJson(StaticSize)} - from {@link StaticSize} to {@link JsonElement}.</li>
 *     <li>{@link #toNbt(StaticSize)} - from {@link StaticSize} to {@link NbtCompound}.</li>
 *     <li>{@link #toBuf(StaticSize, PacketByteBuf)} - from {@link StaticSize} to {@link PacketByteBuf}.</li>
 * </ul>
 
 * <ul>
 *     <li>{@link #fromJson(JsonElement)} - from {@link JsonElement} to {@link StaticSize}.</li>
 *     <li>{@link #fromNbt(NbtCompound)} - from {@link NbtCompound} to {@link StaticSize}.</li>
 *     <li>{@link #fromBuf(PacketByteBuf)} - from {@link PacketByteBuf} to {@link StaticSize}.</li>
 * </ul>
 */
public class StaticSize extends Size {
	public static final Codec<StaticSize> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.FLOAT.optionalFieldOf("width", 0.0F).forGetter(StaticSize::getWidth),
			Codec.FLOAT.optionalFieldOf("height", 0.0F).forGetter(StaticSize::getHeight),
			Codec.FLOAT.optionalFieldOf("length", 0.0F).forGetter(StaticSize::getLength)
	).apply(instance, StaticSize::new));
	
	private final float width;
	private final float height;
	private final float length;
	
	/**
	 * Serializes a size to a {@link PacketByteBuf}.
	 * @param size The size to serialize.
	 * @param buf The buffer to serialize to.
	 * @return The buffer.
	 */
	public static PacketByteBuf toBuf(StaticSize size, PacketByteBuf buf) {
		buf.writeFloat(size.getWidth());
		buf.writeFloat(size.getHeight());
		buf.writeFloat(size.getLength());
		
		return buf;
	}
	
	/**
	 * Deserializes a size from a {@link PacketByteBuf}.
	 * @param buf The buffer to deserialize from.
	 * @return The size.
	 */
	public static StaticSize fromBuf(PacketByteBuf buf) {
		return new StaticSize(buf.readFloat(), buf.readFloat(), buf.readFloat());
	}
	
	/**
	 * Serializes a size to an {@link NbtCompound}.
	 * @param size The size.
	 * @return The serialized size.
	 */
	public static NbtCompound toNbt(StaticSize size) {
		var nbt = new NbtCompound();
		
		nbt.putFloat("width", size.getWidth());
		nbt.putFloat("height", size.getHeight());
		nbt.putFloat("length", size.getLength());
		
		return nbt;
	}
	
	/**
	 * Deserializes a size from an {@link NbtCompound}.
	 * @param nbt The serialized size.
	 * @return The size.
	 */
	public static StaticSize fromNbt(NbtCompound nbt) {
		return new StaticSize(nbt.getFloat("width"), nbt.getFloat("height"), nbt.getFloat("length"));
	}
	
	/**
	 * Serializes a size to a {@link JsonElement}.
	 * @param size The size.
	 * @return The serialized size.
	 */
	public static JsonElement toJson(StaticSize size) {
		var json = new JsonObject();
		
		json.addProperty("width", size.getWidth());
		json.addProperty("height", size.getHeight());
		json.addProperty("length", size.getLength());
		
		return json;
	}
	
	
	/**
	 * Deserializes a size from a {@link JsonElement}.
	 * @param json The serialized size.
	 * @return The size.
	 */
	public static StaticSize fromJson(JsonElement json) {
		var object = json.getAsJsonObject();
		
		return new StaticSize(
				object.get("width").getAsFloat(),
				object.get("height").getAsFloat(),
				object.get("length").getAsFloat()
		);
	}
	
	/**
	 * Constructs a static size.
	 *
	 * @param width  the width size component.
	 * @param height the height size component.
	 * @param length the length size component.
	 */
	public StaticSize(float width, float height, float length) {
		this.width = width;
		this.height = height;
		this.length = length;
	}
	
	/**
	 * Constructs a size.
	 *
	 * @param width  the width size component
	 * @param height the height size component.
	 */
	public StaticSize(float width, float height) {
		this.width = width;
		this.height = height;
		this.length = 0.0F;
	}
	
	/**
	 * Constructs a size.
	 *
	 * @param anchor the anchor,
	 * @param relativeSize the relative size.
	 */
	public StaticSize(SizeHolder anchor, SizeHolder relativeSize) {
		this(anchor.getWidth() + relativeSize.getWidth(), anchor.getHeight() + relativeSize.getHeight(), anchor.getLength() + relativeSize.getLength());
	}
	
	/**
	 * Constructs an anchored static size.
	 *
	 * @param anchor the anchor.
	 * @param width  the relative with size component.
	 * @param height the relative height size component.
	 * @param length the relative length size component.
	 */
	public StaticSize(SizeHolder anchor, float width, float height, float length) {
		this(anchor.getWidth() + width, anchor.getHeight() + height, anchor.getLength() + length);
	}
	
	/**
	 * Constructs an anchored static size.
	 *
	 * @param anchor the anchor.
	 * @param width  the relative with size component.
	 * @param height the relative height size component.
	 */
	public StaticSize(SizeHolder anchor, float width, float height) {
		this(anchor.getWidth() + width, anchor.getHeight() + height, anchor.getLength());
	}
	
	/**
	 * Constructs an anchor's size.
	 *
	 * @param anchor the anchor.
	 */
	public StaticSize(SizeHolder anchor) {
		this(anchor.getWidth(), anchor.getHeight(), anchor.getLength());
	}
	
	
	/**
	 * Returns this size, adding another size to it.
	 *
	 * @param size the size.
	 *
	 * @return the resulting size.
	 */
	public StaticSize plus(SizeHolder size) {
		return new StaticSize(width + size.getWidth(), height + size.getHeight(), length + size.getLength());
	}
	
	/**
	 * Returns this size, subtracting another size from it.
	 *
	 * @param size the size.
	 *
	 * @return the resulting size.
	 */
	public StaticSize minus(SizeHolder size) {
		return new StaticSize(width + size.getWidth(), height + size.getHeight(), length + size.getLength());
	}
	
	/**
	 * Returns this size, multiplying its components by a given number.
	 *
	 * @param number the number.
	 *
	 * @return the resulting size.
	 */
	public StaticSize times(float number) {
		return new StaticSize(width * number, height * number, length * number);
	}
	
	/**
	 * Returns this size, dividing its components by a given number.
	 *
	 * @param number the number.
	 *
	 * @return the resulting size.
	 */
	public StaticSize div(float number) {
		return new StaticSize(width / number, height / number, length / number);
	}
	
	@Override
	public float getWidth() {
		return width;
	}
	
	@Override
	public float getHeight() {
		return height;
	}
	
	@Override
	public float getLength() {
		return length;
	}
}
