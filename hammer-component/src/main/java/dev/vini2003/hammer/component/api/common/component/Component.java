package dev.vini2003.hammer.component.api.common.component;

import dev.vini2003.hammer.component.impl.common.component.ComponentHolder;
import net.minecraft.nbt.NbtCompound;

public interface Component {
	void writeToNbt(NbtCompound nbt);
	
	void readFromNbt(NbtCompound nbt);
}
