package dev.vini2003.hammer.gui.api.common.registry;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ValueRegistry {
	private static final ThreadLocal<Map<Identifier, Object>> VALUES = ThreadLocal.withInitial(ConcurrentHashMap::new);
	
	public static <T> T get(Identifier id) {
		return (T) VALUES.get().get(id);
	}
	
	public static <T> void set(Identifier id, T value) {
		VALUES.get().put(id, value);
	}
}
