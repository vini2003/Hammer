package dev.vini2003.hammer.core.api.common.data;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

public interface TrackedDataSerializer<T> {
	void writeToNbt(String id, T object, NbtCompound nbt);
	T readFromNbt(String id, NbtCompound nbt);
	
	void writeToBuf(T object, PacketByteBuf buf);
	T readFromBuf(PacketByteBuf buf);
}
