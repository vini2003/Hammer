package dev.vini2003.hammer.component.impl.common.util;

import dev.vini2003.hammer.component.api.common.component.Component;
import dev.vini2003.hammer.component.api.common.component.ComponentKey;
import dev.vini2003.hammer.component.impl.common.component.ComponentHolder;
import dev.vini2003.hammer.component.impl.common.state.ComponentPersistentState;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ComponentUtil {
	public static final Map<ComponentKey<? extends Component>, Function<Entity, ? extends Component>> ENTITY_ATTACHERS = new HashMap<>();
	public static final Map<ComponentKey<? extends Component>, Function<World, ? extends Component>> WORLD_ATTACHERS = new HashMap<>();
	
	public static void attachToEntity(Entity entity) {
		if (entity instanceof ComponentHolder holder) {
			var container = holder.getComponentContainer();
			
			for (var entry : ENTITY_ATTACHERS.entrySet()) {
				var key = entry.getKey();
				var func = entry.getValue();
				
				var comp = func.apply(entity);
				
				if (comp != null) {
					container.put(key, comp);
				}
			}
		}
	}
	
	public static void attachToWorld(World world) {
		if (world instanceof ComponentHolder holder) {
			var container = holder.getComponentContainer();
			
			for (var entry : WORLD_ATTACHERS.entrySet()) {
				var key = entry.getKey();
				var func = entry.getValue();
				
				var comp = func.apply(world);
				
				if (comp != null) {
					container.put(key, comp);
				}
			}
		}
	}
	
	public static void attachToPersistentState(ComponentPersistentState state) {
		var container = state.getComponentContainer();
		
		for (var entry : WORLD_ATTACHERS.entrySet()) {
			var key = entry.getKey();
			var func = entry.getValue();
			
			var comp = func.apply(state.getWorld());
			
			if (comp != null) {
				container.put(key, comp);
			}
		}
	}
}
