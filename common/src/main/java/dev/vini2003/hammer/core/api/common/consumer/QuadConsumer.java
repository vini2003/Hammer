package dev.vini2003.hammer.core.api.common.consumer;

public interface QuadConsumer <T, U, V, W> {
	void accept(T t, U u, V v, W w);
}
