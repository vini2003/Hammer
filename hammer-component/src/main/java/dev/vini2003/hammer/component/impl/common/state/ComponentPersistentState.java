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

package dev.vini2003.hammer.component.impl.common.state;

import dev.vini2003.hammer.component.impl.common.util.ComponentUtil;
import dev.vini2003.hammer.component.impl.common.component.container.ComponentContainer;
import dev.vini2003.hammer.component.impl.common.component.holder.ComponentHolder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

public class ComponentPersistentState extends PersistentState implements ComponentHolder {
	private World world;
	
	private ComponentContainer componentContainer = new ComponentContainer();
	
	public ComponentPersistentState(World world) {
		this.world = world;
		
		ComponentUtil.attachToPersistentState(this);
	}
	
	public ComponentPersistentState(World world, NbtCompound nbt) {
		this(world);
		
		componentContainer.readFromNbt(nbt);
	}
	
	public World getWorld() {
		return world;
	}
	
	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		var containerNbt = new NbtCompound();
		componentContainer.writeToNbt(containerNbt);
		return containerNbt;
	}
	
	@Override
	public ComponentContainer getComponentContainer() {
		return componentContainer;
	}
}
