package dev.vini2003.hammer.core.api.common.function;

public interface QuadFunction<T, U, V, W, X> {
	X apply(T t, U u, V v, W w);
}
