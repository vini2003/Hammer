package dev.vini2003.hammer.core.api.common.data;

import dev.vini2003.hammer.core.api.common.component.TrackedDataComponent;
import net.minecraft.nbt.*;

import java.util.function.Supplier;

/**
 * A {@link TrackedDataComponent} is a data tracker that utilizes a
 * component for synchronization purposes.
 */
public class TrackedDataHandler<T> {
	private final Supplier<TrackedDataComponent> component;
	
	private final Class<T> clazz;
	
	private final String key;
	
	private final T defaultValue;
	
	/**
	 * Constructs a tracked data handler.
	 * @param component the component to save to.
	 * @param clazz the data's class.
	 * @param defaultValue the default value.
	 * @param key the key to use when serializing to NBT.
	 */
	public TrackedDataHandler(Supplier<TrackedDataComponent> component, Class<T> clazz, T defaultValue, String key) {
		this.component = component;
		
		this.clazz = clazz;
		
		this.defaultValue = defaultValue;
		
		this.key = key;
	}
	
	/**
	 * Returns this handler's tracked data.
	 * @return this handler's tracked data.
	 */
	public T get() {
		T data = null;
		
		if (clazz.isAssignableFrom(Boolean.class)) {
			if (component.get().get(key) instanceof NbtByte nbtByte) {
				data = (T) Boolean.valueOf((nbtByte).byteValue() == 1);
			}
		} else if (clazz.isAssignableFrom(Byte.class)) {
			if (component.get().get(key) instanceof NbtByte nbtByte) {
				data = (T) Byte.valueOf((nbtByte).byteValue());
			}
		} else if (clazz.isAssignableFrom(Short.class)) {
			if (component.get().get(key) instanceof NbtShort nbtShort) {
				data = (T) Short.valueOf((nbtShort).shortValue());
			}
		} else if (clazz.isAssignableFrom(Integer.class)) {
			if (component.get().get(key) instanceof NbtInt nbtInt) {
				data = (T) Integer.valueOf((nbtInt).intValue());
			}
		} else if (clazz.isAssignableFrom(Long.class)) {
			if (component.get().get(key) instanceof NbtLong nbtLong) {
				data = (T) Long.valueOf((nbtLong).longValue());
			}
		} else if (clazz.isAssignableFrom(Float.class)) {
			if (component.get().get(key) instanceof NbtFloat nbtFloat) {
				data = (T) Float.valueOf((nbtFloat).floatValue());
			}
		} else if (clazz.isAssignableFrom(Double.class)) {
			if (component.get().get(key) instanceof NbtDouble nbtDouble) {
				data = (T) Double.valueOf((nbtDouble).doubleValue());
			}
		} else if (clazz.isAssignableFrom(String.class)) {
			if (component.get().get(key) instanceof NbtString nbtString) {
				data = (T) nbtString.asString();
			}
		} else if (clazz.isAssignableFrom(NbtElement.class)) {
			if (component.get().get(key)) {
				data = component.get().get(clazz.getName());
			}
		}
		
		if (data == null) {
			data = defaultValue;
		}
		
		return data;
	}
	
	/**
	 * Sets this handler's tracked data.
	 * @param data the new tracked data.
	 */
	public void set(T data) {
		if (clazz.isAssignableFrom(Boolean.class)) {
			component.get().set(key, NbtByte.of((byte) (((Boolean) data).booleanValue() ? 1 : 0)));
		} else if (clazz.isAssignableFrom(Byte.class)) {
			component.get().set(clazz.getName(), NbtByte.of((Byte) data));
		} else if (clazz.isAssignableFrom(Short.class)) {
			component.get().set(clazz.getName(), NbtShort.of((Short) data));
		} else if (clazz.isAssignableFrom(Integer.class)) {
			component.get().set(clazz.getName(), NbtInt.of((Integer) data));
		} else if (clazz.isAssignableFrom(Long.class)) {
			component.get().set(clazz.getName(), NbtLong.of((Long) data));
		} else if (clazz.isAssignableFrom(Float.class)) {
			component.get().set(clazz.getName(), NbtFloat.of((Float) data));
		} else if (clazz.isAssignableFrom(Double.class)) {
			component.get().set(clazz.getName(), NbtDouble.of((Double) data));
		} else if (clazz.isAssignableFrom(String.class)) {
			component.get().set(clazz.getName(), NbtString.of((String) data));
		} else if (clazz.isAssignableFrom(NbtElement.class)) {
			component.get().set(clazz.getName(), (NbtElement) data);
		}
	}
}