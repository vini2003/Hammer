package dev.vini2003.hammer.core.api.common.data;

import net.minecraft.entity.data.TrackedData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class TrackedDataSerializerRegistry {
	private static final Map<Class<?>, TrackedDataSerializer<?>> SERIALIZERS = new HashMap<>();
	
	public static <T> void register(Class<T> clazz, TrackedDataSerializer<T> serializer) {
		SERIALIZERS.put(clazz, serializer);
	}
	
	/**
	 * This method is used to get a serializer for a given class.
	 * Due to limitations in generics, and my skill, we are
	 * not specifying the generic's type in this function
	 * such that its return value's functions maybe called
	 * with any object, pending a runtime error if the developer
	 * makes a mistake.
	 *
	 * @param clazz the class to get a serializer for.
	 * @return the serializer.
	 */
	public static TrackedDataSerializer of(Class<?> clazz) {
		return SERIALIZERS.get(clazz);
	}
	
	private static <T> TrackedDataSerializer<T> make(
			TriConsumer<String, T, NbtCompound> writeToNbt,
			BiFunction<String, NbtCompound, T> readFromNbt,
			BiConsumer<T, PacketByteBuf> writeToBuf,
			Function<PacketByteBuf, T> readFromBuf
	) {
		return new TrackedDataSerializer<>() {
			@Override
			public void writeToNbt(String id, T object, NbtCompound nbt) {
				writeToNbt.accept(id, object, nbt);
			}
			
			@Override
			public T readFromNbt(String id, NbtCompound nbt) {
				return readFromNbt.apply(id, nbt);
			}
			
			@Override
			public void writeToBuf(T object, PacketByteBuf buf) {
				writeToBuf.accept(object, buf);
			}
			
			@Override
			public T readFromBuf(PacketByteBuf buf) {
				return readFromBuf.apply(buf);
			}
		};
	}
	
	static {
		register(Boolean.class, make(
				(id, object, nbt) -> nbt.putBoolean(id, object),
				(id, nbt) -> nbt.getBoolean(id),
				(object, buf) -> buf.writeBoolean(object),
				PacketByteBuf::readBoolean
		));
		
		register(Byte.class, make(
				(id, object, nbt) -> nbt.putByte(id, object),
				(id, nbt) -> nbt.getByte(id),
				(object, buf) -> buf.writeByte(object),
				PacketByteBuf::readByte
		));
		
		register(Short.class, make(
				(id, object, nbt) -> nbt.putShort(id, object),
				(id, nbt) -> nbt.getShort(id),
				(object, buf) -> buf.writeShort(object),
				PacketByteBuf::readShort
		));
		
		register(Integer.class, make(
				(id, object, nbt) -> nbt.putInt(id, object),
				(id, nbt) -> nbt.getInt(id),
				(object, buf) -> buf.writeInt(object),
				PacketByteBuf::readInt
		));
		
		register(Long.class, make(
				(id, object, nbt) -> nbt.putLong(id, object),
				(id, nbt) -> nbt.getLong(id),
				(object, buf) -> buf.writeLong(object),
				PacketByteBuf::readLong
		));
		
		register(Float.class, make(
				(id, object, nbt) -> nbt.putFloat(id, object),
				(id, nbt) -> nbt.getFloat(id),
				(object, buf) -> buf.writeFloat(object),
				PacketByteBuf::readFloat
		));
		
		register(Double.class, make(
				(id, object, nbt) -> nbt.putDouble(id, object),
				(id, nbt) -> nbt.getDouble(id),
				(object, buf) -> buf.writeDouble(object),
				PacketByteBuf::readDouble
		));
		
		register(String.class, make(
				(id, object, nbt) -> nbt.putString(id, object),
				(id, nbt) -> nbt.getString(id),
				(object, buf) -> buf.writeString(object),
				PacketByteBuf::readString
		));
	}
}
