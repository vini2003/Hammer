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

package dev.vini2003.hammer.core.api.common.component.base;

import dev.vini2003.hammer.core.api.common.manager.ComponentManager;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.world.World;

/**
 * A {@link Component} is a serializable object that
 * may be attached to {@link Entity}s and {@link World}s
 * using {@link ComponentManager}.
 *
 * <p>The following serialization methods are provided:</p>
 * <ul>
 *     <li>{@link #writeToNbt(NbtCompound)} - from {@link Component} to {@link NbtCompound}.</li>
 * </ul>
 
 * <ul>
 *     <li>{@link #readFromNbt(NbtCompound)} - from {@link NbtCompound} to {@link Component}.</li>
 * </ul>
 *
 * <ul>
 *     <li>{@link #writeToBuf(PacketByteBuf)} ()} - from {@link Component} to {@link PacketByteBuf}.</li>
 * </ul>
 *
 * <ul>
 *     <li>{@link #readFromBuf(PacketByteBuf)} - from {@link PacketByteBuf} to {@link Component}.</li>
 * </ul>
 */
public interface Component {
	/**
	 * Serializes this component to an {@link NbtCompound}.
	 * @param nbt the NBT tag.
	 */
	void writeToNbt(NbtCompound nbt);
	
	/**
	 * Deserializes this component from an {@link NbtCompound}.
	 * @param nbt the NBT tag.
	 */
	void readFromNbt(NbtCompound nbt);
	
	/**
	 * Writes this component to a {@link PacketByteBuf}
	 * for synchronization. This method is called because
	 * often times, you wish to handle serialization to disk
	 * and network synchronization differently, and
	 * {@link NbtCompound} is not as space-efficient.
	 *
	 * Defaults to synchronizing using {@link #writeToNbt(NbtCompound)},
	 * and then writing the NBT to the packet byte buffer.
	 *
	 * @return the packet byte buffer.
	 */
	default void writeToBuf(PacketByteBuf buf) {
		var nbt = new NbtCompound();
		writeToNbt(nbt);
		buf.writeNbt(nbt);
	}
	
	/**
	 * Reads this component from a {@link PacketByteBuf}
	 * for synchronization. This method is called because
	 * often times, you wish to handle serialization to disk
	 * and network synchronization differently, and
	 * {@link NbtCompound} is not as space-efficient.
	 *
	 * Defaults to reading the NBT from the packet byte buffer,
	 * and then deserializing using {@link #readFromNbt(NbtCompound)}.
	 *
	 * @param buf the packet byte buffer.
	 */
	default void readFromBuf(PacketByteBuf buf) {
		var nbt = buf.readNbt();
		readFromNbt(nbt);
	}
}
