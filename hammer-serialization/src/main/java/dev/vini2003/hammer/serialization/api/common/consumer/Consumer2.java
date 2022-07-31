package dev.vini2003.hammer.serialization.api.common.consumer;

public interface Consumer2<P1, P2> {
	void accept(P1 p1, P2 p2);
}
