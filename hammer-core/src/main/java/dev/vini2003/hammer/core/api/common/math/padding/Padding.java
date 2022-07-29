package dev.vini2003.hammer.core.api.common.math.padding;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

/**
 * <p>A {@link Padding} represents a two-dimensional padding.
 *
 * <p>The following serialization methods are provided:</p>
 * <ul>
 *     <li>{@link #toJson(Padding)} - from {@link Padding} to {@link JsonElement}.</li>
 *     <li>{@link #toNbt(Padding)} - from {@link Padding} to {@link NbtCompound}.</li>
 *     <li>{@link #toBuf(Padding, PacketByteBuf)} - from {@link Padding} to {@link PacketByteBuf}.</li>
 * </ul>
 
 * <ul>
 *     <li>{@link #fromJson(JsonElement)} - from {@link JsonElement} to {@link Padding}.</li>
 *     <li>{@link #fromNbt(NbtCompound)} - from {@link NbtCompound} to {@link Padding}.</li>
 *     <li>{@link #fromBuf(PacketByteBuf)} - from {@link PacketByteBuf} to {@link Padding}.</li>
 * </ul>
 */
public class Padding {
	public static final Codec<Padding> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.FLOAT.optionalFieldOf("left", 0.0F).forGetter(Padding::getLeft),
			Codec.FLOAT.optionalFieldOf("right", 0.0F).forGetter(Padding::getRight),
			Codec.FLOAT.optionalFieldOf("top", 0.0F).forGetter(Padding::getTop),
			Codec.FLOAT.optionalFieldOf("bottom", 0.0F).forGetter(Padding::getBottom)
	).apply(instance, Padding::new));
	
	private final float left;
	private final float right;
	private final float top;
	private final float bottom;
	
	
	/**
	 * Serializes a padding to a {@link PacketByteBuf}.
	 * @param padding The padding to serialize.
	 * @param buf The buffer to serialize to.
	 * @return The buffer.
	 */
	public static PacketByteBuf toBuf(Padding padding, PacketByteBuf buf) {
		buf.writeFloat(padding.getLeft());
		buf.writeFloat(padding.getRight());
		buf.writeFloat(padding.getTop());
		buf.writeFloat(padding.getBottom());
		
		return buf;
	}
	
	/**
	 * Deserializes a padding from a {@link PacketByteBuf}.
	 * @param buf The buffer to deserialize from.
	 * @return The padding.
	 */
	public static Padding fromBuf(PacketByteBuf buf) {
		return new Padding(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());
	}
	
	/**
	 * Serializes a padding to an {@link NbtCompound}.
	 * @param padding The padding.
	 * @return The serialized padding.
	 */
	public static NbtCompound toNbt(Padding padding) {
		var nbt = new NbtCompound();
		
		nbt.putFloat("left", padding.getLeft());
		nbt.putFloat("right", padding.getRight());
		nbt.putFloat("top", padding.getTop());
		nbt.putFloat("bottom", padding.getBottom());
		
		return nbt;
	}
	
	/**
	 * Deserializes a padding from an {@link NbtCompound}.
	 * @param nbt The serialized padding.
	 * @return The padding.
	 */
	public static Padding fromNbt(NbtCompound nbt) {
		return new Padding(nbt.getFloat("left"), nbt.getFloat("right"), nbt.getFloat("top"), nbt.getFloat("bottom"));
	}
	
	/**
	 * Serializes a padding to a {@link JsonElement}.
	 * @param padding The padding.
	 * @return The serialized padding.
	 */
	public static JsonElement toJson(Padding padding) {
		var json = new JsonObject();
		
		json.addProperty("left", padding.getLeft());
		json.addProperty("right", padding.getRight());
		json.addProperty("top", padding.getTop());
		json.addProperty("bottom", padding.getBottom());
		
		return json;
	}
	
	/**
	 * Deserializes a padding from a {@link JsonElement}.
	 * @param json The serialized padding.
	 * @return The padding.
	 */
	public static Padding fromJson(JsonElement json) {
		var object = json.getAsJsonObject();
		
		return new Padding(object.get("left").getAsFloat(), object.get("right").getAsFloat(), object.get("top").getAsFloat(), object.get("bottom").getAsFloat());
	}
	
	
	/**
	 * Constructs a padding.
	 * @param left the left padding component.
	 * @param right the right padding component.
	 * @param top the top padding component.
	 * @param bottom the bottom padding component.
	 * @return the padding.
	 */
	public Padding(float left, float right, float top, float bottom) {
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
	}
	
	/**
	 * Returns this padding's left component.
	 * @return this padding's left component.
	 */
	public float getLeft() {
		return left;
	}
	
	/**
	 * Returns this padding's right component.
	 * @return this padding's right component.
	 */
	public float getRight() {
		return right;
	}
	
	/**
	 * Returns this padding's top component.
	 * @return this padding's top component.
	 */
	public float getTop() {
		return top;
	}
	
	/**
	 * Returns this padding's bottom component.
	 * @return this padding's bottom component.
	 */
	public float getBottom() {
		return bottom;
	}
}
