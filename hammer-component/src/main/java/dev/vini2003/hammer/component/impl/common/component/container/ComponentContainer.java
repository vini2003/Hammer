package dev.vini2003.hammer.component.impl.common.component.container;

import dev.vini2003.hammer.component.api.common.component.Component;
import dev.vini2003.hammer.component.api.common.component.key.ComponentKey;
import dev.vini2003.hammer.component.api.common.manager.ComponentManager;
import dev.vini2003.hammer.core.api.common.util.NbtUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ComponentContainer {
	private static final String HAMMER$ID_KEY = "Hammer$Id";
	private static final String HAMMER$COMPONENT_KEY = "Hammer$Component";
	private static final String HAMMER$COMPONENTS_KEY = "Hammer$Components";
	
	private Map<ComponentKey<?>, Component> components = new HashMap<>();
	
	public void put(ComponentKey<?> key, Component component) {
		components.put(key, component);
	}
	
	public void remove(ComponentKey<?> key) {
		components.remove(key);
	}
	
	public <C extends Component> C get(ComponentKey<C> key) {
		return (C) components.get(key);
	}
	
	public void writeToNbt(NbtCompound nbt) {
		var entryNbtList = new NbtList();
		
		for (var entry : entrySet()) {
			var key = entry.getKey();
			var component = entry.getValue();
			
			var entryNbt = new NbtCompound();
			
			NbtUtil.putIdentifier(entryNbt, HAMMER$ID_KEY, key.getId());
			
			var componentNbt = new NbtCompound();
			component.writeToNbt(componentNbt);
			
			entryNbt.put(HAMMER$COMPONENT_KEY, componentNbt);
			
			entryNbtList.add(entryNbt);
		}
		
		nbt.put(HAMMER$COMPONENTS_KEY, entryNbtList);
	}
	
	public void readFromNbt(NbtCompound nbt) {
		var entryNbtList = nbt.getList(HAMMER$COMPONENTS_KEY, NbtElement.COMPOUND_TYPE);
		
		for (var entryElement : entryNbtList) {
			var entryNbt = (NbtCompound) entryElement;
			
			var componentId = NbtUtil.getIdentifier(entryNbt, HAMMER$ID_KEY);
			var componentKey = ComponentManager.getKey(componentId);
			
			var component = get(componentKey);
			
			var componentNbt = entryNbt.getCompound(HAMMER$COMPONENT_KEY);
			component.readFromNbt(componentNbt);
		}
	}
	
	public Collection<Map.Entry<ComponentKey<?>, Component>> entrySet() {
		return components.entrySet();
	}
}
