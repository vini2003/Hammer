package dev.vini2003.hammer.core.api.common.data;

import dev.vini2003.hammer.core.api.common.component.TrackedDataComponent;
import net.minecraft.nbt.*;

import java.util.function.Supplier;

public class TrackedDataHandler<T> {
	private final Supplier<TrackedDataComponent> component;
	
	private final Class<T> clazz;
	
	private final String key;
	
	private final T defaultValue;
	
	public TrackedDataHandler(Supplier<TrackedDataComponent> component, Class<T> clazz, T defaultValue, String key) {
		this.component = component;
		
		this.clazz = clazz;
		
		this.defaultValue = defaultValue;
		
		this.key = key;
	}
	
	public T get() {
		T t = null;
		
		if (clazz.isAssignableFrom(Boolean.class)) {
			if (component.get().get(key) instanceof NbtByte nbtByte) {
				t = (T) Boolean.valueOf((nbtByte).byteValue() == 1);
			}
		} else if (clazz.isAssignableFrom(Byte.class)) {
			if (component.get().get(key) instanceof NbtByte nbtByte) {
				t = (T) Byte.valueOf((nbtByte).byteValue());
			}
		} else if (clazz.isAssignableFrom(Short.class)) {
			if (component.get().get(key) instanceof NbtShort nbtShort) {
				t = (T) Short.valueOf((nbtShort).shortValue());
			}
		} else if (clazz.isAssignableFrom(Integer.class)) {
			if (component.get().get(key) instanceof NbtInt nbtInt) {
				t = (T) Integer.valueOf((nbtInt).intValue());
			}
		} else if (clazz.isAssignableFrom(Long.class)) {
			if (component.get().get(key) instanceof NbtLong nbtLong) {
				t = (T) Long.valueOf((nbtLong).longValue());
			}
		} else if (clazz.isAssignableFrom(Float.class)) {
			if (component.get().get(key) instanceof NbtFloat nbtFloat) {
				t = (T) Float.valueOf((nbtFloat).floatValue());
			}
		} else if (clazz.isAssignableFrom(Double.class)) {
			if (component.get().get(key) instanceof NbtDouble nbtDouble) {
				t = (T) Double.valueOf((nbtDouble).doubleValue());
			}
		} else if (clazz.isAssignableFrom(String.class)) {
			if (component.get().get(key) instanceof NbtString nbtString) {
				t = (T) nbtString.asString();
			}
		} else if (clazz.isAssignableFrom(NbtElement.class)) {
			if (component.get().get(key)) {
				t = component.get().get(clazz.getName());
			}
		}
		
		if (t == null) {
			t = defaultValue;
		}
		
		return t;
	}
	
	public void set(T t) {
		if (clazz.isAssignableFrom(Boolean.class)) {
			component.get().set(key, NbtByte.of((byte) (((Boolean) t).booleanValue() ? 1 : 0)));
		} else if (clazz.isAssignableFrom(Byte.class)) {
			component.get().set(clazz.getName(), NbtByte.of((Byte) t));
		} else if (clazz.isAssignableFrom(Short.class)) {
			component.get().set(clazz.getName(), NbtShort.of((Short) t));
		} else if (clazz.isAssignableFrom(Integer.class)) {
			component.get().set(clazz.getName(), NbtInt.of((Integer) t));
		} else if (clazz.isAssignableFrom(Long.class)) {
			component.get().set(clazz.getName(), NbtLong.of((Long) t));
		} else if (clazz.isAssignableFrom(Float.class)) {
			component.get().set(clazz.getName(), NbtFloat.of((Float) t));
		} else if (clazz.isAssignableFrom(Double.class)) {
			component.get().set(clazz.getName(), NbtDouble.of((Double) t));
		} else if (clazz.isAssignableFrom(String.class)) {
			component.get().set(clazz.getName(), NbtString.of((String) t));
		} else if (clazz.isAssignableFrom(NbtElement.class)) {
			component.get().set(clazz.getName(), (NbtElement) t);
		}
	}
}