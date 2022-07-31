package dev.vini2003.hammer.serialization.api.common.node;

import dev.vini2003.hammer.serialization.api.common.consumer.Consumer1;
import dev.vini2003.hammer.serialization.api.common.consumer.Consumer2;
import dev.vini2003.hammer.serialization.api.common.deserializer.Deserializer;
import dev.vini2003.hammer.serialization.api.common.deserializer.Serializer;
import dev.vini2003.hammer.serialization.api.common.function.*;
import dev.vini2003.hammer.serialization.api.common.node.compound.*;
import dev.vini2003.hammer.serialization.api.common.pair.Pair;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class Node<T> {
	protected Function1<?, T> getter;
	protected Consumer2<?, ?> setter;
	
	protected String key = null;
	
	public Node() {
	
	}
	
	public Node(Function1<?, T> getter, String key) {
		this.getter = getter;
		this.key = key;
	}
	
	public <O> Node<T> getter(Function1<O, T> getter) {
		this.getter = getter;
		return this;
	}
	
	public <I, J> Node<T> setter(Consumer2<I, J> setter) {
		this.setter = setter;
		return this;
	}
	
	public Node<T> key(String key) {
		return new Node<>(getter, key) {
			@Override
			public <F, I> T deserialize(Deserializer<F> deserializer, String key, F object, I instance) {
				return Node.this.deserialize(deserializer, this.key, object, instance);
			}
			
			@Override
			public <F, V> void serialize(Serializer<F> serializer, String key, V value, F object) {
				Node.this.serialize(serializer, this.key, value, object, (Function1<? super V, T>) this.getter);
			}
		};
	}
	
	public <U> Node<U> xmap(Function1<T, U> deserializeMapper, Function1<U, T> serializeMapper) {
		return new Node<>((Function1<?, U>) getter, key) {
			@Override
			public <F, I> U deserialize(Deserializer<F> deserializer, @Nullable String key, F object, I instance) {
				return deserializeMapper.apply(Node.this.deserialize(deserializer, key, object, instance));
			}
			
			@Override
			public <F, V> void serialize(Serializer<F> serializer, @Nullable String key, V value, F object) {
				Node.this.serialize(serializer, key, serializeMapper.apply(get(value)), object);
			}
		};
	}
	
	public static Node<Boolean> BOOLEAN = new Node<>() {
		@Override
		public <F, I> Boolean deserialize(Deserializer<F> deserializer, @Nullable String key, F object, I instance) {
			return set(deserializer.readBoolean(key, object), instance);
		}
		
		@Override
		public <F, V> void serialize(Serializer<F> serializer, @Nullable String key, V value, F object) {
			serializer.writeBoolean(key, get(value), object);
		}
	};
	
	public static Node<Byte> BYTE = new Node<>() {
		@Override
		public <F, I> Byte deserialize(Deserializer<F> deserializer, @Nullable String key, F object, I instance) {
			return set(deserializer.readByte(key, object), instance);
		}
		
		@Override
		public <F, V> void serialize(Serializer<F> serializer, @Nullable String key, V value, F object) {
			serializer.writeByte(key, get(value), object);
		}
	};
	
	public static Node<Short> SHORT = new Node<>() {
		@Override
		public <F, I> Short deserialize(Deserializer<F> deserializer, @Nullable String key, F object, I instance) {
			return set(deserializer.readShort(key, object), instance);
		}
		
		@Override
		public <F, V> void serialize(Serializer<F> serializer, @Nullable String key, V value, F object) {
			serializer.writeShort(key, get(value), object);
		}
	};
	
	public static Node<Character> CHAR = new Node<>() {
		@Override
		public <F, I> Character deserialize(Deserializer<F> deserializer, @Nullable String key, F object, I instance) {
			return set(deserializer.readChar(key, object), instance);
		}
		
		@Override
		public <F, V> void serialize(Serializer<F> serializer, @Nullable String key, V value, F object) {
			serializer.writeChar(key, get(value), object);
		}
	};
	
	public static Node<Integer> INTEGER = new Node<>() {
		@Override
		public <F, I> Integer deserialize(Deserializer<F> deserializer, @Nullable String key, F object, I instance) {
			return set(deserializer.readInt(key, object), instance);
		}
		
		@Override
		public <F, V> void serialize(Serializer<F> serializer, @Nullable String key, V value, F object) {
			serializer.writeInt(key, get(value), object);
		}
	};
	
	public static Node<Long> LONG = new Node<>() {
		@Override
		public <F, I> Long deserialize(Deserializer<F> deserializer, @Nullable String key, F object, I instance) {
			return set(deserializer.readLong(key, object), instance);
		}
		
		@Override
		public <F, V> void serialize(Serializer<F> serializer, @Nullable String key, V value, F object) {
			serializer.writeLong(key, get(value), object);
		}
	};
	
	public static Node<Float> FLOAT = new Node<>() {
		@Override
		public <F, I> Float deserialize(Deserializer<F> deserializer, @Nullable String key, F object, I instance) {
			return set(deserializer.readFloat(key, object), instance);
		}
		
		@Override
		public <F, V> void serialize(Serializer<F> serializer, @Nullable String key, V value, F object) {
			serializer.writeFloat(key, get(value), object);
		}
	};
	
	public static Node<Double> DOUBLE = new Node<>() {
		@Override
		public <F, I> Double deserialize(Deserializer<F> deserializer, @Nullable String key, F object, I instance) {
			return set(deserializer.readDouble(key, object), instance);
		}
		
		@Override
		public <F, V> void serialize(Serializer<F> serializer, @Nullable String key, V value, F object) {
			serializer.writeDouble(key, get(value), object);
		}
	};
	
	public static Node<String> STRING = new Node<>() {
		@Override
		public <F, I> String deserialize(Deserializer<F> deserializer, @Nullable String key, F object, I instance) {
			return set(deserializer.readString(key, object), instance);
		}
		
		@Override
		public <F, V> void serialize(Serializer<F> serializer, @Nullable String key, V value, F object) {
			serializer.writeString(key, get(value), object);
		}
	};
	
	public <F, V> void serialize(Serializer<F> serializer, V value, F object) {
		serialize(serializer, null, value, object);
	}
	
	public <F, V> void serialize(Serializer<F> serializer, V value, F object, Function1<V, T> getter) {
		serialize(serializer, null, value, object, getter);
	}
	
	public <F, V> void serialize(Serializer<F> serializer, @Nullable String key, V value, F object, Function1<V, T> getter) {
		serialize(serializer, key, getter == null ? value : getter.apply(value), object);
	}
	
	public abstract <F, V> void serialize(Serializer<F> serializer, @Nullable String key, V value, F object);
	
	public <F> T deserialize(Deserializer<F> deserializer, F object) {
		return deserialize(deserializer, object, null);
	}
	
	public <F> T deserialize(Deserializer<F> deserializer, @Nullable String key, F object) {
		return deserialize(deserializer, key, object, null);
	}
	
	public <F, I> T deserialize(Deserializer<F> deserializer, F object, I instance) {
		return deserialize(deserializer, key, object, instance);
	}
	
	public abstract <F, I> T deserialize(Deserializer<F> deserializer, @Nullable String key, F object, I instance);
	
	protected <V> T get(V value) {
		return getter == null ? (T) value : ((Function1<V, T>) getter).apply(value);
	}
	
	public <T, I> T set(T value, I instance) {
		if (instance != null && setter != null) {
			((Consumer2<I, T>) setter).accept((I) get(instance), value);
		}
		
		return value;
	}
	
	public String getKey() {
		return key;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "Node[" + (key == null ? "None" : key) + "]";
	}
	
	public static <T1, T2> Node<Map<T1, T2>> map(Node<T1> keyNode, Node<T2> valueNode) {
		return new MapNode<>(keyNode, valueNode);
	}
	
	public static <T> Node<List<T>> list(Node<T> valueNode) {
		return new ListNode<>(valueNode);
	}
	
	public static <T> Node<Optional<T>> optional(Node<T> valueNode) {
		return new OptionalNode<>(valueNode);
	}
	
	public static <T1, T2> Node<Pair<T1, T2>> pair(Node<T1> firstNode, Node<T2> secondNode) {
		return new PairNode<>(firstNode, secondNode);
	}
	
	public static <R, T1, N1 extends Node<T1>> CompoundNode1<R, T1, N1> compound(N1 n1, Function1<T1, R> mapper) {
		return new CompoundNode1<>(n1, mapper);
	}
	
	public static <R, T1, T2, N1 extends Node<T1>, N2 extends Node<T2>> CompoundNode2<R, T1, T2, N1, N2> compound(N1 n1, N2 n2, Function2<T1, T2, R> mapper) {
		return new CompoundNode2<>(n1, n2, mapper);
	}
	
	public static <R, T1, T2, T3, N1 extends Node<T1>, N2 extends Node<T2>, N3 extends Node<T3>> CompoundNode3<R, T1, T2, T3, N1, N2, N3> compound(N1 n1, N2 n2, N3 n3, Function3<T1, T2, T3, R> mapper) {
		return new CompoundNode3<>(n1, n2, n3, mapper);
	}
	
	public static <R, T1, T2, T3, T4, N1 extends Node<T1>, N2 extends Node<T2>, N3 extends Node<T3>, N4 extends Node<T4>> CompoundNode4<R, T1, T2, T3, T4, N1, N2, N3, N4> compound(N1 n1, N2 n2, N3 n3, N4 n4, Function4<T1, T2, T3, T4, R> mapper) {
		return new CompoundNode4<>(n1, n2, n3, n4, mapper);
	}
	
	public static <R, T1, T2, T3, T4, T5, N1 extends Node<T1>, N2 extends Node<T2>, N3 extends Node<T3>, N4 extends Node<T4>, N5 extends Node<T5>> CompoundNode5<R, T1, T2, T3, T4, T5, N1, N2, N3, N4, N5> compound(N1 n1, N2 n2, N3 n3, N4 n4, N5 n5, Function5<T1, T2, T3, T4, T5, R> mapper) {
		return new CompoundNode5<>(n1, n2, n3, n4, n5, mapper);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, N1 extends Node<T1>, N2 extends Node<T2>, N3 extends Node<T3>, N4 extends Node<T4>, N5 extends Node<T5>, N6 extends Node<T6>> CompoundNode6<R, T1, T2, T3, T4, T5, T6, N1, N2, N3, N4, N5, N6> compound(N1 n1, N2 n2, N3 n3, N4 n4, N5 n5, N6 n6, Function6<T1, T2, T3, T4, T5, T6, R> mapper) {
		return new CompoundNode6<>(n1, n2, n3, n4, n5, n6, mapper);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7, N1 extends Node<T1>, N2 extends Node<T2>, N3 extends Node<T3>, N4 extends Node<T4>, N5 extends Node<T5>, N6 extends Node<T6>, N7 extends Node<T7>> CompoundNode7<R, T1, T2, T3, T4, T5, T6, T7, N1, N2, N3, N4, N5, N6, N7> compound(N1 n1, N2 n2, N3 n3, N4 n4, N5 n5, N6 n6, N7 n7, Function7<T1, T2, T3, T4, T5, T6, T7, R> mapper) {
		return new CompoundNode7<>(n1, n2, n3, n4, n5, n6, n7, mapper);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7, T8, N1 extends Node<T1>, N2 extends Node<T2>, N3 extends Node<T3>, N4 extends Node<T4>, N5 extends Node<T5>, N6 extends Node<T6>, N7 extends Node<T7>, N8 extends Node<T8>> CompoundNode8<R, T1, T2, T3, T4, T5, T6, T7, T8, N1, N2, N3, N4, N5, N6, N7, N8> compound(N1 n1, N2 n2, N3 n3, N4 n4, N5 n5, N6 n6, N7 n7, N8 n8, Function8<T1, T2, T3, T4, T5, T6, T7, T8, R> mapper) {
		return new CompoundNode8<>(n1, n2, n3, n4, n5, n6, n7, n8, mapper);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7, T8, T9, N1 extends Node<T1>, N2 extends Node<T2>, N3 extends Node<T3>, N4 extends Node<T4>, N5 extends Node<T5>, N6 extends Node<T6>, N7 extends Node<T7>, N8 extends Node<T8>, N9 extends Node<T9>> CompoundNode9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, N1, N2, N3, N4, N5, N6, N7, N8, N9> compound(N1 n1, N2 n2, N3 n3, N4 n4, N5 n5, N6 n6, N7 n7, N8 n8, N9 n9, Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> mapper) {
		return new CompoundNode9<>(n1, n2, n3, n4, n5, n6, n7, n8, n9, mapper);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, N1 extends Node<T1>, N2 extends Node<T2>, N3 extends Node<T3>, N4 extends Node<T4>, N5 extends Node<T5>, N6 extends Node<T6>, N7 extends Node<T7>, N8 extends Node<T8>, N9 extends Node<T9>, N10 extends Node<T10>> CompoundNode10<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10> compound(N1 n1, N2 n2, N3 n3, N4 n4, N5 n5, N6 n6, N7 n7, N8 n8, N9 n9, N10 n10, Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> mapper) {
		return new CompoundNode10<>(n1, n2, n3, n4, n5, n6, n7, n8, n9, n10, mapper);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, N1 extends Node<T1>, N2 extends Node<T2>, N3 extends Node<T3>, N4 extends Node<T4>, N5 extends Node<T5>, N6 extends Node<T6>, N7 extends Node<T7>, N8 extends Node<T8>, N9 extends Node<T9>, N10 extends Node<T10>, N11 extends Node<T11>> CompoundNode11<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11> compound(N1 n1, N2 n2, N3 n3, N4 n4, N5 n5, N6 n6, N7 n7, N8 n8, N9 n9, N10 n10, N11 n11, Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R> mapper) {
		return new CompoundNode11<>(n1, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11, mapper);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, N1 extends Node<T1>, N2 extends Node<T2>, N3 extends Node<T3>, N4 extends Node<T4>, N5 extends Node<T5>, N6 extends Node<T6>, N7 extends Node<T7>, N8 extends Node<T8>, N9 extends Node<T9>, N10 extends Node<T10>, N11 extends Node<T11>, N12 extends Node<T12>> CompoundNode12<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12> compound(N1 n1, N2 n2, N3 n3, N4 n4, N5 n5, N6 n6, N7 n7, N8 n8, N9 n9, N10 n10, N11 n11, N12 n12, Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R> mapper) {
		return new CompoundNode12<>(n1, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11, n12, mapper);
	}
}