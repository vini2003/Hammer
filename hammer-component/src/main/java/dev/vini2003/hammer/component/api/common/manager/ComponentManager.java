package dev.vini2003.hammer.component.api.common.manager;

import dev.vini2003.hammer.component.api.common.component.Component;
import dev.vini2003.hammer.component.api.common.component.key.ComponentKey;
import dev.vini2003.hammer.component.impl.common.util.ComponentUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ComponentManager {
	private static final Map<Identifier, ComponentKey<?>> KEYS = new HashMap<>();
	
	public static <C extends Component> ComponentKey<C> registerKey(Identifier id) {
		var key = new ComponentKey<C>(id);
		KEYS.put(id, key);
		return key;
	}
	
	@Nullable
	public static <C extends Component> ComponentKey<C> getKey(Identifier id) {
		return (ComponentKey<C>) KEYS.get(id);
	}
	
	public static <C extends Component> void registerForEntity(ComponentKey<C> key, Function<Entity, C> function) {
		ComponentUtil.ENTITY_ATTACHERS.put(key, function);
	}
	
	public static <C extends Component> void registerForWorld(ComponentKey<C> key, Function<World, C> function) {
		ComponentUtil.WORLD_ATTACHERS.put(key, function);
	}
}
