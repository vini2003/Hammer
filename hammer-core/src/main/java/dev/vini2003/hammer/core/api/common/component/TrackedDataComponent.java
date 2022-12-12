package dev.vini2003.hammer.core.api.common.component;

import dev.vini2003.hammer.core.api.common.component.base.Component;
import dev.vini2003.hammer.core.api.common.data.TrackedDataHandler;
import dev.vini2003.hammer.core.registry.common.HCComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

import java.util.HashMap;
import java.util.Map;

/**
 * A {@link TrackedDataComponent} is a {@link Component} whose purpose
 * is to serve as a storage medium for {@link TrackedDataHandler}s.
 */
public class TrackedDataComponent implements Component {
	private final Map<String, NbtElement> entries = new HashMap<>();
	
	private final PlayerEntity player;
	
	private String dirtyKey = null;
	
	public static TrackedDataComponent get(Object obj) {
		return HCComponents.TRACKED_DATA.get(obj);
	}
	
	public static void sync(Object obj) {
		HCComponents.TRACKED_DATA.sync(obj);
	}
	
	public TrackedDataComponent(PlayerEntity player) {
		this.player = player;
	}
	
	public void set(String key, NbtElement element) {
		entries.put(key, element);
		
		dirtyKey = key;
		TrackedDataComponent.sync(player);
		dirtyKey = null;
	}
	
	public <T> T get(String key) {
		return (T) entries.get(key);
	}
	
	@Override
	public void writeToNbt(NbtCompound nbt) {
		for (var entry : entries.entrySet()) {
			var key = entry.getKey();
			var value = entry.getValue();
			
			nbt.put(key, value);
		}
	}
	
	@Override
	public void readFromNbt(NbtCompound nbt) {
		for (var key : nbt.getKeys()) {
			var value = nbt.get(key);
			
			entries.put(key, value);
		}
	}
	
	@Override
	public void writeToNbtForSync(NbtCompound nbt) {
		nbt.put(dirtyKey, get(dirtyKey));
	}
}
