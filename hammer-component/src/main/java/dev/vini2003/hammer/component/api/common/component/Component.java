package dev.vini2003.hammer.component.api.common.component;

import net.minecraft.nbt.NbtCompound;

public interface Component {
	void writeToNbt(NbtCompound nbt);
	
	void readFromNbt(NbtCompound nbt);
}
