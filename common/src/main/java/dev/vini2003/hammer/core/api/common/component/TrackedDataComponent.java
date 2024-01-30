package dev.vini2003.hammer.core.api.common.component;

import dev.vini2003.hammer.core.api.common.component.base.Component;
import dev.vini2003.hammer.core.api.common.data.TrackedDataHandler;
import dev.vini2003.hammer.core.api.common.data.TrackedDataSerializerRegistry;
import dev.vini2003.hammer.core.registry.common.HCComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

import java.util.*;

/**
 * A {@link TrackedDataComponent} is a {@link Component} whose purpose
 * is to serve as a storage medium for {@link TrackedDataHandler}s.
 */
public class TrackedDataComponent implements Component {
	record Key(String id, Class<?> _class) {}
	
	private final Map<Key, Object> entries = new HashMap<>();
	private final Map<String, TrackedDataHandler<?>> handlers = new LinkedHashMap<>();
	
	private final Entity entity;
	
	public TrackedDataComponent(PlayerEntity entity) {
		this.entity = entity;
	}
	
	public <T> TrackedDataHandler<T> register(Class<T> _class, T defaultValue, String id) {
		var handler = new TrackedDataHandler<>(() -> this, _class, defaultValue, id);
		
		handlers.put(id, handler);
		
		return handler;
	}
	
	public void set(String id, Class<?> _class, Object object) {
		entries.put(new Key(id, _class), object);
		
		sync(entity);
	}
	
	public <T> T get(String id, Class<T> _class) {
		return (T) entries.get(new Key(id, _class));
	}
	
	@Override
	public void writeToNbt(NbtCompound nbt) {
		for (var entry : entries.entrySet()) {
			var key = entry.getKey();
			var value = entry.getValue();
			
			var id = key.id();
			var _class = key._class();
			
			TrackedDataSerializerRegistry.of(_class).writeToNbt(id, value, nbt);
		}
	}
	
	@Override
	public void readFromNbt(NbtCompound nbt) {
		for (var id : nbt.getKeys()) {
			var _class = handlers.get(id)._getClass();
			
			var value = TrackedDataSerializerRegistry.of(_class).readFromNbt(id, nbt);
			set(id, _class, value);
		}
	}
	
	@Override
	public void writeToBuf(PacketByteBuf buf) {
		for (var entry : entries.entrySet()) {
			var key = entry.getKey();
			var value = entry.getValue();
			
			var _class = key._class();
			
			TrackedDataSerializerRegistry.of(_class).writeToBuf(value, buf);
		}
	}
	
	@Override
	public void readFromBuf(PacketByteBuf buf) {
		for (var entry : handlers.entrySet()) {
			var id = entry.getKey();
			var handler = entry.getValue();
			
			var _class = handler._getClass();
			
			var value = TrackedDataSerializerRegistry.of(_class).readFromBuf(buf);
			set(id, _class, value);
		}
	}
	
	public static TrackedDataComponent get(Object obj) {
		return HCComponents.TRACKED_DATA.get(obj);
	}
	
	public static void sync(Object obj) {
		HCComponents.TRACKED_DATA.sync(obj);
	}
}
