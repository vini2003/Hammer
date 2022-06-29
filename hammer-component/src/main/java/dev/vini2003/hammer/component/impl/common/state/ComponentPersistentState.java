package dev.vini2003.hammer.component.impl.common.state;

import dev.vini2003.hammer.component.impl.common.util.ComponentUtil;
import dev.vini2003.hammer.component.impl.common.component.ComponentContainer;
import dev.vini2003.hammer.component.impl.common.component.ComponentHolder;
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
