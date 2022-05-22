package dev.vini2003.hammer.core.api.common.cache;

import java.util.function.Supplier;

public class Cached<T> {
	private Supplier<T> supplier;
	
	private T value;
	
	public Cached(Supplier<T> supplier) {
		this.supplier = supplier;
	}
	
	public T get() {
		if (value == null) {
			value = supplier.get();
		}
		
		return value;
	}
	
	public void refresh() {
		value = null;
	}
}
