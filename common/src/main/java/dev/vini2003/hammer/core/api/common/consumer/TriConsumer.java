package dev.vini2003.hammer.core.api.common.consumer;

public interface TriConsumer<T, U, V> {
	void accept(T t, U u, V v);
}
