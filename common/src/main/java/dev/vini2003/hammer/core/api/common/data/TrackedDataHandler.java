package dev.vini2003.hammer.core.api.common.data;

import dev.vini2003.hammer.core.api.common.component.TrackedDataComponent;

import java.util.function.Supplier;

/**
 * A {@link TrackedDataComponent} is a data tracker that utilizes a
 * component for synchronization purposes.
 */
public class TrackedDataHandler<T> {
	private final Supplier<TrackedDataComponent> component;
	
	private final Class<T> _class;
	
	private final String key;
	
	private final T defaultValue;
	
	/**
	 * Constructs a tracked data handler.
	 * @param component the component to save to.
	 * @param _class the data's class.
	 * @param defaultValue the default value.
	 * @param key the key to use when serializing to NBT.
	 */
	public TrackedDataHandler(Supplier<TrackedDataComponent> component, Class<T> _class, T defaultValue, String key) {
		this.component = component;
		
		this._class = _class;
		
		this.defaultValue = defaultValue;
		
		this.key = key;
	}
	
	/**
	 * Returns this handler's tracked data.
	 * @return this handler's tracked data.
	 */
	public T get() {
		var data = component.get().get(key, _class);
		
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
		component.get().set(key, _class, data);
	}
	
	public Class<T> _getClass() {
		return _class;
	}
}