package dev.vini2003.hammer.serialization.api.common.node;

import dev.vini2003.hammer.serialization.api.common.deserializer.Deserializer;
import dev.vini2003.hammer.serialization.api.common.deserializer.Serializer;
import dev.vini2003.hammer.serialization.api.common.function.Function1;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ListNode<T, N extends Node<T>> extends Node<List<T>> {
	private final N n;
	
	public ListNode(N n) {
		this.n = n;
	}
	
	@Override
	public <F, I> List<T> deserialize(Deserializer<F> deserializer, @Nullable String key, F object, I instance) {
		return set(deserializer.readList(n, key, object), instance);
	}
	
	@Override
	public <F, O> void serialize(Serializer<F> serializer, @Nullable String key, O value, F object) {
		serializer.writeList(n, key, get(value), object);
	}
	
	@Override
	public String toString() {
		return "ListNode[" + (key == null ? "None" : key) + ", " + n + "]";
	}
}
